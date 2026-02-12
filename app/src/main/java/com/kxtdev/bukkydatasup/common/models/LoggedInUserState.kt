package com.kxtdev.bukkydatasup.common.models

import com.kxtdev.bukkydatasup.BuildConfig
import com.kxtdev.bukkydatasup.common.enums.Network
import com.kxtdev.bukkydatasup.common.enums.UserType
import com.kxtdev.bukkydatasup.common.utils.toHandledDouble

data class LoggedInUserState(
    private val username: String? = null,
    private val fullname: String? = null,
    private val phone: String? = null,
    private val bankName: String? = null,
    private val email: String? = null,
    private val isEmailVerified: Boolean? = null,
    private val accountNumber: String? = null,
    private val walletBalance: String? = null,
    private val accountBalance: Double? = null,
    private val referralBonus: String? = null,
    private val pin: String? = null,
    private val userId: Int? = null,
    private val address: String? = null,
    private val userType: String? = null,
    private val isAccountVerified: Boolean? = null,
    val isLoading: Boolean? = null,
    val error: Throwable? = null,
    private val topUpPercentage: TopUpPercentageResponse? = null,
    private val rawDataPlans: DataPlans? = null,
    private val rawCablePlans: CablePlansResponse? = null,
    private val banks: List<UserBankResponse>? = null,
    private val supportPhoneNumber: String? = null,
    private val groupLink: String? = null,
    private val affiliateUpgradeFee: Double? = null,
    private val topUserUpgradeFee: Double? = null,
    private val examResponse: ExamsResponse? = null,
    private val rechargeResponse: RechargeResponse? = null,
    private val verification: Verification? = null,
    private val percentage: PercentageResponse? = null,
) {

    val userConfig: UserConfig get() {
        return UserConfig(
            supportPhoneNumber = supportPhoneNumber,
            groupLink = groupLink,
            affiliateUpgradeFee = affiliateUpgradeFee,
            topUserUpgradeFee = topUserUpgradeFee,
            topUpPercentage = topUpPercentage,
            examResponse = examResponse,
            rechargeResponse = rechargeResponse,
            walletBalance = walletBalance?.toHandledDouble(),
            bonusBalance = referralBonus?.toHandledDouble(),
            username = username,
            phone = phone,
        )
    }

    fun getAirtimeSwapPercentage(network: Network): Double? {
        return percentage?.getAirtimeSwapPercentage(network)
    }

    val isUserVerified: Boolean? get() {
        if(verification == null) return null
        if(verification.ninVerified == null && verification.bvnVerified == null) return null
        if(verification.ninVerified == true || verification.bvnVerified == true) return true
        return false
    }

    val profile: ProfileItem get() {

        return ProfileItem(
            username = username,
            isAccountVerified = isAccountVerified,
            email = email,
            isEmailVerified = isEmailVerified,
            fullname = fullname,
            phone = phone,
            userType = userType,
            address = address,
            bonusBalance = referralBonus?.toDouble(),
        )
    }

    val referralLink: String get() {
        return "${BuildConfig.BASE_URL}/signup/?referal=${username.orEmpty()}"
    }

    val isTransactionPinNotConfigured: Boolean get() {
        return !username.isNullOrBlank() && pin.isNullOrBlank()

    }

    val user: UserType
        get() {
        return UserType.getUserType(userType)
    }

}


