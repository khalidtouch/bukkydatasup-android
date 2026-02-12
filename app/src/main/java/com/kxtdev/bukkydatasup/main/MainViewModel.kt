package com.kxtdev.bukkydatasup.main

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.kxtdev.bukkydatasup.common.datastore.SharedPrefProvider
import com.kxtdev.bukkydatasup.common.datastore.UserPreference
import com.kxtdev.bukkydatasup.common.datastore.model.DarkThemeConfig
import com.kxtdev.bukkydatasup.common.datastore.model.ThemeBrand
import com.kxtdev.bukkydatasup.common.datastore.model.UserData
import com.kxtdev.bukkydatasup.common.models.AppUiState
import com.kxtdev.bukkydatasup.common.enums.AuthState
import com.kxtdev.bukkydatasup.common.enums.FundOption
import com.kxtdev.bukkydatasup.common.enums.LoggedInSessionState
import com.kxtdev.bukkydatasup.common.enums.PreferenceItem
import com.kxtdev.bukkydatasup.common.models.LoggedInUserState
import com.kxtdev.bukkydatasup.common.models.PreferenceScreenUiState
import com.kxtdev.bukkydatasup.common.models.PreferenceUiState
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.common.enums.ResetMode
import com.kxtdev.bukkydatasup.common.enums.Status
import com.kxtdev.bukkydatasup.common.models.Advertisement
import com.kxtdev.bukkydatasup.common.models.AlertNotification
import com.kxtdev.bukkydatasup.common.models.LogoutResponse
import com.kxtdev.bukkydatasup.common.models.RegisterFCMTokenRequest
import com.kxtdev.bukkydatasup.common.models.ResetPasswordRequest
import com.kxtdev.bukkydatasup.common.models.ResetPasswordResponse
import com.kxtdev.bukkydatasup.common.models.ResetTransactionPinResponse
import com.kxtdev.bukkydatasup.common.models.SecurityRequest
import com.kxtdev.bukkydatasup.common.models.TransactionStatus
import com.kxtdev.bukkydatasup.common.models.UpgradeUserResponse
import com.kxtdev.bukkydatasup.common.models.UserInfoResponse
import com.kxtdev.bukkydatasup.common.models.Verification
import com.kxtdev.bukkydatasup.common.models.VerifyReservedAccountsRequest
import com.kxtdev.bukkydatasup.common.models.VerifyReservedAccountsResponse
import com.kxtdev.bukkydatasup.common.utils.BaseViewModel
import com.kxtdev.bukkydatasup.common.utils.getFCMToken
import com.kxtdev.bukkydatasup.domain.repository.AppRepository
import com.kxtdev.bukkydatasup.navigation.AppNavigation
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import com.kxtdev.bukkydatasup.network.utils.SessionManager
import com.kxtdev.bukkydatasup.workers.ClearWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

val TAG = MainViewModel::class.java.simpleName

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AppRepository,
    private val sharedPrefProvider: SharedPrefProvider,
    private val preferences: UserPreference,
    private val sessionManager: SessionManager,
) : BaseViewModel() {
    private val mSelectedProduct = MutableStateFlow<Product>(Product.AIRTIME)

    private val mLoggedInUserState = MutableStateFlow<LoggedInUserState>(LoggedInUserState())
    val loggedInUserState: StateFlow<LoggedInUserState> = mLoggedInUserState.asStateFlow()

    private val mAppUiState = MutableStateFlow<AppUiState>(AppUiState())
    val appUiState: StateFlow<AppUiState> = mAppUiState.asStateFlow()

    private val mTransactionStatus = MutableStateFlow<TransactionStatus>(TransactionStatus())
    val transactionStatus: StateFlow<TransactionStatus> = mTransactionStatus.asStateFlow()

    val preferenceUiState: StateFlow<PreferenceUiState> = preferenceUiState(
        preferences.userData
    )
        .stateIn(
            scope = viewModelScope,
            initialValue = PreferenceUiState(),
            started = SharingStarted.WhileSubscribed(5_000)
        )

    private val mSecurityRequest = MutableStateFlow<SecurityRequest>(SecurityRequest())
    val securityRequest: StateFlow<SecurityRequest> = mSecurityRequest.asStateFlow()

    private val mPreferenceScreenUiState = MutableStateFlow<PreferenceScreenUiState>(
        PreferenceScreenUiState()
    )
    val preferenceScreenUiState: StateFlow<PreferenceScreenUiState> =
        mPreferenceScreenUiState.asStateFlow()

    val lastDestination: StateFlow<String> = getLastDestination(preferences)
        .stateIn(
            scope = viewModelScope,
            initialValue = AppNavigation.HomeScreen.route,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    private val mAdvertisements = MutableStateFlow<List<Advertisement>>(listOf())
    val advertisements: StateFlow<List<Advertisement>> = mAdvertisements.asStateFlow()

    fun onBottomNavSelected(index: Int) {
        mAppUiState.update { it.copy(selectedBottomNavItemIndex = index) }
    }

    fun updateAuthState(authState: AuthState) = launch {
        mAppUiState.update { it.copy(authState = authState) }
    }

    override fun onHandledThrowable() {
        mLoggedInUserState.update { it.copy(error = null) }
        mPreferenceScreenUiState.update { it.copy(error = null) }
    }

    fun updateTransactionStatus(status: TransactionStatus) {
        mTransactionStatus.update {
            it.copy(
                header = status.header,
                description = status.description,
                status = status.status,
                itemId = status.itemId,
            )
        }
    }

    fun refreshUser() {
        getUserProfile()
    }

    private fun getUserProfile() = launch(Dispatchers.IO) {
        val hasInitialized = mAppUiState.value.hasInitialized
        if (hasInitialized != true) {
            mLoggedInUserState.update { it.copy(isLoading = true) }
        }
        val userProfile = repository.getUserProfile()
        userProfile
            .onSuccess { data ->
                sharedPrefProvider.saveTransactionPin(data.user?.pin.orEmpty())
                mLoggedInUserState.update {
                    it.copy(
                        username = data.user?.username,
                        email = data.user?.email,
                        walletBalance = data.user?.walletBalance,
                        accountBalance = data.user?.accountBalance,
                        referralBonus = data.user?.bonusBalance,
                        pin = data.user?.pin.orEmpty(),
                        userId = data.user?.id,
                        address = data.user?.address,
                        userType = data.user?.userType,
                        isAccountVerified = data.user?.isAccountVerified,
                        isLoading = null,
                        topUpPercentage = data.topUpPercentage,
                        rawDataPlans = data.dataPlans,
                        rawCablePlans = data.cablePlans,
                        banks = data.user?.userBanks,
                        phone = data.user?.phone,
                        bankName = data.user?.reservedBankName,
                        accountNumber = data.user?.reservedAccountNumber,
                        isEmailVerified = data.user?.isEmailVerified,
                        fullname = data.user?.fullname,
                        supportPhoneNumber = data.supportPhoneNumber,
                        groupLink = data.groupLink,
                        affiliateUpgradeFee = data.affiliateUpgradeFee,
                        topUserUpgradeFee = data.topUserUpgradeFee,
                        examResponse = data.exams,
                        rechargeResponse = data.recharge,
                        percentage = data.percentage,
                        verification = Verification(
                            bvnVerified = data.verification?.bvnVerified,
                            ninVerified = data.verification?.ninVerified,
                        ),
                    )
                }
            }
            .onError { msg ->
                mLoggedInUserState.update { it.copy(error = Throwable(msg), isLoading = null) }
            }
    }

    fun updateAppInitializationState(initialized: Boolean?) {
        mAppUiState.update { it.copy(hasInitialized = initialized) }
    }

    fun updateFundOption(option: FundOption?) {
        mAppUiState.update { it.copy(selectedFundOption = option) }
    }

    fun selectProduct(product: Product) {
        mSelectedProduct.value = product
        mAppUiState.update { it.copy(product = product) }
    }


    fun logout(context: Context, callback: () -> Unit) = launch(Dispatchers.Main) {
        mPreferenceScreenUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "logging out...",
            )
        }
        mLoggedInUserState.value = LoggedInUserState()
        ClearWorker.engage(context)
        val response = repository.logout()
        sessionManager.saveLoggedInState(LoggedInSessionState.LOGGED_OUT.name)
        processLogout(response, callback)
    }

    private fun processLogout(
        result: NetworkResult<LogoutResponse>,
        callback: () -> Unit
    ) {
        result
            .onSuccess { _ ->
                callback.invoke()
                mPreferenceScreenUiState.update {
                    it.copy(
                        isLoading = null,
                        loadingMessage = null
                    )
                }
            }
            .onError {
                callback.invoke()
                mPreferenceScreenUiState.update {
                    it.copy(
                        isLoading = null,
                        loadingMessage = null
                    )
                }
            }
    }

    fun updatePreferenceItem(item: PreferenceItem) = launch {
        mAppUiState.update { it.copy(preference = item) }
        mPreferenceScreenUiState.update { it.copy(preferenceItem = item) }
        mSecurityRequest.update { it.copy(preferenceItem = item) }
    }

    fun onToggleShouldEnableTransaction(currentState: Boolean) {
        launch {
            preferences.setShouldEnableTransactionPin(currentState)
        }
    }

    fun onToggleShouldEnableBiometrics(currentState: Boolean) {
        launch {
            preferences.setShouldEnableBiometrics(currentState)
        }
    }

    fun updateResetMode(mode: ResetMode?) {
        mPreferenceScreenUiState.update {
            it.copy(
                resetMode = mode
            )
        }
    }

    fun updateResetPassword(p: String, mode: ResetMode) {
        when (mode) {
            ResetMode.NEW_PASSWORD -> {
                mSecurityRequest.update {
                    it.copy(newPassword1 = p)
                }
            }

            ResetMode.NEW_PASSWORD_AGAIN -> {
                mSecurityRequest.update {
                    it.copy(newPassword2 = p)
                }
            }

            else -> {
                mSecurityRequest.update {
                    it.copy(password = p)
                }
            }
        }
    }

    fun updateStatusPageAs(show: Boolean?) {
        mPreferenceScreenUiState.update {
            it.copy(
                shouldShowStatusPage = show
            )
        }
    }


    fun resetPassword(callback: (TransactionStatus) -> Unit) = launch(Dispatchers.IO) {
        mPreferenceScreenUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "Resetting password...",
            )
        }
        val password = mSecurityRequest.value.password
        val newPassword1 = mSecurityRequest.value.newPassword1
        val newPassword2 = mSecurityRequest.value.newPassword2
        var error = ""

        if (password == null) error = "Enter password"
        if (newPassword1 == null) error = "Enter new password"
        if (newPassword2 == null) error = "Verify new password"

        if (error.isNotBlank()) {
            mPreferenceScreenUiState.update {
                it.copy(
                    isLoading = null,
                    error = Throwable(error),
                    loadingMessage = null,
                )
            }
        } else {
            val response = repository.resetPassword(
                ResetPasswordRequest(
                    oldPassword = password!!,
                    newPassword1 = newPassword1!!,
                    newPassword2 = newPassword2!!
                )
            )
            processResetPasswordResult(response, callback)
        }
    }


    private fun processResetPasswordResult(
        result: NetworkResult<ResetPasswordResponse>,
        callback: (TransactionStatus) -> Unit
    ) {
        result
            .onSuccess { data ->
                callback(
                    TransactionStatus(
                        status = Status.SUCCESSFUL,
                        header = "Password Reset Complete!",
                        description = data.status
                    )
                )
                mPreferenceScreenUiState.update {
                    it.copy(
                        isLoading = null,
                        loadingMessage = null,
                        shouldShowStatusPage = true,
                        resetMode = ResetMode.PASSWORD_VERIFICATION,
                    )
                }
                mSecurityRequest.update {
                    it.copy(
                        password = null,
                        newPassword1 = null,
                        newPassword2 = null
                    )
                }
            }
            .onError { msg ->
                callback(
                    TransactionStatus(
                        status = Status.FAILED,
                        header = "Ouch!",
                        description = msg
                    )
                )
                mPreferenceScreenUiState.update {
                    it.copy(
                        isLoading = null,
                        loadingMessage = null,
                        shouldShowStatusPage = true,
                        resetMode = ResetMode.PASSWORD_VERIFICATION,
                    )
                }
                mSecurityRequest.update {
                    it.copy(
                        password = null,
                        newPassword1 = null,
                        newPassword2 = null
                    )
                }
            }
    }


    fun resetTransactionPin(callback: (TransactionStatus) -> Unit) = launch(Dispatchers.IO) {
        mPreferenceScreenUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "Resetting transaction pin...",
            )
        }
        val pin1 = mSecurityRequest.value.pin1
        val pin2 = mSecurityRequest.value.pin2
        val password = if(appUiState.value.authState == AuthState.SIGNUP) getSavedPassword() else
            mSecurityRequest.value.password
        var error = ""

        if (pin1.isNullOrBlank()) error = "Enter new transaction pin"
        if (pin2.isNullOrBlank()) error = "Verify your new transaction pin"
        if (password.isNullOrBlank()) error = "Enter your password to continue"

        if (error.isNotBlank()) {
            mPreferenceScreenUiState.update {
                it.copy(
                    isLoading = null,
                    error = Throwable(error),
                    loadingMessage = null,
                )
            }
        } else {
            val response = repository.resetTransactionPin(
                pin1 = pin1!!,
                pin2 = pin2!!,
                password = password!!,
            )
            processResetTransactionPinResult(response, callback)
        }
    }


    private fun processResetTransactionPinResult(
        result: NetworkResult<ResetTransactionPinResponse>,
        callback: (TransactionStatus) -> Unit
    ) {
        result
            .onSuccess { data ->
                callback(
                    TransactionStatus(
                        status = Status.SUCCESSFUL,
                        header = "Pin Reset Complete!",
                        description = data.message
                    )
                )
                mPreferenceScreenUiState.update {
                    it.copy(
                        isLoading = null,
                        loadingMessage = null,
                        shouldShowStatusPage = true,
                        resetMode = ResetMode.PASSWORD_VERIFICATION,
                    )
                }
                mSecurityRequest.update {
                    it.copy(
                        pin1 = null, pin2 = null, password = null
                    )
                }
                mAppUiState.update { it.copy(authState = AuthState.QUICK_SIGN_IN) }
            }
            .onError { msg ->
                callback(
                    TransactionStatus(
                        status = Status.FAILED,
                        header = "Ouch!",
                        description = msg
                    )
                )
                mPreferenceScreenUiState.update {
                    it.copy(
                        isLoading = null,
                        loadingMessage = null,
                        shouldShowStatusPage = true,
                        resetMode = ResetMode.PASSWORD_VERIFICATION,
                    )
                }
                mSecurityRequest.update {
                    it.copy(
                        pin1 = null, pin2 = null, password = null
                    )
                }
            }
    }

    fun updateTransactionPin(p: String, mode: ResetMode) {
        when (mode) {
            ResetMode.NEW_PIN -> {
                mSecurityRequest.update { it.copy(pin1 = p) }
            }

            ResetMode.NEW_PIN_AGAIN -> {
                mSecurityRequest.update { it.copy(pin2 = p) }
            }

            else -> Unit
        }
    }

    fun upgradeUser(newPackage: String?, callback: (TransactionStatus) -> Unit) =
        launch(Dispatchers.IO) {
            mPreferenceScreenUiState.update {
                it.copy(
                    isLoading = true,
                    loadingMessage = "initiating user upgrade...",
                )
            }

            var error = ""
            if (newPackage.isNullOrBlank()) error = "Specify upgrade type"

            if (error.isNotBlank()) {
                mPreferenceScreenUiState.update {
                    it.copy(
                        isLoading = null,
                        error = Throwable(error),
                        loadingMessage = null,
                    )
                }
            } else {
                val response = repository.upgradeUser(newPackage!!)
                processUpgradeUserResult(response, callback)
            }
        }

    private fun processUpgradeUserResult(
        result: NetworkResult<UpgradeUserResponse>,
        callback: (TransactionStatus) -> Unit
    ) {
        var apiResponse: String

        result
            .onSuccess { _ ->
                apiResponse = "Upgraded user account successfully!"
                callback(
                    TransactionStatus(
                        status = Status.SUCCESSFUL,
                        header = "Account Upgrade Complete",
                        description = apiResponse
                    )
                )
                mPreferenceScreenUiState.update {
                    it.copy(
                        isLoading = null,
                        loadingMessage = null,
                        shouldShowStatusPage = true,
                    )
                }
            }
            .onError { msg ->
                callback(
                    TransactionStatus(
                        status = Status.FAILED,
                        header = "Ouch!",
                        description = msg
                    )
                )
                mPreferenceScreenUiState.update {
                    it.copy(
                        isLoading = null,
                        loadingMessage = null,
                        shouldShowStatusPage = true,
                    )
                }
            }
    }

    fun updateFullname(fullname: String) {
        mSecurityRequest.update { it.copy(fullname = fullname) }
    }

    fun updateBvn(bvn: String) {
        mSecurityRequest.update { it.copy(bvn = bvn) }
    }

    fun verifyReservedAccounts(callback: (TransactionStatus) -> Unit) = launch {
        mPreferenceScreenUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "Generating customer accounts",
            )
        }
        val fullname = mSecurityRequest.value.fullname
        val bvn = mSecurityRequest.value.bvn
        val nin = mSecurityRequest.value.nin
        var error = ""

        if (!mSecurityRequest.value.canVerifyAccount) {
            if (fullname.isNullOrBlank()) error = "Please enter your fullname"
            if (bvn.isNullOrBlank()) error = "Please enter your BVN"
        }

        if (error.isNotBlank()) {
            mPreferenceScreenUiState.update {
                it.copy(
                    isLoading = null,
                    error = Throwable(error),
                    loadingMessage = null,
                )
            }
            return@launch
        }

        val result = repository.verifyReservedAccounts(
            VerifyReservedAccountsRequest(
                method = "BVNMETHOD",
                fullname = fullname!!.trim(),
                bvn = bvn?.trim(),
                nin = nin?.trim(),
            )
        )
        processVerifyReservedAccountsResult(result, callback)
    }

    private fun processVerifyReservedAccountsResult(
        result: NetworkResult<VerifyReservedAccountsResponse>,
        callback: (TransactionStatus) -> Unit
    ) {
        when (result) {
            is NetworkResult.Success -> {
                mPreferenceScreenUiState.update {
                    it.copy(
                        isLoading = null,
                        loadingMessage = null,
                        shouldShowStatusPage = true,
                    )
                }
                mSecurityRequest.update {
                    it.copy(
                        fullname = null,
                        bvn = null,
                    )
                }
                callback(
                    TransactionStatus(
                        status = Status.SUCCESSFUL,
                        header = "Congratulations!",
                        description = result.data.message
                    )
                )
            }

            is NetworkResult.Failed -> Unit
            is NetworkResult.Error -> {
                mPreferenceScreenUiState.update {
                    it.copy(
                        isLoading = null,
                        loadingMessage = null,
                        shouldShowStatusPage = true,
                    )
                }
                mSecurityRequest.update {
                    it.copy(
                        fullname = null,
                        bvn = null,
                    )
                }
                callback(
                    TransactionStatus(
                        status = Status.FAILED,
                        header = "Oops!",
                        description = result.message
                    )
                )
            }
        }
    }

    fun getLoggedInSessionState(): String? {
        return sessionManager.getLoggedInState()
    }

    fun updateFCMToken() {
        getFCMToken { token ->
            launch {
                if (token.isNotBlank()) {
                    repository.registerFCMToken(RegisterFCMTokenRequest(token))
                }
            }
        }
    }


    fun updateThemeBrand(themeBrand: ThemeBrand) = launch(Dispatchers.IO) {
        preferences.updateThemeBrand(themeBrand)
    }

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) = launch(Dispatchers.IO) {
        preferences.updateDarkTheme(darkThemeConfig)
    }

    fun updateDynamicColorPreference(useDynamicColor: Boolean) = launch(Dispatchers.IO) {
        preferences.updateDynamicColorPreference(useDynamicColor)
    }

    fun updateThemeDialogState(show: Boolean?) = launch {
        mPreferenceScreenUiState.update { it.copy(shouldShowThemeDialog = show) }
    }

    fun saveLastDestination(destination: String) = launch(Dispatchers.IO) {
        preferences.saveLastDestination(destination)
    }

    fun lockScreen(shouldLock: Boolean) = launch(Dispatchers.IO) {
        preferences.lockScreen(shouldLock)
    }

    fun getDisabledServices() = launch(Dispatchers.IO) {
        val disabledServices = repository.getDisabledServices()
        disabledServices.onSuccess { data ->
            mAppUiState.update { it.copy(disabledServices = data) }
        }
    }

    fun getAlertNotification() = launch(Dispatchers.IO) {
        repository.getAlertNotification()
            .onSuccess { alert ->
                updateAlertNotification(alert)
            }
    }

    private fun updateAlertNotification(alert: AlertNotification?) {
        mAppUiState.update { it.copy(alertNotification = alert) }
    }

    fun getDisabledNetworks() = launch(Dispatchers.IO) {
        val disabledNetworks = repository.getDisabledNetworks()
        disabledNetworks.onSuccess { data ->
            mAppUiState.update { it.copy(disabledNetworks = data) }
        }
    }

    fun onToggleShouldEnablePassCode(currentState: Boolean) {
        launch {
            preferences.setShouldEnablePassCode(currentState)
        }
    }

    fun updateNin(nin: String) {
        mSecurityRequest.update { it.copy(nin = nin) }
    }

    fun getAds() = launch(Dispatchers.IO) {
        mAdvertisements.value = repository.getAds()
    }

    fun getSavedPassword(): String? {
        return sessionManager.getPassword()
    }

}


@OptIn(ExperimentalCoroutinesApi::class)
private fun preferenceUiState(
    userData: Flow<UserData>
): Flow<PreferenceUiState> {
    return userData.mapLatest { pref ->
        PreferenceUiState(
            shouldEnableTransactionPin = pref.shouldEnableTransactionPin,
            shouldEnableBiometrics = pref.shouldEnableBiometrics,
            isLoggedIn = pref.isLoggedIn,
            useDynamicColor = pref.useDynamicColor,
            themeBrand = pref.themeBrand,
            darkThemeConfig = pref.darkThemeConfig,
            shouldEnablePassCode = pref.shouldEnablePassCode,
        )
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun getLoggedInUserResponse(
    repository: AppRepository,
    userData: Flow<UserData>,
): Flow<UserInfoResponse?> {
    return userData.flatMapLatest { user ->
        repository.getUserProfileCached(
            user.username.orEmpty()
        )
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
private fun getLastDestination(
    preferences: UserPreference
): Flow<String> {
    return preferences.userData.mapLatest { user ->
        user.lastDestination ?: AppNavigation.HomeScreen.route
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun getLockScreenState(
    preferences: UserPreference
): Flow<Boolean> {
    return preferences.userData.mapLatest { user ->
        user.shouldLockScreen
    }
}

private fun getAuthenticationToken(
    sessionManager: SessionManager
): Flow<String> {
    val token = sessionManager.getToken()
    return flowOf(token.orEmpty())
}