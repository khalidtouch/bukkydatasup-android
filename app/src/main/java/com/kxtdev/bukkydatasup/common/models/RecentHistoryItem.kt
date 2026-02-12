package com.kxtdev.bukkydatasup.common.models

import com.kxtdev.bukkydatasup.common.enums.CashFlow
import com.kxtdev.bukkydatasup.common.enums.Product
import java.time.LocalDateTime

data class RecentHistoryItem(
    val product: Product,
    val label: String,
    val cashFlow: CashFlow,
    val timestamp: LocalDateTime
)