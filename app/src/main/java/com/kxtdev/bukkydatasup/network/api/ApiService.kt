package com.kxtdev.bukkydatasup.network.api

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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @POST("/rest-auth/logout/")
    suspend fun logout(): Response<NetworkLogoutResponse>

    @GET("/api/user/")
    suspend fun getUserProfile(): Response<NetworkUserProfileResponse>

    @POST("/api/data/")
    suspend fun buyData(@Body request: NetworkBuyDataRequest): Response<NetworkBuyDataResponse>

    @GET("/api/data/")
    suspend fun getDataTransactionHistory(@Query("page") page: Int): Response<NetworkBuyDataHistoryResponse>

    @POST("/api/topup/")
    suspend fun rechargeAirtime(@Body request: NetworkRechargeAirtimeRequest): Response<NetworkRechargeAirtimeResponse>

    @GET("/api/topup/")
    suspend fun getAirtimeTopUpHistory(@Query("page") page: Int): Response<NetworkRechargeAirtimeHistoryResponse>

    @POST("/api/cablesub/")
    suspend fun subscribeCableTV(@Body request: NetworkSubscribeCableTVRequest): Response<NetworkSubscribeCableTVResponse>

    @GET("/api/cablesub/")
    suspend fun getCableTVHistory(@Query("page") page: Int): Response<NetworkSubscribeCableTVHistoryResponse>


    @GET("/api/validateiuc/")
    suspend fun validateSmartCardNumber(
        @Query("cable_id") cableId: Int,
        @Query("smart_card_number") smartCardNumber: String
    ): Response<NetworkValidateSmartCardNumberResponse>


    @POST("/api/billpayment/")
    suspend fun rechargeMeter(@Body request: NetworkSubscribeBillRequest): Response<NetworkSubscribeBillResponse>


    @GET("/api/validatemeter/")
    suspend fun validateMeter(
        @Query("disco_id") discoId: Int,
        @Query("meter_number") meterNumber: String,
        @Query("meter_type") meterType: String
    ): Response<NetworkValidateMeterResponse>


    @GET("/api/resetpin/")
    suspend fun resetTransactionPin(
        @Query("pin1") pin1: String,
        @Query("pin2") pin2: String,
        @Query("password") password: String,
    ): Response<NetworkResetTransactionPinResponse>


    @POST("/api/passwordchange/")
    suspend fun resetPassword(
        @Body request: NetworkResetPasswordRequest
    ): Response<NetworkResetPasswordResponse>


    @POST("/api/couponpayment/")
    suspend fun fundWithCoupon(
        @Body request: NetworkFundWithCouponRequest
    ): Response<NetworkFundWithCouponResponse>


    @POST("/api/transfer/")
    suspend fun transferFunds(
        @Body request: NetworkTransferRequest
    ): Response<NetworkTransferFundsResponse>


    @POST("/api/billpayment/")
    suspend fun subscribeBill(
        @Body request: NetworkSubscribeBillRequest
    ): Response<NetworkSubscribeBillResponse>

    @GET("/api/billpayment/")
    suspend fun getBillPaymentHistory(
        @Query("page") page: Int
    ): Response<NetworkRechargeMeterHistory>


    @POST("/api/epin/")
    suspend fun checkResult(
        @Body request: NetworkResultCheckerRequest
    ): Response<NetworkResultCheckerResponse>


    @GET("/api/epin/")
    suspend fun getResultCheckerHistory(
        @Query("page") page: Int
    ): Response<NetworkResultCheckerHistoryResponse>


    @POST("/api/rechargepin/")
    suspend fun printCard(
        @Body request: NetworkPrintCardRequest
    ): Response<NetworkPrintCardResponse>

    @GET("/api/upgrade/")
    suspend fun upgradeUser(
        @Query("package") newPackage: String
    ): Response<NetworkUpgradeUserResponse>

    @POST("/fcm-token-update/")
    suspend fun registerFCMToken(
        @Body request: NetworkRegisterFCMTokenRequest
    ): Response<NetworkRegisterFCMTokenResponse>


    @GET("/api/push_notifications/")
    suspend fun getPushNotifications(
        @Query("page") page: Int
    ): Response<NetworkPushNotificationPagedResponse>


    @POST("/api/monnify-init/")
    suspend fun initMonnifyCardPayment(
        @Body request: NetworkInitMonnifyCardPaymentRequest
    ): Response<NetworkInitMonnifyCardPaymentResponse>


    @POST("/api/reserve_account/")
    suspend fun verifyReservedAccounts(
        @Body request: NetworkVerifyReservedAccountsRequest
    ): Response<NetworkVerifyReservedAccountsResponse>


    @POST("/api/bonus_transfer/")
    suspend fun withdrawFromReferralBonus(
        @Body request: NetworkReferralBonusWithdrawalRequest
    ): Response<NetworkReferralBonusWithdrawalResponse>

    @GET("/api/Wallet_summary/")
    suspend fun getWalletSummaryHistory(@Query("page") page: Int): Response<NetworkWalletSummaryHistoryResponse>

    @POST("/api/sendsms/")
    suspend fun sendBulkSMS(
        @Body request: NetworkSendBulkSMSRequest
    ): Response<NetworkSendBulkSMSResponse>

    @GET("/api/rechargepin/")
    suspend fun getPrintCardHistory(
        @Query("page") page: Int
    ): Response<NetworkPrintCardHistoryResponse>

    @GET("/api/sendsms/")
    suspend fun getBulkSMSHistory(
        @Query("page") page: Int
    ): Response<NetworkBulkSMSHistoryResponse>

    @GET("/api/services/")
    suspend fun getDisabledServices(): Response<NetworkDisabledServiceResponse>

    @GET("/api/alert/")
    suspend fun getAlertNotification(): Response<NetworkAlertNotificationResponse>

    @GET("/api/data/{id}")
    suspend fun getDataReceipt(
        @Path("id") id: Int
    ): Response<NetworkBuyDataHistoryItemResponse>

    @GET("/api/topup/{id}")
    suspend fun getAirtimeReceipt(
        @Path("id") id: Int
    ): Response<NetworkRechargeAirtimeHistoryItemResponse>

    @GET("/api/cablesub/{id}")
    suspend fun getCableReceipt(
        @Path("id") id: Int
    ): Response<NetworkSubscribeCableTVHistoryItemResponse>

    @GET("/api/billpayment/{id}")
    suspend fun getBillReceipt(
        @Path("id") id: Int
    ): Response<NetworkRechargeMeterHistoryItem>

    @GET("/api/epin/{id}")
    suspend fun getResultCheckerReceipt(
        @Path("id") id: Int
    ): Response<NetworkResultCheckerHistoryItemResponse>

    @GET("/api/ads/")
    suspend fun getAds(): Response<List<NetworkAdvertisementItem>>


}
