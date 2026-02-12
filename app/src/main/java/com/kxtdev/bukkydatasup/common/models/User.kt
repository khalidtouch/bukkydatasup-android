package com.kxtdev.bukkydatasup.common.models

import android.content.Context
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.AirtimeType
import com.kxtdev.bukkydatasup.common.enums.CableTV
import com.kxtdev.bukkydatasup.common.enums.DataPlanType
import com.kxtdev.bukkydatasup.common.enums.ExamType
import com.kxtdev.bukkydatasup.common.enums.Network
import com.kxtdev.bukkydatasup.common.enums.UserType
import com.kxtdev.bukkydatasup.common.utils.asMoney
import com.kxtdev.bukkydatasup.ui.design.PoshIcon


data class UserProfile(
    val user: UserInfoResponse? = null,
    val verification: Verification? = null,
    val notification: NotificationResponse? = null,
    val percentage: PercentageResponse? = null,
    val topUpPercentage: TopUpPercentageResponse? = null,
    val adminNumbers: List<AdminNumberResponse>? = null,
    val exams: ExamsResponse? = null,
    val banks: List<BankResponse>? = null,
    val banners: List<Banner>? = null,
    val dataPlans: DataPlans? = null,
    val cablePlans: CablePlansResponse? = null,
    val supportPhoneNumber: String? = null,
    val groupLink: String? = null,
    val affiliateUpgradeFee: Double? = null,
    val topUserUpgradeFee: Double? = null,
    val recharge: RechargeResponse? = null,
)

data class UserInfoResponse(
    val id: Int? = null,
    val email: String? = null,
    val username: String? = null,
    val fullname: String? = null,
    val pin: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val userType: String? = null,
    val isEmailVerified: Boolean? = null,
    val isAccountVerified: Boolean? = null,
    val passwordHash: String? = null,
    val accountBalance: Double? = null,
    val walletBalance: String? = null,
    val bonusBalance: String? = null,
    val referrerUsername: String? = null,
    val userBanks: List<UserBankResponse>? = null,
    val reservedAccountNumber: String? = null,
    val reservedBankName: String? = null,
)

data class Banner(
    val banner: String? = null,
    val route: String? = null,
)

data class Verification(
    val bvnVerified: Boolean? = null,
    val ninVerified: Boolean? = null,
)

data class NotificationResponse(
    val message: String
)

data class PercentageResponse(
    private val mtn: PercentageItemResponse,
    private val glo: PercentageItemResponse,
    private val nineMobile: PercentageItemResponse,
    private val airtel: PercentageItemResponse,
) {
    fun getAirtimeSwapPercentage(network: Network): Double? {
        return when(network) {
            Network.MTN -> mtn.percentage
            Network.GLO -> glo.percentage
            Network.AIRTEL -> airtel.percentage
            Network.NINE_MOBILE -> nineMobile.percentage
        }
    }
}

data class PercentageItemResponse(
    val percentage: Double? = null,
    val phone: String? = null,
)

data class AdminNumberResponse(
    val network: String,
    val phone: String,
)

data class ExamsResponse(
    val waec: ExamItem? = null,
    val neco: ExamItem? = null,
    val nabteb: ExamItem? = null,
) {
    fun getAmountPerUnit(examType: ExamType): Double {
        return when(examType) {
            ExamType.NECO -> this.neco?.amount ?: 0.0
            ExamType.NABTEB -> this.nabteb?.amount ?: 0.0
            ExamType.WAEC -> this.waec?.amount ?: 0.0
            else -> 0.0
        }
    }
}

data class ExamItem(
    val amount: Double,
)

data class BankResponse(
    val bankName: String,
    val accountName: String,
    val accountNumber: String,
)

data class DataPlans(
    val mtnPlan: DataPlanCategory = DataPlanCategory(),
    val gloPlan: DataPlanCategory = DataPlanCategory(),
    val airtelPlan: DataPlanCategory = DataPlanCategory(),
    val nineMobilePlan: DataPlanCategory = DataPlanCategory(),
) {
    fun retrieveDataPlans(network: Network, dataPlanType: DataPlanType): List<DataPlanItem> {
        val plans = mutableListOf<DataPlanItem>()
        plans += when(network) {
            Network.MTN -> {
                this.mtnPlan.allItems
                    .filter { it.planType == dataPlanType.title }
            }
            Network.GLO -> {
                this.gloPlan.allItems
                    .filter { it.planType == dataPlanType.title }
            }
            Network.AIRTEL -> {
                this.airtelPlan.allItems
                    .filter { it.planType == dataPlanType.title }
            }
            Network.NINE_MOBILE -> {
                this.nineMobilePlan.allItems
                    .filter { it.planType == dataPlanType.title }
            }
        }
        return plans.toList()
    }

    fun retrieveDataPlanTypes(network: Network): List<DataPlanType> {
        val planTypes = mutableListOf<DataPlanType>()
        val plans = mutableListOf<DataPlanItem>()
        plans += when(network) {
            Network.MTN -> {
                this.mtnPlan.allItems
            }

            Network.GLO -> {
                this.gloPlan.allItems
            }

            Network.AIRTEL -> {
                this.airtelPlan.allItems
            }

            Network.NINE_MOBILE -> {
                this.nineMobilePlan.allItems
            }
        }
        planTypes += plans
            .asSequence()
            .filter { it.planType?.isNotBlank() == true }
            .map { it.planType }
            .toSet()
            .map { plan -> DataPlanType.getByTitle(plan.orEmpty())!! }
            .toSet()
        return planTypes.toList()
    }
}

data class CablePlansResponse(
    val gotvPlans: List<CablePlanItem> = listOf(),
    val dstvPlans: List<CablePlanItem> = listOf(),
    val startimePlans: List<CablePlanItem> = listOf(),
    val metadata: List<CableMetadata> = listOf(),
) {
    fun retrieveCablePlans(cable: CableTV): List<CablePlanItem> {
        val plans = mutableListOf<CablePlanItem>()
        plans += when(cable) {
            CableTV.GOTV -> gotvPlans
            CableTV.DSTV -> dstvPlans
            CableTV.STARTIME -> startimePlans
        }
        return plans.toList()
    }
}

data class RechargeResponse(
    val mtnItem: Int,
    val gloItem: Int,
    val airtelItem: Int,
    val nineMobileItem: Int,
    val mtnPins: List<PinResponse>,
    val gloPins: List<PinResponse>,
    val airtelPins: List<PinResponse>,
    val nineMobilePins: List<PinResponse>,
) {
    fun getRechargePins(network: Network): List<PinResponse> {
        return when(network) {
            Network.MTN -> mtnPins
            Network.GLO -> gloPins
            Network.AIRTEL -> airtelPins
            Network.NINE_MOBILE -> nineMobilePins
        }
    }

    fun getByAmount(amount: Double, network: Network): PinResponse? {
        return getRechargePins(network)
            .find { it.amount == amount }
    }
}

fun List<PinResponse>.expressAsStrings(context: Context): List<String> {
    return this.map { context.getString(R.string.money, it.amount.toString().asMoney()) }
}


data class UserBankResponse(
    val bankCode: String? = null,
    val bankName: String? = null,
    val accountNumber: String? = null,
    val accountName: String? = null,
    val trackingReference: String? = null,
)

data class CablePlanItem(
    val id: Int? = null,
    val cablePlanId: String? = null,
    val cableName: String? = null,
    val cablePackage: String? = null,
    val planAmount: String? = null,
)

data class CableMetadata(
    val id: Int,
    val name: String,
)

data class PinResponse(
    val id: Int?,
    val networkName: String?,
    val amount: Double?,
    val amountToPay: Double?,
    val affiliatePrice: Double?,
    val topUserPrice: Double?,
    val apiPrice: Double?,
) {
    fun expressAsString(context: Context): String {
        return context.getString(R.string.money, amount.toString().asMoney())
    }

    companion object {
        val Empty: PinResponse get() {
            return PinResponse(
                id = null,
                networkName = null,
                amount = null,
                amountToPay = null,
                affiliatePrice = null,
                topUserPrice = null,
                apiPrice = null
            )
        }
    }
}

data class DataPlanCategory(
    val allItems: List<DataPlanItem> = listOf(),
    val cgItems: List<DataPlanItem> = listOf(),
    val smeItems: List<DataPlanItem> = listOf(),
    val giftingItems: List<DataPlanItem> = listOf(),
    val sme2Items: List<DataPlanItem> = listOf(),
)

data class DataPlanItem(
    val id: Int? = null,
    val dataPlanId: String? = null,
    val network: Int? = null,
    val planType: String? = null,
    val planNetwork: String? = null,
    val validity: String? = null,
    val planSize: String? = null,
    val planAmount: String? = null,
)

data class TopUpPercentageResponse(
    val mtn: TopUpPercentageItemResponse,
    val glo: TopUpPercentageItemResponse,
    val nineMobile: TopUpPercentageItemResponse,
    val airtel: TopUpPercentageItemResponse,
) {
    fun getTopUpPercentage(network: Network, airtimeType: AirtimeType): Double {
        return when(network) {
            Network.MTN -> {
                if(airtimeType == AirtimeType.SHARE_SELL) mtn.shareNSell else mtn.vtu
            }
            Network.GLO -> {
                if(airtimeType == AirtimeType.SHARE_SELL) glo.shareNSell else glo.vtu
            }
            Network.AIRTEL -> {
                if(airtimeType == AirtimeType.SHARE_SELL) airtel.shareNSell else airtel.vtu
            }
            Network.NINE_MOBILE -> {
                if(airtimeType == AirtimeType.SHARE_SELL) nineMobile.shareNSell else nineMobile.vtu
            }
        }
    }
}

data class TopUpPercentageItemResponse(
    val vtu: Double,
    val shareNSell: Double,
)

data class ResetTransactionPinResponse(
    val message: String
)

data class ResetPasswordRequest(
    val oldPassword: String,
    val newPassword1: String,
    val newPassword2: String
)

data class ResetPasswordResponse(
    val status: String
)

data class UpgradeUserResponse(
    val status: String,
)

data class RegisterFCMTokenRequest(
    val token: String
)

data class RegisterFCMTokenResponse(
    val message: String
)

data class VerifyReservedAccountsRequest(
    val method: String? = null,
    val fullname: String? = null,
    val bvn: String? = null,
    val nin: String? = null,
)

data class VerifyReservedAccountsResponse(
    val status: String,
    val message: String
)

fun List<DataPlanItem>.retrieveDataPlanTypes(network: Network): List<DataPlanType> {
    val planTypes = mutableListOf<DataPlanType>()
    planTypes += this
        .asSequence()
        .filter { it.planType?.isNotBlank() == true }
        .map { it.planType }
        .toSet()
        .map { plan -> DataPlanType.getByTitle(plan.orEmpty())!! }
        .toSet()
        .sortedBy { plan -> plan.order }

    return planTypes.toList()
}