package com.kxtdev.bukkydatasup.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NetworkLoginResponse(
    @SerialName("key") val token: String = "",
)

@Serializable
data class NetworkRegisterResponse(
    @SerialName("message") val message: String = ""
)



@Serializable
data class NetworkLogoutResponse(
    @SerialName("detail") val message: String = "",
)

@Serializable
data class NetworkRechargeAirtimeResponse(
    @SerialName("id") val id: Int = 1,
    @SerialName("airtime_type") val airtimeType: String = "",
    @SerialName("network") val network: Int = 1,
    @SerialName("ident") val reference: String = "",
    @SerialName("mobile_number") val phone: String = "",
    @SerialName("amount") val amount: String = "0.0",
    @SerialName("plan_amount") val planAmount: String = "N0.0",
    @SerialName("plan_network") val planNetwork: String = "",
    @SerialName("paid_amount") val paidAmount: String = "0.0",
    @SerialName("balance_before") val balanceBefore: String = "0.0",
    @SerialName("balance_after") val balanceAfter: String = "0.0",
    @SerialName("Status") val status: String = "",
    @SerialName("create_date") val dateCreated: String = "",
    @SerialName("customer_name") val customerName: String? = null,
    @SerialName("Ported_number") val isPorted: Boolean? = null,
)


@Serializable
data class NetworkRechargeAirtimeHistoryResponse(
    @SerialName("count") val count: Long = 0,
    @SerialName("next") val next: String? = null,
    @SerialName("previous") val previous: String? = null,
    @SerialName("results") val results: List<NetworkRechargeAirtimeHistoryItemResponse> = listOf(),
)


@Serializable
data class NetworkRechargeAirtimeHistoryItemResponse(
    @SerialName("id") val id: Int = 1,
    @SerialName("airtime_type") val airtimeType: String = "",
    @SerialName("network") val network: Int = 1,
    @SerialName("ident") val reference: String = "",
    @SerialName("mobile_number") val phone: String = "",
    @SerialName("amount") val amount: String = "0.0",
    @SerialName("plan_amount") val planAmount: String = "N0.0",
    @SerialName("plan_network") val planNetwork: String = "",
    @SerialName("paid_amount") val paidAmount: String = "0.0",
    @SerialName("balance_before") val balanceBefore: String = "0.0",
    @SerialName("balance_after") val balanceAfter: String = "0.0",
    @SerialName("Status") val status: String = "",
    @SerialName("create_date") val dateCreated: String = "",
    @SerialName("Ported_number") val isPorted: Boolean = false,
)


@Serializable
data class NetworkBuyDataResponse(
    @SerialName("id") val id: Int = 1,
    @SerialName("network") val network: Int = 1,
    @SerialName("ident") val reference: String = "",
    @SerialName("balance_before") val balanceBefore: String = "0.0",
    @SerialName("balance_after") val balanceAfter: String = "0.0",
    @SerialName("mobile_number") val phone: String = "",
    @SerialName("plan") val planId: Int = 2,
    @SerialName("Status") val status: String = "",
    @SerialName("api_response") val apiResponse: String = "",
    @SerialName("plan_network") val planNetwork: String = "",
    @SerialName("plan_name") val planName: String = "",
    @SerialName("plan_amount") val planAmount: String = "N0.0",
    @SerialName("create_date") val dateCreated: String = "",
    @SerialName("Ported_number") val isPorted: Boolean = false,
)


@Serializable
data class NetworkBuyDataHistoryResponse(
    @SerialName("count") val count: Long = 0,
    @SerialName("next") val next: String? = null,
    @SerialName("previous") val previous: String? = null,
    @SerialName("results") val results: List<NetworkBuyDataHistoryItemResponse> = listOf(),
)

@Serializable
data class NetworkBuyDataHistoryItemResponse(
    @SerialName("id") val id: Int = 1,
    @SerialName("network") val network: Int = 1,
    @SerialName("ident") val reference: String = "",
    @SerialName("balance_before") val balanceBefore: String = "0.0",
    @SerialName("balance_after") val balanceAfter: String = "0.0",
    @SerialName("mobile_number") val phone: String = "",
    @SerialName("plan") val planId: Int = 2,
    @SerialName("Status") val status: String = "",
    @SerialName("api_response") val apiResponse: String = "",
    @SerialName("plan_network") val planNetwork: String = "",
    @SerialName("plan_name") val planName: String = "",
    @SerialName("plan_amount") val planAmount: String = "N0.0",
    @SerialName("create_date") val dateCreated: String = "",
    @SerialName("Ported_number") val isPorted: Boolean = false,
)


@Serializable
data class NetworkRechargeMeterResponse(
    @SerialName("id") val id: Int = 1,
    @SerialName("ident") val reference: String = "",
    @SerialName("disco_name") val discoName: String = "",
    @SerialName("amount") val amount: String = "0.0",
    @SerialName("Customer_Phone") val phone: String = "",
    @SerialName("meter_number") val meterNumber: String = "",
    @SerialName("token") val token: String = "",
    @SerialName("MeterType") val meterType: String = "",
    @SerialName("paid_amount") val paidAmount: String = "0.0",
    @SerialName("balance_before") val balanceBefore: String = "0.0",
    @SerialName("balance_after") val balanceAfter: String = "0.0",
    @SerialName("Status") val status: String = "",
    @SerialName("create_date") val dateCreated: String = "",
    @SerialName("customer_name") val customerName: String = "",
    @SerialName("customer_address") val customerAddress: String = "",
)


@Serializable
data class NetworkValidateMeterResponse(
    @SerialName("invalid") val isInvalid: Boolean,
    @SerialName("name") val name: String,
    @SerialName("address") val address: String,
)

@Serializable
data class NetworkSubscribeCableTVResponse(
    @SerialName("id") val id: Int = 1,
    @SerialName("ident") val reference: String = "",
    @SerialName("cablename") val cableId: Int = 2,
    @SerialName("cableplan") val planId: Int = 2,
    @SerialName("package") val packageName: String = "",
    @SerialName("plan_amount") val planAmount: String = "0.0",
    @SerialName("paid_amount") val paidAmount: Double = 0.0,
    @SerialName("smart_card_number") val smartCardNumber: String = "",
    @SerialName("balance_before") val balanceBefore: String = "0.0",
    @SerialName("balance_after") val balanceAfter: String = "0.0",
    @SerialName("Status") val status: String = "",
    @SerialName("create_date") val dateCreated: String = "",
    @SerialName("customer_name") val customerName: String = "",
)


@Serializable
data class NetworkSubscribeCableTVHistoryResponse(
    @SerialName("count") val count: Long = 0,
    @SerialName("next") val next: String? = null,
    @SerialName("previous") val previous: String? = null,
    @SerialName("results") val results: List<NetworkSubscribeCableTVHistoryItemResponse> = listOf(),
)

@Serializable
data class NetworkSubscribeCableTVHistoryItemResponse(
    @SerialName("id") val id: Int = 1,
    @SerialName("ident") val reference: String = "",
    @SerialName("cablename") val cableId: Int = 2,
    @SerialName("cableplan") val planId: Int = 2,
    @SerialName("package") val packageName: String = "",
    @SerialName("plan_amount") val planAmount: String = "0.0",
    @SerialName("paid_amount") val paidAmount: String = "0.0",
    @SerialName("smart_card_number") val smartCardNumber: String = "",
    @SerialName("balance_before") val balanceBefore: String = "0.0",
    @SerialName("balance_after") val balanceAfter: String = "0.0",
    @SerialName("Status") val status: String = "",
    @SerialName("create_date") val dateCreated: String = "",
    @SerialName("customer_name") val customerName: String = "",
)


@Serializable
data class NetworkValidateSmartCardNumberResponse(
    @SerialName("invalid") val isInvalid: Boolean,
    @SerialName("name") val name: String,
)

@Serializable
data class NetworkUserProfileResponse(
    @SerialName("user") val user: NetworkUserInfoResponse? = null,
    @SerialName("verification") val verification: NetworkVerificationResponse? = null,
    @SerialName("notification") val notification: NetworkNotificationResponse? = null,
    @SerialName("percentage") val percentage: NetworkPercentageResponse? = null,
    @SerialName("topuppercentage") val topUpPercentage: NetworkTopUpPercentageResponse? = null,
    @SerialName("Admin_number") val adminNumbers: List<NetworkAdminNumberResponse>? = null,
    @SerialName("Exam") val exams: NetworkExamsResponse? = null,
    @SerialName("banks") val banks: List<NetworkBankResponse>? = null,
    @SerialName("banners") val banners: List<NetworkBannerResponse>? = null,
    @SerialName("Dataplans") val dataPlans: NetworkDataPlans? = null,
    @SerialName("Cableplan") val cablePlans: NetworkCablePlansResponse? = null,
    @SerialName("support_phone_number") val supportPhoneNumber: String? = null,
    @SerialName("whatsapp_group_link") val groupLink: String? = null,
    @SerialName("upgrade_to_affiliate_fee") val affiliateUpgradeFee: Double? = null,
    @SerialName("upgrade_to_topuser_fee") val topUserUpgradeFee: Double? = null,
    @SerialName("recharge") val recharge: NetworkRechargeResponse? = null,
)


@Serializable
data class NetworkUserInfoResponse(
    @SerialName("id") val id: Int? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("FullName") val fullname: String? = null,
    @SerialName("pin") val pin: String? = null,
    @SerialName("Address") val address: String? = null,
    @SerialName("Phone") val phone: String? = null,
    @SerialName("user_type") val userType: String? = null,
    @SerialName("email_verify") val isEmailVerified: Boolean? = null,
    @SerialName("verify") val isAccountVerified: Boolean? = null,
    @SerialName("password") val passwordHash: String? = null,
    @SerialName("Account_Balance") val accountBalance: Double? = null,
    @SerialName("wallet_balance") val walletBalance: String? = null,
    @SerialName("bonus_balance") val bonusBalance: String? = null,
    @SerialName("referer_username") val referrerUsername: String? = null,
    @SerialName("bank_accounts") val userBanks: List<NetworkUserBankResponse>? = null,
    @SerialName("reservedaccountNumber") val reservedAccountNumber: String? = null,
    @SerialName("reservedbankName") val reservedBankName: String? = null,
    @SerialName("BVN") val bvn: String? = null,
    @SerialName("NIN") val nin: String? = null,
)

@Serializable
data class NetworkBannerResponse(
    @SerialName("banner") val banner: String? = null,
    @SerialName("route") val route: String? = null,
)

@Serializable
data class NetworkVerificationResponse(
    @SerialName("bvn_verified") val bvnVerified: Boolean? = null,
    @SerialName("nin_verified") val ninVerified: Boolean? = null,
)

@Serializable
data class NetworkNotificationResponse(
    @SerialName("message") val message: String = ""
)

@Serializable
data class NetworkPercentageResponse(
    @SerialName("MTN") val mtn: NetworkPercentageItemResponse = NetworkPercentageItemResponse(),
    @SerialName("GLO") val glo: NetworkPercentageItemResponse = NetworkPercentageItemResponse(),
    @SerialName("9MOBILE") val nineMobile: NetworkPercentageItemResponse = NetworkPercentageItemResponse(),
    @SerialName("AIRTEL") val airtel: NetworkPercentageItemResponse = NetworkPercentageItemResponse(),
)

@Serializable
data class NetworkPercentageItemResponse(
    @SerialName("percent") val percentage: Double? = null,
    @SerialName("phone") val phone: String? = null,
)

@Serializable
data class NetworkTopUpPercentageResponse(
    @SerialName("MTN") val mtn: NetworkTopUpPercentageItemResponse = NetworkTopUpPercentageItemResponse(),
    @SerialName("GLO") val glo: NetworkTopUpPercentageItemResponse = NetworkTopUpPercentageItemResponse(),
    @SerialName("9MOBILE") val nineMobile: NetworkTopUpPercentageItemResponse = NetworkTopUpPercentageItemResponse(),
    @SerialName("AIRTEL") val airtel: NetworkTopUpPercentageItemResponse = NetworkTopUpPercentageItemResponse(),
)

@Serializable
data class NetworkTopUpPercentageItemResponse(
    @SerialName("VTU") val vtu: Double = 0.0,
    @SerialName("Share and Sell") val shareNSell: Double = 0.0,
)

@Serializable
data class NetworkAdminNumberResponse(
    @SerialName("network") val network: String = "",
    @SerialName("phone_number") val phone: String = "",
)

@Serializable
data class NetworkExamsResponse(
    @SerialName("WAEC") val waec: NetworkExamItem? = null,
    @SerialName("NECO") val neco: NetworkExamItem? = null,
    @SerialName("NABTEB") val nabteb: NetworkExamItem? = null,
)

@Serializable
data class NetworkExamItem(
    @SerialName("amount") val amount: Double = 0.0
)

@Serializable
data class NetworkUserBankResponse(
    @SerialName("bankCode") val bankCode: String = "",
    @SerialName("bankName") val bankName: String = "",
    @SerialName("accountNumber") val accountNumber: String = "",
    @SerialName("accountName") val accountName: String = "",
    @SerialName("trackingReference") val trackingReference: String? = null,
    @SerialName("Reserved_Account_Id") val reservedAccountId: String? = null,
)

@Serializable
data class NetworkBankResponse(
    @SerialName("bank_name") val bankName: String = "",
    @SerialName("account_name") val accountName: String = "",
    @SerialName("account_number") val accountNumber: String = "",
)

@Serializable
data class NetworkDataPlans(
    @SerialName("MTN_PLAN") val mtnPlan: NetworkDataPlanCategory = NetworkDataPlanCategory(),
    @SerialName("GLO_PLAN") val gloPlan: NetworkDataPlanCategory = NetworkDataPlanCategory(),
    @SerialName("AIRTEL_PLAN") val airtelPlan: NetworkDataPlanCategory = NetworkDataPlanCategory(),
    @SerialName("9MOBILE_PLAN") val nineMobilePlan: NetworkDataPlanCategory = NetworkDataPlanCategory(),
) {
    fun parseAll(): List<NetworkDataPlanItem> {
        val plans = mutableListOf<NetworkDataPlanItem>()
        
        plans += mtnPlan.allItems
        plans += gloPlan.allItems
        plans += airtelPlan.allItems
        plans += nineMobilePlan.allItems

        return plans.toList()
    }
}

@Serializable
data class NetworkDataPlanCategory(
    @SerialName("ALL") val allItems: List<NetworkDataPlanItem> = listOf(),
    @SerialName("CORPORATE") val cgItems: List<NetworkDataPlanItem> = listOf(),
    @SerialName("SME") val smeItems: List<NetworkDataPlanItem> = listOf(),
    @SerialName("GIFTING") val giftingItems: List<NetworkDataPlanItem> = listOf(),
    @SerialName("SME2") val sme2Items: List<NetworkDataPlanItem> = listOf(),
)

@Serializable
data class NetworkDataPlanItem(
    @SerialName("id") val id: Int? = null,
    @SerialName("dataplan_id") val dataPlanId: String? = null,
    @SerialName("network") val network: Int? = null,
    @SerialName("plan_type") val planType: String? = null,
    @SerialName("plan_network") val planNetwork: String? = null,
    @SerialName("month_validate") val validity: String? = null,
    @SerialName("plan") val planSize: String? = null,
    @SerialName("plan_amount") val planAmount: String? = null,
    @SerialName("Affilliate_price") val affiliatePrice: Double? = null,
    @SerialName("TopUser_price") val topUserPrice: Double? = null,
)

@Serializable
data class NetworkDataCouponPlanItem(
    @SerialName("id") val id: Int? = null,
    @SerialName("dataplan_id") val dataPlanId: String? = null,
    @SerialName("network") val network: String? = null,
    @SerialName("plan_type") val planType: String? = null,
    @SerialName("plan_network") val planNetwork: String? = null,
    @SerialName("month_validate") val validity: String? = null,
    @SerialName("plan") val planSize: String? = null,
    @SerialName("plan_amount") val planAmount: String? = null,
    @SerialName("Affilliate_price") val affiliatePrice: Double? = null,
    @SerialName("TopUser_price") val topUserPrice: Double? = null,
)


@Serializable
data class NetworkCablePlansResponse(
    @SerialName("GOTVPLAN") val gotvPlans: List<NetworkCablePlanItem> = listOf(),
    @SerialName("DSTVPLAN") val dstvPlans: List<NetworkCablePlanItem> = listOf(),
    @SerialName("STARTIMEPLAN") val startimePlans: List<NetworkCablePlanItem> = listOf(),
    @SerialName("cablename") val metadata: List<NetworkCableMetadata> = listOf(),
) {
    fun parseAll(): List<NetworkCablePlanItem> {
        val plans = mutableListOf<NetworkCablePlanItem>()

        plans += gotvPlans
        plans += dstvPlans
        plans += startimePlans

        return plans.toList()
    }
}


@Serializable
data class NetworkCablePlanItem(
    @SerialName("id") val id: Int = 1,
    @SerialName("cableplan_id") val cablePlanId: String = "1",
    @SerialName("cable") val cableName: String = "",
    @SerialName("package") val cablePackage: String = "",
    @SerialName("plan_amount") val planAmount: String = "0.0",
)

@Serializable
data class NetworkCableMetadata(
    @SerialName("id") val id: Int = 1,
    @SerialName("name") val name: String = "",
)

@Serializable
data class NetworkRechargeResponse(
    @SerialName("mtn") val mtnItem: Int = 0,
    @SerialName("glo") val gloItem: Int = 0,
    @SerialName("airtel") val airtelItem: Int = 0,
    @SerialName("9mobile") val nineMobileItem: Int = 0,
    @SerialName("mtn_pin") val mtnPins: List<NetworkPinResponse> = listOf(),
    @SerialName("glo_pin") val gloPins: List<NetworkPinResponse> = listOf(),
    @SerialName("airtel_pin") val airtelPins: List<NetworkPinResponse> = listOf(),
    @SerialName("9mobile_pin") val nineMobilePins: List<NetworkPinResponse> = listOf(),
)

@Serializable
data class NetworkPinResponse(
    @SerialName("id") val id: Int? = null,
    @SerialName("network_name") val networkName: String? = null,
    @SerialName("amount") val amount: Double? = null,
    @SerialName("amount_to_pay") val amountToPay: Double? = null,
    @SerialName("Affilliate_price") val affiliatePrice: Double? = null,
    @SerialName("TopUser_price") val topUserPrice: Double? = null,
    @SerialName("api_price") val apiPrice: Double? = null,
)

@Serializable
data class NetworkResetTransactionPinResponse(
    @SerialName("message") val message: String,
)

@Serializable
data class NetworkResetPasswordResponse(
    @SerialName("status") val status: String,
)

@Serializable
data class NetworkFundWithCouponResponse(
    @SerialName("id") val id: Int,
    @SerialName("Code") val code: String,
    @SerialName("amount") val amount: Long,
    @SerialName("Status") val status: String,
    @SerialName("create_date") val dateCreated: String
)

@Serializable
data class NetworkTransferFundsResponse(
    @SerialName("receiver_username") val recipientUsername: String,
    @SerialName("amount") val amount: Long,
    @SerialName("id") val id: Int,
    @SerialName("ident") val reference: String,
    @SerialName("Status") val status: String,
    @SerialName("create_date") val dateCreated: String
)


@Serializable
data class NetworkSubscribeBillResponse(
    @SerialName("id") val id: Int = 1,
    @SerialName("ident") val reference: String,
    @SerialName("package") val discoName: String,
    @SerialName("disco_name") val discoId: Int,
    @SerialName("amount") val amount: String,
    @SerialName("Customer_Phone") val phone: String,
    @SerialName("meter_number") val meterNumber: String,
    @SerialName("token") val token: String,
    @SerialName("MeterType") val meterType: String,
    @SerialName("paid_amount") val paidAmount: String,
    @SerialName("balance_before") val balanceBefore: String,
    @SerialName("balance_after") val balanceAfter: String,
    @SerialName("Status") val status: String,
    @SerialName("create_date") val dateCreated: String,
    @SerialName("customer_name") val customerName: String,
    @SerialName("customer_address") val customerAddress: String,
)


@Serializable
data class NetworkResultCheckerResponse(
    @SerialName("exam_name") val examName: String,
    @SerialName("quantity") val quantity: Int,
    @SerialName("customer_jamb_profile_id") val jambProfileId: String? = null,
    @SerialName("data") val data: String? = null,
    @SerialName("id") val id: Int,
    @SerialName("Status") val status: String,
    @SerialName("previous_balance") val balanceBefore: String,
    @SerialName("after_balance") val balanceAfter: String,
    @SerialName("amount") val amount: Double,
    @SerialName("create_date") val dateCreated: String,
)


@Serializable
data class NetworkRechargeMeterHistory(
    @SerialName("count") val count: Long = 0,
    @SerialName("next") val next: String? = null,
    @SerialName("previous") val previous: String? = null,
    @SerialName("results") val results: List<NetworkRechargeMeterHistoryItem> = listOf(),
)


@Serializable
data class NetworkRechargeMeterHistoryItem(
    @SerialName("id") val id: Int,
    @SerialName("ident") val reference: String? = null,
    @SerialName("package") val discoName: String? = null,
    @SerialName("disco_name") val discoId: Int? = null,
    @SerialName("amount") val amount: String?= null,
    @SerialName("Customer_Phone") val phone: String? = null,
    @SerialName("meter_number") val meterNumber: String? = null,
    @SerialName("token") val token: String? = null,
    @SerialName("MeterType") val meterType: String? = null,
    @SerialName("paid_amount") val paidAmount: String? = null,
    @SerialName("balance_before") val balanceBefore: String? = null,
    @SerialName("balance_after") val balanceAfter: String? = null,
    @SerialName("Status") val status: String? = null,
    @SerialName("create_date") val dateCreated: String? = null,
    @SerialName("customer_name") val customerName: String? = null,
    @SerialName("customer_address") val customerAddress: String? = null,
)


@Serializable
data class NetworkResultCheckerHistoryResponse(
    @SerialName("count") val count: Long = 0,
    @SerialName("next") val next: String? = null,
    @SerialName("previous") val previous: String? = null,
    @SerialName("results") val results: List<NetworkResultCheckerHistoryItemResponse> = listOf()
)


@Serializable
data class NetworkResultCheckerHistoryItemResponse(
    @SerialName("exam_name") val examName: String? = null,
    @SerialName("quantity") val quantity: Int? = null,
    @SerialName("customer_jamb_profile_id") val jambProfileId: String? = null,
    @SerialName("pins") val pins: List<String>? = null,
    @SerialName("id") val id: Int,
    @SerialName("Status") val status: String? = null,
    @SerialName("previous_balance") val balanceBefore: String? = null,
    @SerialName("after_balance") val balanceAfter: String? = null,
    @SerialName("amount") val amount: Double? = null,
    @SerialName("create_date") val dateCreated: String? = null,
)



@Serializable
data class NetworkPrintCardResponse(
    @SerialName("Status") val status: String? = null,
    @SerialName("network") val network: Int? = null,
    @SerialName("network_amount") val networkAmount: Int? = null,
    @SerialName("name_on_card") val nameOnCard: String? = null,
    @SerialName("quantity") val quantity: Int? = null,
    @SerialName("data_pin") val dataPins: List<NetworkPrintCardPinResponse>? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("previous_balance") val balanceBefore: String? = null,
    @SerialName("after_balance") val balanceAfter: String? = null,
    @SerialName("amount") val amount: Double? = null,
    @SerialName("create_date") val dateCreated: String? = null,
)

@Serializable
data class NetworkPrintCardPinResponse(
    @SerialName("model") val model: String? = null,
    @SerialName("pk") val pk: Int? = null,
    @SerialName("fields") val field: NetworkPrintCardPinField? = null,
)

@Serializable
data class NetworkPrintCardPinField(
    @SerialName("network") val network: Int? = null,
    @SerialName("available") val isAvailable: Boolean? = null,
    @SerialName("amount") val amount: Double? = null,
    @SerialName("pin") val pin: String? = null,
    @SerialName("serial") val serial: String? = null,
    @SerialName("load_code") val loadCode: String? = null,
)


@Serializable
data class NetworkUpgradeUserResponse(
    @SerialName("message") val status: String
)


@Serializable
data class NetworkRegisterFCMTokenResponse(
    @SerialName("message") val message: String
)

@Serializable
data class NetworkPushNotificationResponse(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("body") val body: String,
    @SerialName("create_date") val dateCreated: String,
)


@Serializable
data class NetworkPushNotificationPagedResponse(
    @SerialName("count") val count: Int? = null,
    @SerialName("next") val next: String? = null,
    @SerialName("previous") val previous: String? = null,
    @SerialName("results") val results: List<NetworkPushNotificationResponse> = listOf()
)


@Serializable
data class NetworkInitMonnifyCardPaymentResponse(
    @SerialName("url") val checkoutUrl: String,
)

@Serializable
data class NetworkInitPaystackCardPaymentResponse(
    @SerialName("url") val checkoutUrl: String,
)


@Serializable
data class NetworkVerifyReservedAccountsResponse(
    @SerialName("status") val status: String,
    @SerialName("msg") val message: String,
)

@Serializable
data class NetworkReferralBonusWithdrawalResponse(
    @SerialName("amount") val amount: String,
)

@Serializable
data class NetworkWalletSummaryResponse(
    @SerialName("ident") val reference: String? = null,
    @SerialName("product") val product: String? = null,
    @SerialName("amount") val amount: String? = null,
    @SerialName("previous_balance") val balanceBefore: String? = null,
    @SerialName("after_balance") val balanceAfter: String? = null,
    @SerialName("create_date") val dateCreated: String? = null,
)

@Serializable
data class NetworkSendBulkSMSResponse(
    @SerialName("message") val message: String,
)

@Serializable
data class NetworkWalletSummaryHistoryResponse(
    @SerialName("count") val count: Long = 0,
    @SerialName("next") val next: String? = null,
    @SerialName("previous") val previous: String? = null,
    @SerialName("results") val results: List<NetworkWalletSummaryResponse> = listOf(),
)


@Serializable
data class NetworkBulkSMSHistoryResponse(
    @SerialName("count") val count: Long = 0,
    @SerialName("next") val next: String? = null,
    @SerialName("previous") val previous: String? = null,
    @SerialName("results") val results: List<NetworkBulkSMSHistoryItem> = listOf()
)


@Serializable
data class NetworkBulkSMSHistoryItem(
    @SerialName("total") val total: Long? = null,
    @SerialName("unit") val unit: Long? = null,
    @SerialName("sendername") val sender: String? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("page") val page: Double? = null,
    @SerialName("amount") val amount: Double? = null,
    @SerialName("to") val recipient: String? = null,
    @SerialName("ident") val reference: String? = null,
    @SerialName("create_date") val dateCreated: String? = null,
    @SerialName("DND") val dnd: Boolean? = null,
)

@Serializable
data class NetworkPrintCardHistoryResponse(
    @SerialName("count") val count: Long = 0,
    @SerialName("next") val next: String? = null,
    @SerialName("previous") val previous: String? = null,
    @SerialName("results") val results: List<NetworkPrintCardHistoryItem> = listOf()
)


@Serializable
data class NetworkPrintCardHistoryItem(
    @SerialName("Status") val status: String? = null,
    @SerialName("network") val network: Int? = null,
    @SerialName("network_amount") val networkAmount: Int? = null,
    @SerialName("name_on_card") val nameOnCard: String? = null,
    @SerialName("quantity") val quantity: Int? = null,
    @SerialName("data_pin") val dataPins: List<NetworkPrintCardPinResponse>? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("previous_balance") val balanceBefore: String? = null,
    @SerialName("after_balance") val balanceAfter: String? = null,
    @SerialName("amount") val amount: Double? = null,
    @SerialName("create_date") val dateCreated: String? = null,
)


@Serializable
data class NetworkDisabledService(
    @SerialName("service") val service: String? = null,
    @SerialName("disable") val isDisabled: Boolean? = null,
)

@Serializable
data class NetworkDisabledNetwork(
    @SerialName("name") val networkName: String? = null,
    @SerialName("airtime_disable") val isAirtimeDisabled: Boolean? = null,
    @SerialName("data_disable") val isDataDisabled: Boolean? = null,
    @SerialName("data_card_disable") val isDataCardDisabled: Boolean? = null,
    @SerialName("recharge_pin_disable") val isRechargePinDisabled: Boolean? = null,
    @SerialName("vtu_disable") val isVtuDisabled: Boolean? = null,
    @SerialName("share_and_sell_disable") val isShareAndSellDisabled: Boolean? = null,
)


@Serializable
data class NetworkAlertNotificationResponse(
    @SerialName("alert") val alert: String? = null
)

@Serializable
data class NetworkDisabledServiceResponse(
    @SerialName("services") val services: List<NetworkDisabledService>? = null,
    @SerialName("networks") val networks: List<NetworkDisabledNetwork>? = null,
)

@Serializable
data class NetworkAdvertisementItem(
    @SerialName("id") val id: Int? = null,
    @SerialName("image") val image: String? = null,
    @SerialName("description") val description: String? = null,
)