package com.kxtdev.bukkydatasup.common.models

import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.common.enums.Status
import java.time.LocalDateTime

data class HistoryDetailItem(
    val id: Int? = null,
    val recipient: String? = null,
    val providerName: String? = null,
    val amount: Double? = null,
    val paidAmount: Double? = null,
    val status: Status? = null,
    val apiResponse: String? = null,
    val product: Product? = null,
    val reference: String? = null,
    val timestamp: LocalDateTime? = null
)