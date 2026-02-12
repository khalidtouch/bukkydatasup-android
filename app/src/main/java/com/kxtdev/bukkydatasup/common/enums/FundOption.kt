package com.kxtdev.bukkydatasup.common.enums

enum class FundOption(val id: Int, val title: String) {
    FUND_WITH_COUPON(1, "Fund With Coupon"),
    FUND_WITH_MONNIFY_CARD(2, "Fund With ATM"),
    FUND_FROM_BONUS(3, "Fund From Bonus");

    companion object {
        fun getAvailableOptions(): List<FundOption> {
            return entries
                .filter { it != FUND_WITH_MONNIFY_CARD && it != FUND_WITH_COUPON }
                .toTypedArray()
                .toList()
        }

        fun getById(id: Int): FundOption {
            return entries
                .find { it.id == id } ?: FUND_WITH_COUPON
        }
    }
}