package com.kxtdev.bukkydatasup.common.models

import android.content.Context
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.Product

data class UserConfig(
    val supportPhoneNumber: String? = null,
    val groupLink: String? = null,
    val affiliateUpgradeFee: Double? = null,
    val topUserUpgradeFee: Double? = null,
    val topUpPercentage: TopUpPercentageResponse? = null,
    val examResponse: ExamsResponse? = null,
    val rechargeResponse: RechargeResponse? = null,
    val walletBalance: Double? = null,
    val bonusBalance: Double? = null,
    val username: String? = null,
    val phone: String? = null,
) {
    fun getBalance(context: Context, product: Product): Pair<Double?,String> {
        return if(product == Product.REFER_FRIEND)
            Pair(bonusBalance, context.getString(R.string.bonus_balance))
        else
            Pair(walletBalance, context.getString(R.string.wallet_balance))
    }

}