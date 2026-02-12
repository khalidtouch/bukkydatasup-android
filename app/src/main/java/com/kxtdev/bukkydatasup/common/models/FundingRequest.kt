package com.kxtdev.bukkydatasup.common.models

import com.kxtdev.bukkydatasup.common.enums.FundOption

data class FundingRequest(
    val code: String? = null,
    val amount: String? = null,
    val fundOption: FundOption? = null,
) {
    val isValid: Boolean get() {
        return when(fundOption) {
            FundOption.FUND_WITH_COUPON -> {
                !code.isNullOrBlank()
            }
            FundOption.FUND_WITH_MONNIFY_CARD,
            FundOption.FUND_FROM_BONUS -> {
                !amount.isNullOrBlank()
            }
            else -> false
        }
    }
}