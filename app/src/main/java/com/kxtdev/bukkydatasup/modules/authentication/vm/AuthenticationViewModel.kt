package com.kxtdev.bukkydatasup.modules.authentication.vm

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.kxtdev.bukkydatasup.BuildConfig
import com.kxtdev.bukkydatasup.common.datastore.SharedPrefProvider
import com.kxtdev.bukkydatasup.common.datastore.UserPreference
import com.kxtdev.bukkydatasup.common.enums.AuthState
import com.kxtdev.bukkydatasup.common.enums.LoggedInSessionState
import com.kxtdev.bukkydatasup.common.enums.Status
import com.kxtdev.bukkydatasup.common.models.AuthenticationRequest
import com.kxtdev.bukkydatasup.common.models.LoginRequest
import com.kxtdev.bukkydatasup.common.models.LoginResponse
import com.kxtdev.bukkydatasup.common.models.RegisterRequest
import com.kxtdev.bukkydatasup.common.models.RegisterResponse
import com.kxtdev.bukkydatasup.common.models.TransactionStatus
import com.kxtdev.bukkydatasup.common.utils.BaseViewModel
import com.kxtdev.bukkydatasup.domain.repository.AppRepository
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import com.kxtdev.bukkydatasup.network.utils.SessionManager
import com.kxtdev.bukkydatasup.workers.RefreshWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val repository: AppRepository,
    private val sessionManager: SessionManager,
    private val sharedPrefProvider: SharedPrefProvider,
    private val preference: UserPreference,
): BaseViewModel() {

    private val mAuthenticationUiState = MutableStateFlow<AuthenticationUiState>(
        AuthenticationUiState()
    )
    val authenticationUiState: StateFlow<AuthenticationUiState> = mAuthenticationUiState.asStateFlow()

    private val mCurrentPage = MutableStateFlow<Int>(1)

    val currentAuthenticationPage: StateFlow<Int> = currentAuthenticationPage(mCurrentPage)
        .stateIn(
            scope = viewModelScope,
            initialValue = 1,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    private val mAuthenticationRequest = MutableStateFlow<AuthenticationRequest>(AuthenticationRequest())
    val authenticationRequest: StateFlow<AuthenticationRequest> = mAuthenticationRequest.asStateFlow()


    fun updateLoadingState(loading: Boolean) {
        mAuthenticationUiState.update { it.copy(isLoading = loading) }
    }

    fun updateUsername(username: String) {
        mAuthenticationRequest.update { it.copy(username = username) }
    }

    fun updatePassword(password: String) {
        mAuthenticationRequest.update { it.copy(password = password) }
    }

    fun updateConfirmPassword(password: String) {
        mAuthenticationRequest.update { it.copy(confirmPassword = password) }
    }

    fun updatePhone(phone: String) {
        mAuthenticationRequest.update { it.copy(phone = phone) }
    }

    fun updateEmail(email: String) {
        mAuthenticationRequest.update { it.copy(email = email) }
    }

    fun updateFullName(name: String) {
        mAuthenticationRequest.update { it.copy(fullname = name) }
    }

    fun updateAddress(address: String) {
        mAuthenticationRequest.update { it.copy(address = address) }
    }

    fun updateRefererUsername(name: String) {
        mAuthenticationRequest.update { it.copy(refererUsername = name) }
    }

    fun updatePasscode(passcode: String) {
        mAuthenticationUiState.update {
            it.copy(passcode = passcode)
        }
    }

    fun onOpenedDashboard() {
        mAuthenticationUiState.update { it.copy(canGoToDashboard = null) }
    }

    fun onAccountCreationCompleted() {
        mAuthenticationUiState.update { it.copy(hasCompletedAccountCreation = null) }
    }

    override fun onHandledThrowable() {
        mAuthenticationUiState.update { it.copy(error = null) }
    }

    fun login(context: Context) = launch {
        mAuthenticationUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "Logging in..."
            )
        }
        val username = mAuthenticationRequest.value.username
        val password = mAuthenticationRequest.value.password
        var error = ""

        if(!mAuthenticationRequest.value.isValid(1)) {
            if(username.isNullOrBlank()) error = "Enter your username"
            if(password.isNullOrBlank()) error = "Enter your password"
            return@launch
        }

        if(error.isNotBlank()) {
            mAuthenticationUiState.update {
                it.copy(
                    isLoading = null,
                    error = Throwable(error)
                )
            }
        } else {
            val loginResult = repository.login(LoginRequest(username!!, password!!))
            processLoginResult(context, loginResult, username, password)
        }
    }

    fun loginWithBiometrics(context: Context) = launch {
        mAuthenticationUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "Logging in..."
            )
        }
        val username = sessionManager.getUsername()
        val password = sessionManager.getPassword()
        var error = ""

        if(username.isNullOrBlank()) error = "Enter a username"
        if(password.isNullOrBlank()) error = "Enter password"

        if(error.isNotBlank()) {
            mAuthenticationUiState.update { it.copy(
                isLoading = null,
                error = Throwable(error)
            ) }
        } else {
            val loginResult = repository.login(LoginRequest(username!!, password!!))
            processLoginResult(context, loginResult, username, password)
        }
    }

    fun loginWithPasscode(context: Context) = launch {
        mAuthenticationUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "Logging in..."
            )
        }
        val savedUserPasscode = sharedPrefProvider.getTransactionPin()
        val code = mAuthenticationUiState.value.passcode
        val username = sessionManager.getUsername()
        val password = sessionManager.getPassword()
        var error = ""

        if(username.isNullOrBlank()) error = "Enter a username"
        if(password.isNullOrBlank()) error = "Enter password"
        if(savedUserPasscode.isNullOrBlank() || savedUserPasscode != code) error = "Invalid passcode, try again"

        if(error.isNotBlank()) {
            mAuthenticationUiState.update { it.copy(
                isLoading = null,
                error = Throwable(error)
            ) }
        } else {
            val loginResult = repository.login(LoginRequest(username!!, password!!))
            processLoginResult(context, loginResult, username, password)
        }
    }


    private suspend fun processLoginResult(
         context: Context,
        result: NetworkResult<LoginResponse>,
        username: String,
        password: String
    ) {
        when(result) {
            is NetworkResult.Success -> {
                mAuthenticationUiState.update {
                    it.copy(
                        canGoToDashboard = true,
                        isLoading = null,
                    )
                }
                sessionManager.saveLoggedInState(LoggedInSessionState.LOGGED_IN.name)
                sessionManager.saveToken(result.data.token)
                sessionManager.saveUsername(username)
                sessionManager.savePassword(password)
                preference.setUsername(username)

                RefreshWorker.engage(context)
            }
            is NetworkResult.Failed -> Unit
            is NetworkResult.Error -> {
                mAuthenticationUiState.update { it.copy(
                    isLoading = null,
                    error = Throwable(result.message)
                ) }
            }
        }
    }

    fun register(context: Context, callback: (TransactionStatus) -> Unit) = launch {
        mAuthenticationUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "Creating user account..."
            )
        }
        var error = ""
        val username = mAuthenticationRequest.value.username
        val password = mAuthenticationRequest.value.password
        val confirmPassword = mAuthenticationRequest.value.confirmPassword
        val phone = mAuthenticationRequest.value.phone
        val email = mAuthenticationRequest.value.email
        val fullname = mAuthenticationRequest.value.fullname
        val address = mAuthenticationRequest.value.address
        val referer = mAuthenticationRequest.value.refererUsername

        if (
            !mAuthenticationRequest.value.isLevelOneCompleted ||
            !mAuthenticationRequest.value.isLevelTwoCompleted ||
            !mAuthenticationRequest.value.isLevelThreeCompleted
        ) {
            if(username.isNullOrBlank()) error = "Please enter username"
            if(password.isNullOrBlank()) error = "Please enter password"
            if(confirmPassword.isNullOrBlank()) error = "Please confirm your password"
            if(phone.isNullOrBlank()) error = "Please enter phone"
            if(email.isNullOrBlank()) error = "Please enter email"
        }

        if(error.isNotBlank()) {
            mAuthenticationUiState.update {
                it.copy(
                    isLoading = null,
                    error = Throwable(error)
                )
            }

            return@launch
        }


        val registerResult = repository.register(
            RegisterRequest(
                username = username!!,
                password = password!!,
                confirmPassword = confirmPassword!!,
                phone = phone!!,
                email = email!!,
                fullname = fullname,
                address = address,
                refererUsername = referer.orEmpty(),
            )
        )

        processRegisterResult(context, registerResult, username, password, callback)
    }

    private suspend fun processRegisterResult(
        context: Context,
        result: NetworkResult<RegisterResponse>,
        username: String,
        password: String,
        callback: (TransactionStatus) -> Unit
    ) {
        when(result) {
            is NetworkResult.Success -> {

                preference.setUsername(username)
                sessionManager.savePassword(password)
                repository.login(LoginRequest(username, password)).onSuccess { data ->

                    sessionManager.saveLoggedInState(LoggedInSessionState.LOGGED_IN.name)
                    sessionManager.saveToken(data.token)
                    sessionManager.saveUsername(username)
                    sessionManager.savePassword(password)
                    preference.setUsername(username)

                    RefreshWorker.engage(context)

                    mAuthenticationUiState.update {
                        it.copy(
                            hasCompletedAccountCreation = true,
                            isLoading = null,
                        )
                    }

                    callback(
                        TransactionStatus(
                            header = "Yo!",
                            description = result.data.message.orEmpty(),
                            status = Status.SUCCESSFUL
                        )
                    )
                }
            }
            is NetworkResult.Failed -> Unit
            is NetworkResult.Error -> {

                mAuthenticationUiState.update {
                    it.copy(
                        isLoading = null,
                        error = Throwable(result.message)
                    )
                }
                callback(
                    TransactionStatus(
                        header = "Ouch!",
                        description = result.message,
                        status = Status.FAILED
                    )
                )
            }
        }
    }

    fun forgotPassword(context: Context) = launch {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(BuildConfig.BASE_URL + "/password_reset/")
            context.startActivity(this)
        }
    }

    fun nextPage() = launch {
        val current = mCurrentPage.value
        if(current in 1..< TOTAL_AUTHENTICATION_PAGES) {
            mCurrentPage.value = current + 1
        }
    }

    fun previousPage() = launch {
        val current = mCurrentPage.value
        if(current in 2..TOTAL_AUTHENTICATION_PAGES) {
            mCurrentPage.value = current - 1
        }
    }

    fun resetPage() = launch {
        mCurrentPage.value = 1
    }

    fun updateTermsAndPrivacyPolicyAgreement(agree: Boolean) = launch {
        mAuthenticationRequest.update { it.copy(hasAgreedToTermsAndPrivacyPolicy = agree) }
    }

    fun updateAuthState(authState: AuthState) = launch {
        mAuthenticationRequest.update { it.copy(authState = authState) }
    }

    companion object {
        const val TOTAL_AUTHENTICATION_PAGES = 3
    }

}

@OptIn(ExperimentalCoroutinesApi::class)
private fun currentAuthenticationPage(
    currentPage: StateFlow<Int>
): Flow<Int> {
    return currentPage.mapLatest { it }
}

data class AuthenticationUiState(
    val isLoading: Boolean? = null,
    val loadingMessage: String = "",
    val canGoToDashboard: Boolean? = null,
    val error: Throwable? = null,
    val passcode: String? = null,
    val hasCompletedAccountCreation: Boolean? = null,
)