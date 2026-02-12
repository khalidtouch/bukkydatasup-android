package com.kxtdev.bukkydatasup.common.models

import com.kxtdev.bukkydatasup.common.enums.AuthState
import com.kxtdev.bukkydatasup.common.enums.FundOption
import com.kxtdev.bukkydatasup.common.enums.PreferenceItem
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.common.utils.equivalent

data class AppUiState(
    val product: Product = Product.AIRTIME,
    val selectedHistoryProduct: Product? = Product.AIRTIME,
    val authState: AuthState = AuthState.SPLASH_SCREEN,
    val preference: PreferenceItem = PreferenceItem.THEMES,
    val selectedBottomNavItemIndex: Int = 0,
    val selectedFundOption: FundOption? = null,
    val hasInitialized: Boolean? = null,
    val disabledServices: List<DisabledService>? = null,
    val alertNotification: AlertNotification? = null,
    val disabledNetworks: List<DisabledNetwork>? = null,
) {
    val isAirtimeDisabled: Boolean? get() {
       return isServiceDisabled("airtime.json")
    }

    val isDataDisabled: Boolean? get() {
        return isServiceDisabled("data")
    }

    val isCableDisabled: Boolean? get() {
        return isServiceDisabled("cablesub")
    }

    val isBillDisabled: Boolean? get() {
        return isServiceDisabled("bill")
    }

    val isBankPaymentDisabled: Boolean? get() {
        return isServiceDisabled("bankpayment")
    }

    val isMonnifyATMDisabled: Boolean? get() {
        return isServiceDisabled("monnify atm")
    }

    val isMonnifyBankDisabled: Boolean? get() {
        return isServiceDisabled("monnfy bank")
    }

    val isPaystackDisabled: Boolean? get() {
        return isServiceDisabled("paystack")
    }

    val isAirtimeFundingDisabled: Boolean? get() {
        return isServiceDisabled("airtime_funding")
    }

    val isBulkSMSDisabled: Boolean? get() {
        return isServiceDisabled("bulk sms")
    }

    val isRechargeCardPrintingDisabled: Boolean? get() {
        return isServiceDisabled("recharge_printing")
    }

    val isResultCheckerDisabled: Boolean? get() {
        return isServiceDisabled("result_checker")
    }

    fun getProductDisabledState(product: Product): Boolean? {
        return when(product) {
            Product.AIRTIME -> isAirtimeDisabled
            Product.DATA -> isDataDisabled
            Product.CABLE -> isCableDisabled
            Product.ELECTRICITY -> isBillDisabled
            Product.BULK_SMS -> isBulkSMSDisabled
            Product.PRINT_CARD -> isRechargeCardPrintingDisabled
            Product.RESULT_CHECKER -> isResultCheckerDisabled
            Product.TRANSFER -> false
            Product.WHATSAPP_GROUP -> false
            Product.AIRTIME_SWAP -> isAirtimeFundingDisabled
            Product.REFER_FRIEND -> false
            Product.WALLET_HISTORY -> false
        }
    }

    fun getFundingDisabledState(fundOption: FundOption): Boolean? {
        return when(fundOption) {
            FundOption.FUND_WITH_MONNIFY_CARD -> isMonnifyATMDisabled
            FundOption.FUND_FROM_BONUS -> false
            FundOption.FUND_WITH_COUPON -> false
        }
    }


    private fun isServiceDisabled(name: String): Boolean? {
        if (disabledServices == null ) return null
        val service = disabledServices.find { it.service?.equivalent(name) == true }
            ?: return null
        return service.isDisabled
    }
}