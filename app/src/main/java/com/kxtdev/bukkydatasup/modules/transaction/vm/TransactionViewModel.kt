package com.kxtdev.bukkydatasup.modules.transaction.vm

import androidx.lifecycle.viewModelScope
import com.kxtdev.bukkydatasup.common.datastore.SharedPrefProvider
import com.kxtdev.bukkydatasup.common.enums.AirtimeType
import com.kxtdev.bukkydatasup.common.enums.CableTV
import com.kxtdev.bukkydatasup.common.enums.CheckoutState
import com.kxtdev.bukkydatasup.common.enums.DataPlanType
import com.kxtdev.bukkydatasup.common.enums.DiscoProvider
import com.kxtdev.bukkydatasup.common.enums.ExamType
import com.kxtdev.bukkydatasup.common.enums.MeterType
import com.kxtdev.bukkydatasup.common.enums.Network
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.common.enums.Status
import com.kxtdev.bukkydatasup.common.enums.UserType
import com.kxtdev.bukkydatasup.common.models.BuyDataRequest
import com.kxtdev.bukkydatasup.common.models.BuyDataResponse
import com.kxtdev.bukkydatasup.common.models.CablePlanItem
import com.kxtdev.bukkydatasup.common.models.CablePlansResponse
import com.kxtdev.bukkydatasup.common.models.DataPlanItem
import com.kxtdev.bukkydatasup.common.models.DataPlans
import com.kxtdev.bukkydatasup.common.models.HistoryDetailItem
import com.kxtdev.bukkydatasup.common.models.LoggedInUserState
import com.kxtdev.bukkydatasup.common.models.PinResponse
import com.kxtdev.bukkydatasup.common.models.PrintCardRequest
import com.kxtdev.bukkydatasup.common.models.PrintCardResponse
import com.kxtdev.bukkydatasup.common.models.RechargeAirtimeRequest
import com.kxtdev.bukkydatasup.common.models.RechargeAirtimeResponse
import com.kxtdev.bukkydatasup.common.models.ResultCheckerRequest
import com.kxtdev.bukkydatasup.common.models.ResultCheckerResponse
import com.kxtdev.bukkydatasup.common.models.SendBulkSMSRequest
import com.kxtdev.bukkydatasup.common.models.SendBulkSMSResponse
import com.kxtdev.bukkydatasup.common.models.SubscribeBillRequest
import com.kxtdev.bukkydatasup.common.models.SubscribeBillResponse
import com.kxtdev.bukkydatasup.common.models.SubscribeCableTVRequest
import com.kxtdev.bukkydatasup.common.models.SubscribeCableTVResponse
import com.kxtdev.bukkydatasup.common.models.TopUpPercentageResponse
import com.kxtdev.bukkydatasup.common.models.TransactionRequest
import com.kxtdev.bukkydatasup.common.models.TransactionStatus
import com.kxtdev.bukkydatasup.common.models.TransferFundsRequest
import com.kxtdev.bukkydatasup.common.models.TransferFundsResponse
import com.kxtdev.bukkydatasup.common.models.getPins
import com.kxtdev.bukkydatasup.common.models.retrieveDataPlanTypes
import com.kxtdev.bukkydatasup.common.utils.BaseViewModel
import com.kxtdev.bukkydatasup.common.utils.asMoney
import com.kxtdev.bukkydatasup.domain.repository.AppRepository
import com.kxtdev.bukkydatasup.navigation.AppNavigation
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

val TAG = TransactionViewModel::class.java.simpleName

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: AppRepository,
    private val sharedPrefProvider: SharedPrefProvider,
) : BaseViewModel() {
    private val mTransactionUiState = MutableStateFlow<TransactionUiState>(TransactionUiState())
    val transactionUiState: StateFlow<TransactionUiState> = mTransactionUiState.asStateFlow()

    private val mTransactionRequest = MutableStateFlow<TransactionRequest>(TransactionRequest())
    val transactionRequest: StateFlow<TransactionRequest> = mTransactionRequest.asStateFlow()

    private val mNetwork = MutableStateFlow<Network>(Network.MTN)
    private val mDataPlanType = MutableStateFlow<DataPlanType>(DataPlanType.CG)
    private val mCableTv = MutableStateFlow<CableTV>(CableTV.GOTV)

    private val mRawDataPlans = MutableStateFlow<DataPlans>(DataPlans())
    private val mRawCablePlans = MutableStateFlow<CablePlansResponse>(CablePlansResponse())

    private val mFromDestinationRoute = MutableStateFlow<String>(AppNavigation.HomeScreen.route)
    val fromDestinationRoute: StateFlow<String> = mFromDestinationRoute.asStateFlow()

    val dataPlansCached: StateFlow<List<DataPlanItem>> = getDataPlansFlowCached(
        mTransactionRequest, repository
    )
        .stateIn(
            scope = viewModelScope,
            initialValue = listOf(),
            started = SharingStarted.WhileSubscribed(5_000)
        )


    val dataPlanTypesCached: StateFlow<List<DataPlanType>> = getDataPlanTypesFlowCached(
        mTransactionRequest, repository
    )
        .stateIn(
            scope = viewModelScope,
            initialValue = listOf(),
            started = SharingStarted.WhileSubscribed(5_000)
        )


    val cablePlansCached: StateFlow<List<CablePlanItem>> = getCablePlansFlowCached(
        mTransactionRequest, repository
    )
        .stateIn(
            scope = viewModelScope,
            initialValue = listOf(),
            started = SharingStarted.WhileSubscribed(5_000)
        )


    fun airtimeSwapTotalAmount(loggedInUserState: LoggedInUserState): StateFlow<Double> {
        return getAirtimeSwapTotalAmount(
            MutableStateFlow(loggedInUserState),
            mTransactionRequest
        )
            .stateIn(
                scope = viewModelScope,
                initialValue = 0.0,
                started = SharingStarted.WhileSubscribed(5_000)
            )
    }


    fun airtimeTotalAmount(loggedInUserState: LoggedInUserState): StateFlow<Double> {
        return getAirtimeTotalAmount(
            MutableStateFlow(loggedInUserState),
            mTransactionRequest
        )
            .stateIn(
                scope = viewModelScope,
                initialValue = 0.0,
                started = SharingStarted.WhileSubscribed(5_000)
            )
    }

    fun resultCheckerTotalAmount(loggedInUserState: LoggedInUserState): StateFlow<Double> {
        return getResultCheckerTotalAmount(
            MutableStateFlow(loggedInUserState),
            mTransactionRequest
        )
            .stateIn(
                scope = viewModelScope,
                initialValue = 0.0,
                started = SharingStarted.WhileSubscribed(5_000)
            )
    }

    fun computedPinResponses(loggedInUserState: LoggedInUserState): StateFlow<List<PinResponse>> {
        return getComputedPinResponses(
            MutableStateFlow(loggedInUserState),
            mTransactionRequest
        )
            .stateIn(
                scope = viewModelScope,
                initialValue = listOf(),
                started = SharingStarted.WhileSubscribed(5_000)
            )
    }

    val cardPrintingTotalAmount: StateFlow<Double> = getCardPrintingTotalAmount(mTransactionRequest)
        .stateIn(
            scope = viewModelScope,
            initialValue = 0.0,
            started = SharingStarted.WhileSubscribed(5_000)
        )

    val cablePlansFlow: StateFlow<List<CablePlanItem>> = cablePlansFlow(
        cable = mCableTv,
        rawCablePlans = mRawCablePlans
    )
        .stateIn(
            scope = viewModelScope,
            initialValue = listOf(),
            started = SharingStarted.WhileSubscribed(5_000)
        )


    val dataPlanTypesFlow: StateFlow<List<DataPlanType>> = dataPlanTypesFlow(
        network = mNetwork,
        rawDataPlans = mRawDataPlans
    )
        .stateIn(
            scope = viewModelScope,
            initialValue = listOf(),
            started = SharingStarted.WhileSubscribed(5_000)
        )

    val dataPlansFlow: StateFlow<List<DataPlanItem>> = dataPlansFlow(
        network = mNetwork,
        dataPlanType = mDataPlanType,
        rawDataPlans = mRawDataPlans
    )
        .stateIn(
            scope = viewModelScope,
            initialValue = listOf(),
            started = SharingStarted.WhileSubscribed(5_000)
        )

    private val mGeneratedTags = MutableStateFlow<HashMap<Int, String>>(hashMapOf())
    val generatedTags: StateFlow<HashMap<Int, String>> = mGeneratedTags.asStateFlow()

    private val mTagKey = MutableStateFlow<Int>(1)
    val tagKey: StateFlow<Int> = mTagKey.asStateFlow()

    fun onChangeDialogState(currentState: Boolean) {
        mTransactionUiState.update {
            it.copy(
                shouldShowDialog = !currentState
            )
        }
    }

    fun updateNetwork(networkId: Int, networkName: String) {
        Network.getById(networkId)?.let { mNetwork.value = it }
        mTransactionRequest.update {
            it.copy(
                networkName = networkName,
                networkId = networkId
            )
        }
    }

    fun updateStatusPageAs(show: Boolean?) {
        mTransactionUiState.update {
            it.copy(
                shouldShowStatusPage = show
            )
        }
    }

    fun updateCablePlans(rawCablePlans: CablePlansResponse) = launch {
        mTransactionUiState.update { it.copy(isLoading = true) }
        delay(500)
        mRawCablePlans.value = rawCablePlans
        mTransactionUiState.update { it.copy(isLoading = null) }
    }

    fun updateDeviceNumber(number: String) {
        mTransactionRequest.update { it.copy(deviceNumber = number) }
    }

    fun calculateTopUpPercentage(
        airtimeType: AirtimeType,
        network: Network,
        topUpPercentageResponse: TopUpPercentageResponse
    ): Double {
        val currentTopUpPercentage: Double = when (network) {
            Network.MTN -> {
                if (airtimeType == AirtimeType.VTU)
                    topUpPercentageResponse.mtn.vtu else topUpPercentageResponse.mtn.shareNSell
            }

            Network.GLO -> {
                if (airtimeType == AirtimeType.VTU)
                    topUpPercentageResponse.glo.vtu else topUpPercentageResponse.glo.shareNSell
            }

            Network.AIRTEL -> {
                if (airtimeType == AirtimeType.VTU)
                    topUpPercentageResponse.airtel.vtu else topUpPercentageResponse.airtel.shareNSell
            }

            Network.NINE_MOBILE -> {
                if (airtimeType == AirtimeType.VTU)
                    topUpPercentageResponse.nineMobile.vtu else topUpPercentageResponse.nineMobile.shareNSell
            }
        }
        return 100 - currentTopUpPercentage
    }

    fun updateTransactionPin(pin: String?) {
        mTransactionUiState.update {
            it.copy(
                transactionPin = pin
            )
        }
    }

    fun updateNetworkAmount(id: Int, amount: String) {
        val amt = amount.toDouble()
        mTransactionRequest.update {
            it.copy(
                networkAmountId = id,
                amount = amt
            )
        }
    }

    fun initTransactionRequest(product: Product) {
        mTransactionRequest.value = TransactionRequest()
        when (product) {
            Product.AIRTIME -> {
                mTransactionRequest.update {
                    it.copy(
                        airtimeType = AirtimeType.VTU
                    )
                }
            }

            Product.DATA -> {
                mTransactionRequest.update {
                    it.copy(
                        dataPlanType = DataPlanType.CG
                    )
                }
            }

            Product.ELECTRICITY -> {
                mTransactionRequest.update {
                    it.copy(
                        meterType = MeterType.PREPAID
                    )
                }
            }

            else -> Unit
        }
        mGeneratedTags.value = hashMapOf()
    }

    fun updateRecipientPhone(phone: String) {
        mTransactionRequest.update {
            it.copy(
                recipientPhone = phone
            )
        }
    }

    fun updateTopUpPercentageDiscount(discount: Double) {
        mTransactionRequest.update {
            it.copy(
                percentageDiscount = discount
            )
        }
    }

    fun updateProvider(providerId: Int, providerName: String, product: Product) {
        mTransactionRequest.update {
            when (product) {
                Product.CABLE -> {
                    CableTV.getById(providerId)?.let { tv -> mCableTv.value = tv }
                    it.copy(
                        cableName = providerName,
                        cableId = providerId,
                    )
                }

                Product.ELECTRICITY -> {
                    it.copy(
                        discoId = providerId,
                        discoName = providerName
                    )
                }

                Product.RESULT_CHECKER -> {
                    it.copy(
                        examName = providerName
                    )
                }

                Product.PRINT_CARD -> {
                    it.copy(
                        networkId = providerId,
                        networkName = providerName,
                    )
                }

                else -> it
            }
        }
    }


    fun onChangeDialogItem(name: String, product: Product) {
        when (product) {
            Product.CABLE -> {
                mCableTv.value = CableTV.getByTitle(name) ?: CableTV.GOTV
            }

            Product.ELECTRICITY -> {
                val disco = DiscoProvider.getByTitle(name) ?: DiscoProvider.IKEJA_ELECTRIC
                mTransactionRequest.update { it.copy(discoId = disco.id) }
            }

            else -> Unit
        }
        mTransactionRequest.update {
            when (product) {
                Product.AIRTIME, Product.DATA,
                Product.PRINT_CARD -> {
                    it.copy(
                        networkName = name,
                        networkId = Network.getByName(name)?.id
                    )
                }

                Product.CABLE -> {
                    it.copy(cableName = name)
                }

                Product.ELECTRICITY -> {
                    it.copy(discoName = name)
                }

                Product.RESULT_CHECKER -> {
                    it.copy(examName = name)
                }

                else -> it
            }
        }
    }

    fun updateAmount(amount: String, product: Product) {
        try {
            when (product) {
                Product.AIRTIME,
                Product.TRANSFER,
                Product.ELECTRICITY,
                Product.RESULT_CHECKER,
                Product.AIRTIME_SWAP -> {
                    val mAmount = amount.toDouble()
                    mTransactionRequest.update {
                        it.copy(
                            amount = mAmount
                        )
                    }
                }

                Product.PRINT_CARD -> {
                    val quantity = amount.toInt()
                    mTransactionRequest.update {
                        it.copy(
                            quantity = quantity
                        )
                    }
                }
                else -> Unit

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateAirtimeType(type: AirtimeType) {
        mTransactionRequest.update { it.copy(airtimeType = type) }
    }

    fun updateMeterType(type: MeterType) {
        mTransactionRequest.update { it.copy(meterType = type) }
    }

    fun updateCustomerName(name: String) {
        mTransactionRequest.update { it.copy(customerName = name) }
    }

    fun updateCustomerAddress(address: String) {
        mTransactionRequest.update { it.copy(address = address) }
    }

    fun updateDataPlanType(planType: DataPlanType) {
        mDataPlanType.value = planType
        mTransactionRequest.update { it.copy(dataPlanType = planType) }
    }

    fun updatePlan(
        planId: Int,
        planSize: String,
        planAmount: Double,
        planValidity: String,
    ) {
        mTransactionRequest.update {
            it.copy(
                planId = planId,
                planSize = planSize,
                amount = planAmount,
                planValidity = planValidity
            )
        }
    }

    fun updateUsername(n: String) {
        mTransactionRequest.update { it.copy(customerName = n) }
    }

    fun updateNameOnCard(name: String) {
        mTransactionRequest.update { it.copy(nameOnCard = name) }
    }

    fun updateShouldShowTransactionReceipt(show: Boolean?) {
        mTransactionUiState.update {
            it.copy(
                shouldShowTransactionReceipt = show
            )
        }
    }

    fun onChangePinVerificationScreenState(currentState: Boolean?) {
        val state = currentState ?: false
        mTransactionUiState.update {
            it.copy(
                shouldStartPinVerification = !state
            )
        }
    }

    fun verifyPin(pin: String, onSuccess: () -> Unit, onFailed: () -> Unit) {
        val userPin = sharedPrefProvider.getTransactionPin()
        if (userPin.isNullOrBlank() || userPin != pin) return onFailed.invoke()
        mTransactionUiState.update { it.copy(transactionPinVerified = true) }
        onSuccess.invoke()
    }

    fun onVerificationFailed() {
        mTransactionUiState.update {
            it.copy(
                error = Throwable("Pin verification failed.")
            )
        }
    }

    fun updateQuantity(quantity: String) {
        try {
            mTransactionRequest.update { it.copy(quantity = if (quantity.isBlank()) 0 else quantity.toInt()) }
        } catch (e: Exception) {
            mTransactionRequest.update { it.copy(quantity = 0) }
        }
    }

    override fun onHandledThrowable() {
        mTransactionUiState.update {
            it.copy(error = null)
        }
    }

    fun buyData(callback: (TransactionStatus) -> Unit) = launch(Dispatchers.IO) {
        mTransactionUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "Subscribing data..."
            )
        }
        val request = mTransactionRequest.value

        var errorMessage = ""
        if (request.dataPlanType == null) errorMessage = "Select a plan type"
        if (request.planId == null) errorMessage = "Select a plan"
        if (request.networkId == null) errorMessage = "Select a network"
        if (request.recipientPhone.isNullOrBlank()) errorMessage = "Enter a valid phone"

        if (errorMessage.isNotBlank()) {
            mTransactionUiState.update {
                it.copy(isLoading = null, error = Throwable(errorMessage))
            }
            return@launch
        }
        val result = repository.buyData(
            BuyDataRequest(
                network = request.networkId!!,
                phone = request.recipientPhone!!,
                plan = request.planId!!,
                isPorted = true,
            )
        )
        processDataPurchase(result, callback)
    }

    private fun processDataPurchase(
        result: NetworkResult<BuyDataResponse>,
        callback: (TransactionStatus) -> Unit
    ) {
        result
            .onSuccess { data ->
                if (data.status.lowercase() == Status.SUCCESSFUL.title.lowercase()) {
                    callback(
                        TransactionStatus(
                            status = Status.SUCCESSFUL,
                            header = "Here you go!",
                            description = data.apiResponse,
                            itemId = data.id,
                        )
                    )
                } else if (data.status.lowercase() == Status.PROCESSING.title.lowercase()) {
                    callback(
                        TransactionStatus(
                            status = Status.PROCESSING,
                            header = "Hmmmm!",
                            description = data.apiResponse,
                            itemId = data.id,
                        )
                    )
                } else {
                    callback(
                        TransactionStatus(
                            status = Status.FAILED,
                            header = "Oh sorry!",
                            description = data.apiResponse,
                            itemId = data.id,
                        )
                    )
                }
                mTransactionUiState.update {
                    it.copy(
                        isLoading = null,
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                        apiResponse = data.apiResponse,
                        shouldShowTransactionReceipt = true,
                        transactionItemId = data.id,
                    )
                }
            }
            .onError { msg ->
                val error = Throwable(msg)
                callback(
                    TransactionStatus(
                        status = Status.FAILED,
                        header = "Oops!",
                        description = error.message.orEmpty(),
                    )
                )
                mTransactionUiState.update {
                    it.copy(
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        isLoading = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                    )
                }
            }
    }

    fun subscribeCable(callback: (TransactionStatus) -> Unit) = launch(Dispatchers.IO) {
        mTransactionUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "Subscribing cable TV..."
            )
        }
        val request = mTransactionRequest.value
        var errorMessage = ""
        if (request.cableId == null) errorMessage = "Select a cable TV"
        if (request.deviceNumber == null) errorMessage = "Enter SmartCard number"
        if (request.planId == null) errorMessage = "Select a plan"

        if (errorMessage.isNotBlank()) {
            mTransactionUiState.update {
                it.copy(isLoading = null, error = Throwable(errorMessage))
            }
            return@launch
        }

        val result = repository.subscribeCable(
            SubscribeCableTVRequest(
                cableId = request.cableId!!,
                planId = request.planId!!,
                smartCardNumber = request.deviceNumber!!,
            )
        )

        processCableTvSubscription(result, callback)
    }

    private fun processCableTvSubscription(
        result: NetworkResult<SubscribeCableTVResponse>,
        callback: (TransactionStatus) -> Unit
    ) {
        result
            .onSuccess { data ->
                val apiResponse: String
                if (data.status.lowercase() == Status.SUCCESSFUL.title.lowercase()) {
                    apiResponse = """
                            You have successfully recharged N${data.planAmount.asMoney()}
                              ${data.packageName} on ${data.smartCardNumber} decoder. 
                       """.trimIndent()
                    callback(
                        TransactionStatus(
                            status = Status.SUCCESSFUL,
                            header = "Here you go!",
                            description = apiResponse,
                            itemId = data.id,
                        )
                    )
                } else if (data.status.lowercase() == Status.PROCESSING.title.lowercase()) {
                    apiResponse = "Please wait..."
                    callback(
                        TransactionStatus(
                            status = Status.PROCESSING,
                            header = "Hmmmm!",
                            description = apiResponse,
                            itemId = data.id,
                        )
                    )
                } else {
                    apiResponse = "Sorry, couldn't complete transaction"
                    callback(
                        TransactionStatus(
                            status = Status.FAILED,
                            header = "Oh sorry!",
                            description = apiResponse,
                            itemId = data.id,
                        )
                    )
                }
                mTransactionUiState.update {
                    it.copy(
                        isLoading = null,
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                        apiResponse = apiResponse,
                        shouldShowTransactionReceipt = true,
                        transactionItemId = data.id,
                    )
                }
            }
            .onError { msg ->
                val error = Throwable(msg)
                callback(
                    TransactionStatus(
                        status = Status.FAILED,
                        header = "Oops!",
                        description = error.message.orEmpty(),
                    )
                )
                mTransactionUiState.update {
                    it.copy(
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        isLoading = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                    )
                }
            }
    }

    fun validateSmartCardNumber(number: String, cableId: Int) = launch(Dispatchers.IO) {
        mTransactionUiState.update { it.copy(isLoading = true) }
        val result = repository.validateSmartCardNumber(cableId, number)
        result
            .onSuccess { data ->
                if (data.name.lowercase().contains("invalid")) {
                    mTransactionUiState.update {
                        it.copy(
                            isLoading = null,
                            error = Throwable(data.name)
                        )
                    }
                } else {
                    mTransactionUiState.update { it.copy(isLoading = null) }
                    mTransactionRequest.update { it.copy(customerName = data.name) }
                }
            }
            .onError { msg ->
                mTransactionUiState.update {
                    it.copy(
                        isLoading = null,
                        error = Throwable(msg)
                    )
                }
            }
    }

    fun topUpAirtime(callback: (TransactionStatus) -> Unit) = launch(Dispatchers.IO) {
        mTransactionUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "Recharging airtime..."
            )
        }
        val request = mTransactionRequest.value

        var errorMessage = ""
        if (request.amount == null) errorMessage = "Enter a valid amount"
        if (request.networkId == null) errorMessage = "Select a network"
        if (request.recipientPhone.isNullOrBlank()) errorMessage = "Enter a valid phone"

        if (errorMessage.isNotBlank()) {
            mTransactionUiState.update {
                it.copy(isLoading = null, error = Throwable(errorMessage))
            }
            return@launch
        }

        val result = repository.rechargeAirtime(
            RechargeAirtimeRequest(
                network = request.networkId!!,
                phone = request.recipientPhone!!,
                amount = request.amount!!,
                airtimeType = request.airtimeType?.title ?: AirtimeType.VTU.title,
                isPorted = true,
            )
        )
        processAirtimeRecharge(result, callback)
    }

    private fun processAirtimeRecharge(
        result: NetworkResult<RechargeAirtimeResponse>,
        callback: (TransactionStatus) -> Unit
    ) {
        result
            .onSuccess { data ->
                val apiResponse: String

                if (data.status.lowercase() == Status.SUCCESSFUL.title.lowercase()) {
                    apiResponse = """
                            You have successfully recharged ${data.planAmount} ${data.planNetwork}
                            airtime to ${data.phone} 
                       """.trimIndent()
                    callback(
                        TransactionStatus(
                            status = Status.SUCCESSFUL,
                            header = "Here you go!",
                            description = apiResponse,
                            itemId = data.id,
                        )
                    )
                } else if (data.status.lowercase() == Status.PROCESSING.title.lowercase()) {
                    apiResponse = "Transaction pending response"
                    callback(
                        TransactionStatus(
                            status = Status.PROCESSING,
                            header = "Hmmmm!",
                            description = apiResponse,
                            itemId = data.id,
                        )
                    )
                } else {
                    apiResponse = "Could not complete transaction"
                    callback(
                        TransactionStatus(
                            status = Status.FAILED,
                            header = "Oh sorry!",
                            description = apiResponse,
                            itemId = data.id,
                        )
                    )
                }
                mTransactionUiState.update {
                    it.copy(
                        isLoading = null,
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                        apiResponse = apiResponse,
                        shouldShowTransactionReceipt = true,
                        transactionItemId = data.id,
                    )
                }
            }
            .onError { msg ->
                val error = Throwable(msg)
                callback(
                    TransactionStatus(
                        status = Status.FAILED,
                        header = "Oops!",
                        description = error.message.orEmpty(),
                    )
                )
                mTransactionUiState.update {
                    it.copy(
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        isLoading = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                    )
                }
            }
    }

    fun transferFunds(callback: (TransactionStatus) -> Unit) = launch(Dispatchers.IO) {
        mTransactionUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "Moving funds..."
            )
        }
        val request = mTransactionRequest.value

        var errorMessage = ""
        if (request.amount == null) errorMessage = "Enter a valid amount"
        if (request.customerName == null) errorMessage = "Enter a username"

        if (errorMessage.isNotBlank()) {
            mTransactionUiState.update {
                it.copy(isLoading = null, error = Throwable(errorMessage))
            }
            return@launch
        }

        val response = repository.transferFunds(
            TransferFundsRequest(
                amount = request.amount!!,
                recipientUsername = request.customerName!!,
            )
        )
        processTransferFundsResult(response, callback)
    }

    private fun processTransferFundsResult(
        result: NetworkResult<TransferFundsResponse>,
        callback: (TransactionStatus) -> Unit
    ) {
        result
            .onSuccess { data ->
                val apiResponse: String

                if (data.status.lowercase() == Status.SUCCESSFUL.title.lowercase()) {
                    apiResponse = """
                            You have successfully moved N${data.amount.toString().asMoney()} to
                            ${data.recipientUsername} 
                       """.trimIndent()
                    callback(
                        TransactionStatus(
                            status = Status.SUCCESSFUL,
                            header = "Here you go!",
                            description = apiResponse,
                        )
                    )
                } else if (data.status.lowercase() == Status.PROCESSING.title.lowercase()) {
                    apiResponse = "Transaction pending response"
                    callback(
                        TransactionStatus(
                            status = Status.PROCESSING,
                            header = "Hmmmm!",
                            description = apiResponse,
                        )
                    )
                } else {
                    apiResponse = "Could not complete transaction"
                    callback(
                        TransactionStatus(
                            status = Status.FAILED,
                            header = "Oh sorry!",
                            description = apiResponse
                        )
                    )
                }
                mTransactionUiState.update {
                    it.copy(
                        isLoading = null,
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                        apiResponse = apiResponse,
                    )
                }
            }
            .onError { msg ->
                val error = Throwable(msg)
                callback(
                    TransactionStatus(
                        status = Status.FAILED,
                        header = "Oops!",
                        description = error.message.orEmpty(),
                    )
                )
                mTransactionUiState.update {
                    it.copy(
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        isLoading = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                    )
                }
            }
    }

    fun validateMeter(
        discoId: Int,
        meterNumber: String,
        meterType: String,
        customerPhone: String
    ) = launch(Dispatchers.IO) {
        mTransactionUiState.update { it.copy(isLoading = true) }
        val result = repository.validateMeter(discoId, meterNumber, meterType)
        result
            .onSuccess { data ->
                if (data.isInvalid) {
                    mTransactionUiState.update {
                        it.copy(
                            isLoading = null,
                            error = Throwable(data.name)
                        )
                    }
                } else {
                    mTransactionUiState.update { it.copy(isLoading = null) }
                    mTransactionRequest.update {
                        it.copy(
                            customerName = data.name,
                            address = data.address,
                            recipientPhone = customerPhone,
                        )
                    }
                }
            }
            .onError { msg ->
                mTransactionUiState.update {
                    it.copy(
                        isLoading = null,
                        error = Throwable(msg)
                    )
                }
            }
    }

    fun subscribeBill(callback: (TransactionStatus) -> Unit) = launch(Dispatchers.IO) {
        mTransactionUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "Recharging meter..."
            )
        }
        val request = mTransactionRequest.value

        var errorMessage = ""
        if (request.amount == null) errorMessage = "Enter a valid amount"
        if (request.deviceNumber.isNullOrBlank()) errorMessage = "Enter meter number"
        if (request.discoId == null) errorMessage = "Select a disco"
        if (request.meterType == null) errorMessage = "Select a meter type"
        if (request.recipientPhone.isNullOrBlank()) errorMessage = "Enter recipient phone"
        if (request.customerName.isNullOrBlank()) errorMessage = "Enter customer name"
        if (request.address.isNullOrBlank()) errorMessage = "Enter customer address"

        if (errorMessage.isNotBlank()) {
            mTransactionUiState.update {
                it.copy(isLoading = null, error = Throwable(errorMessage))
            }
            return@launch
        }

        val response = repository.subscribeBill(
            SubscribeBillRequest(
                request.discoId!!,
                request.amount!!,
                request.deviceNumber!!,
                request.meterType?.title!!,
                request.recipientPhone!!,
                request.customerName!!,
                request.address!!
            )
        )
        processBillResult(response, callback)
    }

    private fun processBillResult(
        result: NetworkResult<SubscribeBillResponse>,
        callback: (TransactionStatus) -> Unit
    ) {
        result
            .onSuccess { data ->
                val apiResponse: String

                if (data.status.lowercase() == Status.SUCCESSFUL.title.lowercase()) {
                    apiResponse = """
                            You have successfully recharged N${data.amount.toString().asMoney()} on
                            ${data.meterNumber} meter 
                       """.trimIndent()
                    callback(
                        TransactionStatus(
                            status = Status.SUCCESSFUL,
                            header = "Here you go!",
                            description = apiResponse,
                            itemId = data.id,
                        )
                    )
                } else if (data.status.lowercase() == Status.PROCESSING.title.lowercase()) {
                    apiResponse = "Transaction pending response"
                    callback(
                        TransactionStatus(
                            status = Status.PROCESSING,
                            header = "Hmmmm!",
                            description = apiResponse,
                            itemId = data.id,
                        )
                    )
                } else {
                    apiResponse = "Could not complete transaction"
                    callback(
                        TransactionStatus(
                            status = Status.FAILED,
                            header = "Oh sorry!",
                            description = apiResponse,
                            itemId = data.id,
                        )
                    )
                }
                mTransactionUiState.update {
                    it.copy(
                        isLoading = null,
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                        apiResponse = apiResponse,
                        shouldShowTransactionReceipt = true,
                        transactionItemId = data.id,
                    )
                }
            }
            .onError { msg ->
                val error = Throwable(msg)
                callback(
                    TransactionStatus(
                        status = Status.FAILED,
                        header = "Oops!",
                        description = error.message.orEmpty(),
                    )
                )
                mTransactionUiState.update {
                    it.copy(
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        isLoading = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                    )
                }
            }
    }

    fun checkResult(callback: (TransactionStatus) -> Unit) = launch(Dispatchers.IO) {
        mTransactionUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "Generating result pin..."
            )
        }
        val request = mTransactionRequest.value

        var errorMessage = ""
        if (request.examName.isNullOrBlank()) errorMessage = "Enter exam name"
        if (request.quantity == null) errorMessage = "Specify quantity"

        if (errorMessage.isNotBlank()) {
            mTransactionUiState.update {
                it.copy(isLoading = null, error = Throwable(errorMessage))
            }
            return@launch
        }

        val response = repository.checkResult(
            ResultCheckerRequest(
                request.examName!!,
                request.quantity!!
            )
        )
        processResultCheckerResult(response, callback)
    }


    private fun processResultCheckerResult(
        result: NetworkResult<ResultCheckerResponse>,
        callback: (TransactionStatus) -> Unit
    ) {
        result
            .onSuccess { data ->
                val apiResponse: String

                if (data.status.lowercase() == Status.SUCCESSFUL.title.lowercase()) {
                    apiResponse = """
                            Result pin generation for ${data.examName} successful
                       """.trimIndent()
                    callback(
                        TransactionStatus(
                            status = Status.SUCCESSFUL,
                            header = "Here you go!",
                            description = apiResponse,
                            itemId = data.id,
                        )
                    )
                } else if (data.status.lowercase() == Status.PROCESSING.title.lowercase()) {
                    apiResponse = "Transaction pending response"
                    callback(
                        TransactionStatus(
                            status = Status.PROCESSING,
                            header = "Hmmmm!",
                            description = apiResponse,
                            itemId = data.id,
                        )
                    )
                } else {
                    apiResponse = "Could not complete transaction"
                    callback(
                        TransactionStatus(
                            status = Status.FAILED,
                            header = "Oh sorry!",
                            description = apiResponse,
                            itemId = data.id,
                        )
                    )
                }
                mTransactionUiState.update {
                    it.copy(
                        isLoading = null,
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                        apiResponse = apiResponse,
                        shouldShowTransactionReceipt = true,
                        transactionItemId = data.id,
                    )
                }
            }
            .onError { msg ->
                val error = Throwable(msg)
                callback(
                    TransactionStatus(
                        status = Status.FAILED,
                        header = "Oops!",
                        description = error.message.orEmpty(),
                    )
                )
                mTransactionUiState.update {
                    it.copy(
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        isLoading = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                    )
                }
            }
    }


    fun printCard(callback: (TransactionStatus) -> Unit) = launch(Dispatchers.IO) {
        mTransactionUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "Generating airtime pin..."
            )
        }
        val request = mTransactionRequest.value

        var errorMessage = ""
        if (request.networkId == null) errorMessage = "Enter network Id"
        val networkAmountId = request.pinResponse?.id
        if (networkAmountId == null) errorMessage = "Enter network Amount"
        if (request.quantity == null) errorMessage = "Enter quantity"
        if (request.nameOnCard.isNullOrBlank()) errorMessage = "Enter name on Card"


        if (errorMessage.isNotBlank()) {
            mTransactionUiState.update {
                it.copy(isLoading = null, error = Throwable(errorMessage))
            }
            return@launch
        }

        val response = repository.printCard(
            PrintCardRequest(
                request.networkId!!,
                networkAmountId!!,
                request.quantity!!,
                request.nameOnCard!!
            )
        )
        processPrintCardResult(response, callback)
    }


    private fun processPrintCardResult(
        result: NetworkResult<PrintCardResponse>,
        callback: (TransactionStatus) -> Unit
    ) {
        result
            .onSuccess { data ->
                val apiResponse: String

                if (data.status?.lowercase() == Status.SUCCESSFUL.title.lowercase()) {
                    apiResponse = """
                           Airtime Recharge Pins generated successfully!
                           ${data.dataPins?.getPins()?.joinToString(",")}
                       """.trimIndent()
                    callback(
                        TransactionStatus(
                            status = Status.SUCCESSFUL,
                            header = "Here you go!",
                            description = apiResponse,
                            itemId = null,
                        )
                    )
                } else if (data.status?.lowercase() == Status.PROCESSING.title.lowercase()) {
                    apiResponse = "Transaction pending response"
                    callback(
                        TransactionStatus(
                            status = Status.PROCESSING,
                            header = "Hmmmm!",
                            description = apiResponse,
                            itemId = null,
                        )
                    )
                } else {
                    apiResponse = "Could not complete transaction"
                    callback(
                        TransactionStatus(
                            status = Status.FAILED,
                            header = "Oh sorry!",
                            description = apiResponse,
                            itemId = null,
                        )
                    )
                }
                mTransactionUiState.update {
                    it.copy(
                        isLoading = null,
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                        apiResponse = apiResponse,
                    )
                }
            }
            .onError { msg ->
                val error = Throwable(msg)
                callback(
                    TransactionStatus(
                        status = Status.FAILED,
                        header = "Oops!",
                        description = error.message.orEmpty(),
                    )
                )
                mTransactionUiState.update {
                    it.copy(
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        isLoading = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                    )
                }
            }
    }

    fun updateTransactionProduct(product: Product?) {
        mTransactionRequest.update { it.copy(product = product) }
    }

    fun updateMessage(message: String) {
        mTransactionRequest.update { it.copy(message = message) }
    }

    fun addToTags(key: Int, value: String) {
        mGeneratedTags.value[key] = value
        mTagKey.value = key + 1
        syncTagsAsRecipientPhone(mGeneratedTags.value)
    }

    fun removeFromTags(key: Int) {
        mGeneratedTags.value.remove(key)
        syncTagsAsRecipientPhone(mGeneratedTags.value)
    }

    private fun syncTagsAsRecipientPhone(tags: HashMap<Int, String>) {
        var t = mutableListOf<String>()
        tags.keys.forEach { k -> t += tags[k].orEmpty() }
        t = t.filterNot { it.isBlank() }.toMutableList()
        mTransactionRequest.update { it.copy(recipientPhone = t.joinToString(",")) }
    }

    fun updatePinResponse(pinResponse: PinResponse) {
        mTransactionRequest.update { it.copy(pinResponse = pinResponse) }
    }

    fun updateCheckoutState(checkoutState: CheckoutState?) {
        mTransactionUiState.update { it.copy(checkoutState = checkoutState) }
    }

    fun sendBulkSMS(callback: (TransactionStatus) -> Unit) = launch(Dispatchers.IO) {
        mTransactionUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = "Sending messages..."
            )
        }
        val request = mTransactionRequest.value
        var errorMessage = ""

        if(request.recipientPhone.isNullOrBlank()) errorMessage = "Enter atleast one recipient phone"
        if(request.customerName.isNullOrBlank()) errorMessage = "Enter sender name"
        if(request.message.isNullOrBlank()) errorMessage = "Enter message body"

        if(errorMessage.isNotBlank()) {
            mTransactionUiState.update {
                it.copy(isLoading = null, error = Throwable(errorMessage))
            }
            return@launch
        }

        val response = repository.sendBulkSMS(
            SendBulkSMSRequest(
                recipient = request.recipientPhone!!,
                message = request.message!!,
                sender = request.customerName!!,
                dnd = false
            )
        )

        processBulkSMSResult(response, callback)

    }

    private fun processBulkSMSResult(
        result: NetworkResult<SendBulkSMSResponse>,
        callback: (TransactionStatus) -> Unit
    ) {
        when(result) {
            is NetworkResult.Success -> {
                callback(
                    TransactionStatus(
                        header = "Here you go!",
                        description = result.data.message,
                        status = Status.SUCCESSFUL,
                    )
                )
                mTransactionUiState.update {
                    it.copy(
                        isLoading = null,
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                        apiResponse = result.data.message,
                    )
                }

            }
            is NetworkResult.Failed -> Unit
            is NetworkResult.Error -> {
                callback(
                    TransactionStatus(
                        header = "Oops!",
                        description = result.message,
                        status = Status.SUCCESSFUL,
                    )
                )
                mTransactionUiState.update {
                    it.copy(
                        isLoading = null,
                        shouldStartPinVerification = null,
                        transactionPin = null,
                        transactionPinVerified = null,
                        shouldShowStatusPage = true,
                    )
                }

            }
        }
    }

    fun updateUserType(userType: UserType) {
        mTransactionUiState.update { it.copy(userType = userType) }
    }

    fun getDataTransactionReceipt(id: Int) = launch(Dispatchers.IO) {

        mTransactionUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = RECEIPT_LOADING_MESSAGE
            )
        }
        mTransactionUiState.update {
            it.copy(
                transactionReceiptItem = repository.getDataReceipt(id),
                isLoading = false,
                loadingMessage = "",
            )
        }
    }

    fun getAirtimeTransactionReceipt(id: Int) = launch(Dispatchers.IO) {
        mTransactionUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = RECEIPT_LOADING_MESSAGE
            )
        }
        mTransactionUiState.update {
            it.copy(
                transactionReceiptItem = repository.getAirtimeReceipt(id),
                isLoading = false,
                loadingMessage = "",
            )
        }
    }

    fun getCableReceipt(id: Int) = launch(Dispatchers.IO) {
        mTransactionUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = RECEIPT_LOADING_MESSAGE
            )
        }
        mTransactionUiState.update {
            it.copy(
                transactionReceiptItem = repository.getCableReceipt(id),
                isLoading = false,
                loadingMessage = "",
            )
        }
    }

    fun getBillReceipt(id: Int) = launch(Dispatchers.IO) {
        mTransactionUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = RECEIPT_LOADING_MESSAGE
            )
        }
        mTransactionUiState.update {
            it.copy(
                transactionReceiptItem = repository.getBillReceipt(id),
                isLoading = false,
                loadingMessage = "",
            )
        }
    }

    fun getResultCheckerReceipt(id: Int) = launch(Dispatchers.IO) {
        mTransactionUiState.update {
            it.copy(
                isLoading = true,
                loadingMessage = RECEIPT_LOADING_MESSAGE
            )
        }
        mTransactionUiState.update {
            it.copy(
                transactionReceiptItem = repository.getResultCheckerReceipt(id),
                isLoading = false,
                loadingMessage = "",
            )
        }
    }

    fun updateFromDestination(route: String) {
        mFromDestinationRoute.value = route
    }

    companion object {
        const val RECEIPT_LOADING_MESSAGE = "generating receipt..."
    }

}


private fun dataPlansFlow(
    network: StateFlow<Network>,
    dataPlanType: StateFlow<DataPlanType>,
    rawDataPlans: StateFlow<DataPlans>,
): Flow<List<DataPlanItem>> {
    return combine(
        network, dataPlanType, rawDataPlans,
    ) { net, type, raw ->
        raw.retrieveDataPlans(net, type)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun getDataPlansFlowCached(
    transactionRequest: StateFlow<TransactionRequest>,
    repository: AppRepository
): Flow<List<DataPlanItem>> {
    return transactionRequest.flatMapLatest { request ->
        val dataPlanType = request.selectedDataPlanType
        val network = request.getSelectedNetwork() ?: Network.MTN

        repository.getDataPlans(network.id, dataPlanType.title)
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
private fun getDataPlanTypesFlowCached(
    transactionRequest: StateFlow<TransactionRequest>,
    repository: AppRepository
): Flow<List<DataPlanType>> {
    return transactionRequest.flatMapLatest { request ->
        val network = request.getSelectedNetwork() ?: Network.MTN
        repository.getDataPlans(network.id).map { plans ->
            plans.retrieveDataPlanTypes(network)
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun getCablePlansFlowCached(
    transactionRequest: StateFlow<TransactionRequest>,
    repository: AppRepository
): Flow<List<CablePlanItem>> {
    return transactionRequest.flatMapLatest { request ->
        val cableName = request.selectedProviderName.orEmpty()

        repository.getCablePlans(cableName)
    }
}

private fun dataPlanTypesFlow(
    network: StateFlow<Network>,
    rawDataPlans: StateFlow<DataPlans>,
): Flow<List<DataPlanType>> {
    return combine(
        network,
        rawDataPlans
    ) { net, raw ->
        raw.retrieveDataPlanTypes(net)
    }
}

private fun cablePlansFlow(
    cable: StateFlow<CableTV>,
    rawCablePlans: StateFlow<CablePlansResponse>
): Flow<List<CablePlanItem>> {
    return combine(
        cable,
        rawCablePlans
    ) { c, raw ->
        raw.retrieveCablePlans(c)
    }
}

private fun getAirtimeTotalAmount(
    loggedInUserState: StateFlow<LoggedInUserState>,
    transactionRequest: StateFlow<TransactionRequest>,
): Flow<Double> {
    return combine(
        loggedInUserState,
        transactionRequest
    ) { user, request ->
        request.getAirtimeTotalAmount(
            user.userConfig.topUpPercentage
                ?.getTopUpPercentage(
                    Network.getById(
                        request.networkId ?: 1
                    ) ?: Network.MTN,
                    request.airtimeType
                        ?: AirtimeType.VTU
                ) ?: 0.0
        )
    }
}

private fun getResultCheckerTotalAmount(
    loggedInUserState: StateFlow<LoggedInUserState>,
    transactionRequest: StateFlow<TransactionRequest>,
): Flow<Double> {
    return combine(
        loggedInUserState,
        transactionRequest
    ) { user, request ->
        val selectedExamType = ExamType.getByTitle(request.examName.orEmpty()) ?: ExamType.NECO
        request.getResultCheckerTotalAmount(
            user.userConfig.examResponse?.getAmountPerUnit(
                selectedExamType
            ) ?: 0.0
        )
    }
}

private fun getComputedPinResponses(
    loggedInUserState: StateFlow<LoggedInUserState>,
    transactionRequest: StateFlow<TransactionRequest>,
): Flow<List<PinResponse>> {
    return combine(
        loggedInUserState,
        transactionRequest
    ) { user, request ->
        val network = Network.getByName(request.networkName.orEmpty()) ?: Network.MTN
        user.userConfig.rechargeResponse?.getRechargePins(
            network
        ) ?: listOf()
    }
}

private fun getCardPrintingTotalAmount(
    transactionRequest: StateFlow<TransactionRequest>,
): Flow<Double> {
    return transactionRequest.map { request ->
        request.quantity?.times((request.pinResponse?.amountToPay ?: 0.0)) ?: 0.0
    }
}

private fun getAirtimeSwapTotalAmount(
    loggedInUserState: StateFlow<LoggedInUserState>,
    transactionRequest: StateFlow<TransactionRequest>,
): Flow<Double> {
    return combine(
        loggedInUserState,
        transactionRequest
    ) { user, request ->
        val swapPercentage = user.getAirtimeSwapPercentage(
            Network.getById(
                request.networkId ?: 1
            ) ?: Network.MTN
        ) ?: 0.0
        request.amount?.times(swapPercentage / 100.0) ?: 0.0
    }
}


data class TransactionUiState(
    val error: Throwable? = null,
    val isLoading: Boolean? = null,
    val loadingMessage: String = "",
    val transactionRequest: String? = null,
    val apiResponse: String? = null,
    val shouldShowDialog: Boolean? = null,
    val shouldStartPinVerification: Boolean? = null,
    val transactionPinVerified: Boolean? = null,
    val transactionPin: String? = null,
    val shouldShowStatusPage: Boolean? = null,
    val shouldShowTransactionReceipt: Boolean? = null,
    val transactionItemId: Int? = null,
    val checkoutState: CheckoutState? = null,
    val userType: UserType? = null,
    val transactionReceiptItem: HistoryDetailItem? = null,
)


