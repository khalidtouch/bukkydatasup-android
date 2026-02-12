package com.kxtdev.bukkydatasup.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NetworkLoginRequest(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String
)

@Serializable
data class NetworkRegisterRequest(
    @SerialName("username") val username: String,
    @SerialName("password1") val password: String,
    @SerialName("password2") val confirmPassword: String,
    @SerialName("Phone") val phone: String,
    @SerialName("email") val email: String,
    @SerialName("FullName") val fullname: String? = null,
    @SerialName("Address") val address: String? = null,
    @SerialName("referer_username") val refererUsername: String? = null,
)


@Serializable
data class NetworkBuyDataRequest(
    @SerialName("network") val network: Int,
    @SerialName("mobile_number") val phone: String,
    @SerialName("plan") val plan: Int,
    @SerialName("Ported_number") val isPorted: Boolean,
)

@Serializable
data class NetworkRechargeAirtimeRequest(
    @SerialName("network") val network: Int,
    @SerialName("mobile_number") val phone: String,
    @SerialName("amount") val amount: Long,
    @SerialName("airtime_type") val airtimeType: String,
    @SerialName("Ported_number") val isPorted: Boolean,
)

@Serializable
data class NetworkSubscribeCableTVRequest(
    @SerialName("cablename") val cableId: Int,
    @SerialName("cableplan") val planId: Int,
    @SerialName("smart_card_number") val smartCardNumber: String
)

@Serializable
data class NetworkSubscribeBillRequest(
    @SerialName("disco_name") val discoId: Int,
    @SerialName("amount") val amount: Long,
    @SerialName("meter_number") val meterNumber: String,
    @SerialName("MeterType") val meterType: String,
    @SerialName("Customer_Phone") val phone: String,
    @SerialName("customer_name") val customerName: String,
    @SerialName("customer_address") val customerAddress: String,
)


@Serializable
data class NetworkResetPasswordRequest(
    @SerialName("old_password") val oldPassword: String,
    @SerialName("new_password1") val newPassword1: String,
    @SerialName("new_password2") val newPassword2: String,
)


@Serializable
data class NetworkFundWithCouponRequest(
    @SerialName("Code") val code: String
)


@Serializable
data class NetworkTransferRequest(
    @SerialName("amount") val amount: Long,
    @SerialName("receiver_username") val recipientUsername: String,
)


@Serializable
data class NetworkResultCheckerRequest(
    @SerialName("exam_name") val examName: String,
    @SerialName("quantity") val quantity: Int
)


@Serializable
data class NetworkPrintCardRequest(
    @SerialName("network") val networkId: Int,
    @SerialName("network_amount") val networkAmount: Int,
    @SerialName("quantity") val quantity: Int,
    @SerialName("name_on_card") val nameOnCard: String
)

@Serializable
data class NetworkVerifyReservedAccountsRequest(
    @SerialName("methodtype") val method: String? = null,
    @SerialName("full_name") val fullname: String? = null,
    @SerialName("bvn") val bvn: String? = null,
    @SerialName("nin") val nin: String? = null,
)

@Serializable
data class NetworkRegisterFCMTokenRequest(
    @SerialName("fcm_token") val token: String
)

@Serializable
data class NetworkSendBulkSMSRequest(
    @SerialName("recetipient") val recipient: String,
    @SerialName("message") val message: String,
    @SerialName("sender") val sender: String,
    @SerialName("DND") val dnd: Boolean,
)

@Serializable
data class NetworkInitMonnifyCardPaymentRequest(
    @SerialName("amount") val amount: Long,
)

@Serializable
data class NetworkInitPaystackCardPaymentRequest(
    @SerialName("amount") val amount: Long,
)

@Serializable
data class NetworkReferralBonusWithdrawalRequest(
    @SerialName("amount") val amount: String,
)
