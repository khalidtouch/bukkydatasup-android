package com.kxtdev.bukkydatasup.common.models

import android.content.Context
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.Product


data class HistoryUiState(
    val error: Throwable? = null,
    val loadingMessage: String? = null,
    val isLoading: Boolean? = null,
    val isFilterExpanded: Boolean? = null,
    private val product: Product? = null,
    val historyDetailItem: HistoryDetailItem? = null,
) {

    val selectedProduct: Product
        get() {
            return product ?: Product.WALLET_HISTORY
        }

    fun getEmptyMessage(context: Context): String? {
        return when (product) {
            Product.AIRTIME -> context.getString(R.string.no_airtime_topup_records)
            Product.DATA -> context.getString(R.string.no_data_subscription_records)
            Product.CABLE -> context.getString(R.string.no_cable_subscription_records)
            Product.RESULT_CHECKER -> context.getString(R.string.no_exam_pin_history_records)
            Product.WALLET_HISTORY -> context.getString(R.string.no_wallet_transaction_records)
            Product.ELECTRICITY -> context.getString(R.string.no_bill_payment_records)
            else -> null
        }
    }
}
