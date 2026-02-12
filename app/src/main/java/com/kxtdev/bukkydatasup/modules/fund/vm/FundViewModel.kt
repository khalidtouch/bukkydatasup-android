package com.kxtdev.bukkydatasup.modules.fund.vm

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kxtdev.bukkydatasup.common.enums.FundOption
import com.kxtdev.bukkydatasup.common.enums.Status
import com.kxtdev.bukkydatasup.common.models.FundWithCouponRequest
import com.kxtdev.bukkydatasup.common.models.FundWithCouponResponse
import com.kxtdev.bukkydatasup.common.models.FundingRequest
import com.kxtdev.bukkydatasup.common.models.InitMonnifyCardPaymentRequest
import com.kxtdev.bukkydatasup.common.models.InitMonnifyCardPaymentResponse
import com.kxtdev.bukkydatasup.common.models.ReferralBonusWithdrawalRequest
import com.kxtdev.bukkydatasup.common.models.ReferralBonusWithdrawalResponse
import com.kxtdev.bukkydatasup.common.models.TransactionStatus
import com.kxtdev.bukkydatasup.common.models.UserBankResponse
import com.kxtdev.bukkydatasup.common.utils.BaseViewModel
import com.kxtdev.bukkydatasup.common.utils.NetworkCheckoutHandler
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.asMoney
import com.kxtdev.bukkydatasup.domain.repository.AppRepository
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FundViewModel @Inject constructor(
    private val repository: AppRepository,
    private val savedStateHandle: SavedStateHandle,
): BaseViewModel() {

    private val mFundingUiState = MutableStateFlow<FundingUiState>(FundingUiState())
    val fundingUiState: StateFlow<FundingUiState> = mFundingUiState.asStateFlow()

    private val mAmount: StateFlow<String> = savedStateHandle.getStateFlow(
        key = Settings.KEY_FUNDING_AMOUNT, initialValue = ""
    )

    private val mCode: StateFlow<String> = savedStateHandle.getStateFlow(
        key = Settings.KEY_FUNDING_CODE, initialValue = ""
    )

    private val mFundingOptionId: StateFlow<Int> = savedStateHandle.getStateFlow(
        key = Settings.KEY_FUNDING_OPTION_ID, initialValue = FundOption.FUND_WITH_COUPON.id
    )

    val fundingRequest: StateFlow<FundingRequest> = getFundingRequest(
        code = mCode, amount = mAmount, fundOptionId = mFundingOptionId
    )
        .stateIn(
            scope = viewModelScope,
            initialValue = FundingRequest(),
            started = SharingStarted.WhileSubscribed(5_000)
        )

    val banksFlowCached: StateFlow<List<UserBankResponse>> = getBanks(repository)
        .stateIn(
            scope = viewModelScope,
            initialValue = listOf(),
            started = SharingStarted.WhileSubscribed(5_000)
        )

    fun updateShowStatusPage(show: Boolean?) {
        mFundingUiState.update { it.copy(shouldShowStatusPage = show) }
    }

    fun updateValue(value: String, option: FundOption?) {
        when(option) {
            FundOption.FUND_WITH_MONNIFY_CARD,
            FundOption.FUND_FROM_BONUS -> {
                savedStateHandle[Settings.KEY_FUNDING_AMOUNT] = value
            }
            FundOption.FUND_WITH_COUPON -> {
                savedStateHandle[Settings.KEY_FUNDING_CODE] = value
            }
            else -> Unit
        }
    }

    fun updateFundOption(option: FundOption?) {
        savedStateHandle[Settings.KEY_FUNDING_OPTION_ID] = option?.id
    }

    fun fundWithCoupon(callback: (TransactionStatus) -> Unit) = launch(Dispatchers.IO) {
        mFundingUiState.update { it.copy(isLoading = true, loadingMessage = "Processing coupon...") }

        var error = ""
        val code = mCode.value

        if(code.isBlank()) error = "Please enter coupon code"

        if(error.isNotBlank()) {
            mFundingUiState.update {
                it.copy(
                    isLoading = null,
                    error = Throwable(error)
                )
            }
        } else {
            val response = repository.fundWithCoupon(
                FundWithCouponRequest(code)
            )
            processFundingWithCouponResult(response, callback)
        }
    }

    private fun processFundingWithCouponResult(
        result: NetworkResult<FundWithCouponResponse>,
        callback: (TransactionStatus) -> Unit
    ) {
        result
            .onSuccess { data ->
                var apiResponse = ""
                if(data.status.lowercase() == Status.SUCCESSFUL.title.lowercase()) {
                    apiResponse = """
                        You have successfully funded N${data.amount.toString().asMoney()}
                        to your wallet via Coupon Funding
                    """.trimIndent()
                    callback(
                        TransactionStatus(
                            status = Status.SUCCESSFUL,
                            header = "Here you go!",
                            description = apiResponse
                        )
                    )
                } else if(data.status.lowercase() == Status.PROCESSING.title.lowercase()) {
                    apiResponse = "Please wait..."
                    callback(
                        TransactionStatus(
                            status = Status.PROCESSING,
                            header = "Hmmmm!",
                            description = apiResponse
                        )
                    )
                } else {
                    apiResponse = "Sorry, couldn't complete transaction"
                    callback(
                        TransactionStatus(
                            status = Status.FAILED,
                            header = "Oh sorry!",
                            description = apiResponse
                        )
                    )
                }
                mFundingUiState.update { it.copy(
                    isLoading = null,
                    shouldShowStatusPage = true,
                ) }
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
                mFundingUiState.update { it.copy(
                    isLoading = null,
                    shouldShowStatusPage = true,
                ) }
            }
    }


    fun fundWithMonnify(context: Context) = launch(Dispatchers.IO) {
        mFundingUiState.update { it.copy(isLoading = true, loadingMessage = "Processing payment...") }

        var error = ""
        val amount = mAmount.value

        if(amount.isBlank()) error = "Please enter amount"

        if(error.isNotBlank()) {
            mFundingUiState.update {
                it.copy(
                    isLoading = null,
                    error = Throwable(error)
                )
            }
        } else {
            val response = repository.initMonnifyCardPayment(
                InitMonnifyCardPaymentRequest(amount.toDouble())
            )
            processMonnifyPaymentResult(context, response)
        }
    }

    private suspend fun processMonnifyPaymentResult(
        context: Context,
        result: NetworkResult<InitMonnifyCardPaymentResponse>
    ) {
        when(result) {
            is NetworkResult.Success -> {
                mFundingUiState.update { it.copy(loadingMessage = "Redirecting to checkout...") }
                delay(1000L)
                NetworkCheckoutHandler.cardPaymentCheckout(
                    context, result.data.checkoutUrl
                )
                mFundingUiState.update { it.copy(
                    isLoading = null,
                ) }
            }
            is NetworkResult.Failed -> Unit
            is NetworkResult.Error -> {
                mFundingUiState.update { it.copy(
                    isLoading = null,
                    error = Throwable(result.message)
                ) }
            }
        }
    }


    fun withdrawFromReferralBonus(callback: (TransactionStatus) -> Unit) = launch(Dispatchers.IO) {
        mFundingUiState.update { it.copy(
            isLoading = true,
            loadingMessage = "Withdrawing from referral bonus",
        ) }
        val amount = mAmount.value
        var error = ""

        if(!fundingRequest.value.isValid) {
            if(amount.isBlank()) error = "Please enter your amount"
        }

        if(error.isNotBlank()) {
            mFundingUiState.update { it.copy(
                isLoading = null,
                error = Throwable(error),
                loadingMessage = null,
            ) }
        } else {
            val response = repository.withdrawFromReferralBonus(
                ReferralBonusWithdrawalRequest(
                    amount.toDouble()
                )
            )
            processWithdrawFromReferralBonusResult(response, callback)
        }
    }

    private fun processWithdrawFromReferralBonusResult(
        result: NetworkResult<ReferralBonusWithdrawalResponse>,
        callback: (TransactionStatus) -> Unit
    ) {
        when(result) {
            is NetworkResult.Success -> {
                val apiResponse = """
                    N${result.data.amount} has been moved to your wallet
                """.trimIndent()
                callback(
                    TransactionStatus(
                        header = "Yo!",
                        description = apiResponse,
                        status = Status.SUCCESSFUL
                    )
                )
                mFundingUiState.update {
                    it.copy(
                        isLoading = null,
                        loadingMessage = null,
                        shouldShowStatusPage = true,
                    )
                }
                savedStateHandle[Settings.KEY_FUNDING_AMOUNT] = ""
            }
            is NetworkResult.Failed -> Unit
            is NetworkResult.Error -> {
                callback(
                    TransactionStatus(
                        header = "Oops!",
                        description = result.message,
                        status = Status.FAILED
                    )
                )
                mFundingUiState.update {
                    it.copy(
                        isLoading = null,
                        loadingMessage = null,
                        shouldShowStatusPage = true,
                    )
                }
                savedStateHandle[Settings.KEY_FUNDING_AMOUNT] = ""
            }
        }
    }


    override fun onHandledThrowable() {
        mFundingUiState.update {
            it.copy(
                error = null
            )
        }
    }
}

private fun getBanks(
    repository: AppRepository
): Flow<List<UserBankResponse>> {
    return repository.getBanks()
}

private fun getFundingRequest(
    code: StateFlow<String>,
    amount: StateFlow<String>,
    fundOptionId: StateFlow<Int>
): Flow<FundingRequest> {
    return combine(
        code,
        amount,
        fundOptionId
    ) { c, a, f ->
        val fundOption = FundOption.getById(f)
        FundingRequest(
            code = c,
            amount = a,
            fundOption = fundOption
        )
    }
}


data class FundingUiState(
    val isLoading: Boolean? = null,
    val loadingMessage: String? = null,
    val error: Throwable? = null,
    val shouldShowStatusPage: Boolean? = null,
)