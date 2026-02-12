package com.kxtdev.bukkydatasup.common.models

import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.common.enums.Status
import java.time.LocalDateTime

data class HistoryListItem(
    val id: Int?,
    val icon: Int?,
    val timestamp: LocalDateTime?,
    val product: Product?,
    val status: Status?,
    val amount: Double?,
    val recipient: String?
)