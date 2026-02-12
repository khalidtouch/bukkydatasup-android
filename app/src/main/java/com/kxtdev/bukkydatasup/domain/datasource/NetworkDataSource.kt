package com.kxtdev.bukkydatasup.domain.datasource

import com.kxtdev.bukkydatasup.common.utils.recordException
import com.kxtdev.bukkydatasup.network.api.ApiService
import com.kxtdev.bukkydatasup.network.api.AuthService
import com.kxtdev.bukkydatasup.network.models.NetworkAdvertisementItem
import com.kxtdev.bukkydatasup.network.models.NetworkAlertNotificationResponse
import com.kxtdev.bukkydatasup.network.models.NetworkBulkSMSHistoryResponse
import com.kxtdev.bukkydatasup.network.models.NetworkBuyDataHistoryItemResponse
import com.kxtdev.bukkydatasup.network.models.NetworkBuyDataHistoryResponse
import com.kxtdev.bukkydatasup.network.models.NetworkBuyDataRequest
import com.kxtdev.bukkydatasup.network.models.NetworkBuyDataResponse
import com.kxtdev.bukkydatasup.network.models.NetworkDisabledServiceResponse
import com.kxtdev.bukkydatasup.network.models.NetworkFundWithCouponRequest
import com.kxtdev.bukkydatasup.network.models.NetworkFundWithCouponResponse
import com.kxtdev.bukkydatasup.network.models.NetworkInitMonnifyCardPaymentRequest
import com.kxtdev.bukkydatasup.network.models.NetworkInitMonnifyCardPaymentResponse
import com.kxtdev.bukkydatasup.network.models.NetworkLoginRequest
import com.kxtdev.bukkydatasup.network.models.NetworkLoginResponse
import com.kxtdev.bukkydatasup.network.models.NetworkLogoutResponse
import com.kxtdev.bukkydatasup.network.models.NetworkPrintCardHistoryResponse
import com.kxtdev.bukkydatasup.network.models.NetworkPrintCardRequest
import com.kxtdev.bukkydatasup.network.models.NetworkPrintCardResponse
import com.kxtdev.bukkydatasup.network.models.NetworkPushNotificationPagedResponse
import com.kxtdev.bukkydatasup.network.models.NetworkRechargeAirtimeHistoryItemResponse
import com.kxtdev.bukkydatasup.network.models.NetworkRechargeAirtimeHistoryResponse
import com.kxtdev.bukkydatasup.network.models.NetworkRechargeAirtimeRequest
import com.kxtdev.bukkydatasup.network.models.NetworkRechargeAirtimeResponse
import com.kxtdev.bukkydatasup.network.models.NetworkRechargeMeterHistory
import com.kxtdev.bukkydatasup.network.models.NetworkRechargeMeterHistoryItem
import com.kxtdev.bukkydatasup.network.models.NetworkReferralBonusWithdrawalRequest
import com.kxtdev.bukkydatasup.network.models.NetworkReferralBonusWithdrawalResponse
import com.kxtdev.bukkydatasup.network.models.NetworkRegisterFCMTokenRequest
import com.kxtdev.bukkydatasup.network.models.NetworkRegisterFCMTokenResponse
import com.kxtdev.bukkydatasup.network.models.NetworkRegisterRequest
import com.kxtdev.bukkydatasup.network.models.NetworkRegisterResponse
import com.kxtdev.bukkydatasup.network.models.NetworkResetPasswordRequest
import com.kxtdev.bukkydatasup.network.models.NetworkResetPasswordResponse
import com.kxtdev.bukkydatasup.network.models.NetworkResetTransactionPinResponse
import com.kxtdev.bukkydatasup.network.models.NetworkResultCheckerHistoryItemResponse
import com.kxtdev.bukkydatasup.network.models.NetworkResultCheckerHistoryResponse
import com.kxtdev.bukkydatasup.network.models.NetworkResultCheckerRequest
import com.kxtdev.bukkydatasup.network.models.NetworkResultCheckerResponse
import com.kxtdev.bukkydatasup.network.models.NetworkSendBulkSMSRequest
import com.kxtdev.bukkydatasup.network.models.NetworkSendBulkSMSResponse
import com.kxtdev.bukkydatasup.network.models.NetworkSubscribeBillRequest
import com.kxtdev.bukkydatasup.network.models.NetworkSubscribeBillResponse
import com.kxtdev.bukkydatasup.network.models.NetworkSubscribeCableTVHistoryItemResponse
import com.kxtdev.bukkydatasup.network.models.NetworkSubscribeCableTVHistoryResponse
import com.kxtdev.bukkydatasup.network.models.NetworkSubscribeCableTVRequest
import com.kxtdev.bukkydatasup.network.models.NetworkSubscribeCableTVResponse
import com.kxtdev.bukkydatasup.network.models.NetworkTransferFundsResponse
import com.kxtdev.bukkydatasup.network.models.NetworkTransferRequest
import com.kxtdev.bukkydatasup.network.models.NetworkUpgradeUserResponse
import com.kxtdev.bukkydatasup.network.models.NetworkUserProfileResponse
import com.kxtdev.bukkydatasup.network.models.NetworkValidateMeterResponse
import com.kxtdev.bukkydatasup.network.models.NetworkValidateSmartCardNumberResponse
import com.kxtdev.bukkydatasup.network.models.NetworkVerifyReservedAccountsRequest
import com.kxtdev.bukkydatasup.network.models.NetworkVerifyReservedAccountsResponse
import com.kxtdev.bukkydatasup.network.models.NetworkWalletSummaryHistoryResponse
import com.kxtdev.bukkydatasup.network.utils.BadRequestException
import com.kxtdev.bukkydatasup.network.utils.InternetException
import com.kxtdev.bukkydatasup.network.utils.NetworkConnectionHelper
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import com.kxtdev.bukkydatasup.network.utils.ServerErrorException
import com.kxtdev.bukkydatasup.network.utils.getResponse
import com.kxtdev.bukkydatasup.network.utils.networkCall
import javax.inject.Inject

const val TAG = "NetworkDataSource"

class NetworkDataSource @Inject constructor(
    private val apiService: ApiService,
    private val authService: AuthService,
    private val networkHelper: NetworkConnectionHelper
) {
    suspend fun login(request: NetworkLoginRequest): NetworkResult<NetworkLoginResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = { authService.login(request) }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun logout(): NetworkResult<NetworkLogoutResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.logout() }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getUserProfile(): NetworkResult<NetworkUserProfileResponse> {
        try {

            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.getUserProfile() }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun buyData(request: NetworkBuyDataRequest): NetworkResult<NetworkBuyDataResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.buyData(request) }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getDataHistory(page: Int): NetworkResult<NetworkBuyDataHistoryResponse> {
        try {

            val response = apiService.getDataTransactionHistory(page)
                .getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }


    suspend fun rechargeAirtime(request: NetworkRechargeAirtimeRequest):
            NetworkResult<NetworkRechargeAirtimeResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.rechargeAirtime(request) }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getAirtimeHistory(page: Int): NetworkResult<NetworkRechargeAirtimeHistoryResponse> {
        try {

            val response = apiService.getAirtimeTopUpHistory(page)
                .getResponse()

            return NetworkResult.success(response)
        }catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun subscribeCableTV(request: NetworkSubscribeCableTVRequest):
            NetworkResult<NetworkSubscribeCableTVResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.subscribeCableTV(request) }
            ).getResponse()

            return NetworkResult.success(response)
        }catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getCableHistory(page: Int):
            NetworkResult<NetworkSubscribeCableTVHistoryResponse> {
        try {

            val response = apiService.getCableTVHistory(page)
                .getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun validateSmartCardNumber(
        cableId: Int,
        smartCardNumber: String
    ): NetworkResult<NetworkValidateSmartCardNumberResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.validateSmartCardNumber(cableId, smartCardNumber) }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun validateMeter(
        discoId: Int,
        meterNumber: String,
        meterType: String
    ): NetworkResult<NetworkValidateMeterResponse> {

        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = {
                    apiService.validateMeter(
                        discoId,
                        meterNumber,
                        meterType
                    )
                }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }

    }



    suspend fun resetTransactionPin(
        pin1: String,
        pin2: String,
        password: String,
    ): NetworkResult<NetworkResetTransactionPinResponse> {
        try  {
            val response = networkCall(
                networkHelper = networkHelper,
                service = {
                    apiService.resetTransactionPin(
                        pin1,
                        pin2,
                        password
                    )
                }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun resetPassword(
        request: NetworkResetPasswordRequest
    ): NetworkResult<NetworkResetPasswordResponse> {
        try  {
            val response = networkCall(
                networkHelper = networkHelper,
                service = {
                    apiService.resetPassword(request)
                }
            ).getResponse()

            return NetworkResult.success(response)
        }catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun fundWithCoupon(
        request: NetworkFundWithCouponRequest
    ): NetworkResult<NetworkFundWithCouponResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = {
                    apiService.fundWithCoupon(request)
                }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun transferFunds(
        request: NetworkTransferRequest
    ): NetworkResult<NetworkTransferFundsResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = {
                    apiService.transferFunds(request)
                }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun subscribeBill(
        request: NetworkSubscribeBillRequest
    ): NetworkResult<NetworkSubscribeBillResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = {
                    apiService.subscribeBill(request)
                }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }


    suspend fun checkResult(
        request: NetworkResultCheckerRequest
    ): NetworkResult<NetworkResultCheckerResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = {
                    apiService.checkResult(request)
                }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getResultCheckerHistory(page: Int): NetworkResult<NetworkResultCheckerHistoryResponse> {
        try {
            val response = apiService.getResultCheckerHistory(page)
                .getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }


    suspend fun upgradeUser(newPackage: String):
            NetworkResult<NetworkUpgradeUserResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.upgradeUser(newPackage) }
            ).getResponse()

            return NetworkResult.success(response)
        }catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun register(request: NetworkRegisterRequest):
            NetworkResult<NetworkRegisterResponse> {
        try {

            val response = networkCall(
                networkHelper = networkHelper,
                service = { authService.register(request) }
            ).getResponse()

            return NetworkResult.success(response)

        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }


    }

    suspend fun registerFCMToken(request: NetworkRegisterFCMTokenRequest):
            NetworkResult<NetworkRegisterFCMTokenResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.registerFCMToken(request) }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getBillPaymentHistory(page: Int): NetworkResult<NetworkRechargeMeterHistory> {
        try {
            val response = apiService.getBillPaymentHistory(page)
                .getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }


    suspend fun getPushNotifications(page: Int): NetworkResult<NetworkPushNotificationPagedResponse?> {
        try {
            val response = apiService.getPushNotifications(page)
                .getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun initMonnifyCardPayment(request: NetworkInitMonnifyCardPaymentRequest):
            NetworkResult<NetworkInitMonnifyCardPaymentResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.initMonnifyCardPayment(request) }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }


    suspend fun verifyReservedAccounts(request: NetworkVerifyReservedAccountsRequest):
            NetworkResult<NetworkVerifyReservedAccountsResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.verifyReservedAccounts(request) }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun withdrawFromReferralBonus(
        request: NetworkReferralBonusWithdrawalRequest
    ): NetworkResult<NetworkReferralBonusWithdrawalResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.withdrawFromReferralBonus(request) }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun printCard(
        request: NetworkPrintCardRequest
    ): NetworkResult<NetworkPrintCardResponse> {
        try  {
            val response = networkCall(
                networkHelper = networkHelper,
                service = {
                    apiService.printCard(request)
                }
            ).getResponse()

            return NetworkResult.success(response)

        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getWalletSummaryHistory(page: Int): NetworkResult<NetworkWalletSummaryHistoryResponse> {
        try {

            val response = apiService.getWalletSummaryHistory(page)
                .getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }


    suspend fun sendBulkSMS(
        request: NetworkSendBulkSMSRequest
    ): NetworkResult<NetworkSendBulkSMSResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.sendBulkSMS(request) }
            ).getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getPrintCardHistory(page: Int): NetworkResult<NetworkPrintCardHistoryResponse> {
        try {
            val response = apiService.getPrintCardHistory(page)
                .getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getBulkSMSHistory(page: Int): NetworkResult<NetworkBulkSMSHistoryResponse> {
        try {
            val response = apiService.getBulkSMSHistory(page)
                .getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }


    suspend fun getDisabledService(): NetworkResult<NetworkDisabledServiceResponse> {
        try {
            val response = apiService.getDisabledServices()
                .getResponse()

            return NetworkResult.success(response)
        } catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getAlertNotification():
            NetworkResult<NetworkAlertNotificationResponse> {
        try {
            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.getAlertNotification() }
            ).getResponse()

            return NetworkResult.success(response)
        }catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getDataReceipt(id: Int): NetworkResult<NetworkBuyDataHistoryItemResponse> {
        try {

            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.getDataReceipt(id) }
            ).getResponse()

            return NetworkResult.success(response)
        }catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getAirtimeReceipt(id: Int): NetworkResult<NetworkRechargeAirtimeHistoryItemResponse> {
        try {

            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.getAirtimeReceipt(id) }
            ).getResponse()

            return NetworkResult.success(response)
        }catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getCableReceipt(id: Int): NetworkResult<NetworkSubscribeCableTVHistoryItemResponse> {
        try {

            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.getCableReceipt(id) }
            ).getResponse()

            return NetworkResult.success(response)
        }catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getBillReceipt(id: Int): NetworkResult<NetworkRechargeMeterHistoryItem> {
        try {

            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.getBillReceipt(id) }
            ).getResponse()

            return NetworkResult.success(response)
        }catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getResultCheckerReceipt(id: Int): NetworkResult<NetworkResultCheckerHistoryItemResponse> {
        try {

            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.getResultCheckerReceipt(id) }
            ).getResponse()

            return NetworkResult.success(response)
        }catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

    suspend fun getAds(): NetworkResult<List<NetworkAdvertisementItem>> {
        try {

            val response = networkCall(
                networkHelper = networkHelper,
                service = { apiService.getAds() }
            ).getResponse()

            return NetworkResult.success(response)
        }catch (e: BadRequestException) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        } catch (e: ServerErrorException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: InternetException) {
            e.recordException()
            return NetworkResult.error(e.message)
        } catch (e: Exception) {
            e.recordException()
            return NetworkResult.error(e.message.orEmpty())
        }
    }

}

