package com.kxtdev.bukkydatasup.common.models

import com.kxtdev.bukkydatasup.common.enums.Status
import java.time.LocalDateTime

data class ValidateMeterResponse(
    val isInvalid: Boolean,
    val name: String,
    val address: String,
)

data class SubscribeBillResponse(
    val id: Int,
    val reference: String,
    val discoName: String,
    val discoId: Int,
    val amount: Double,
    val phone: String,
    val meterNumber: String,
    val token: String,
    val meterType: String,
    val paidAmount: Double,
    val balanceBefore: Double,
    val balanceAfter: Double,
    val status: String,
    val dateCreated: LocalDateTime,
    val customerName: String,
    val address: String,
)

data class SubscribeBillRequest(
    val discoId: Int,
    val amount: Double,
    val meterNumber: String,
    val meterType: String,
    val phone: String,
    val customerName: String,
    val customerAddress: String
)

data class RechargeMeterHistoryItem(
    val id: Int,
    val reference: String?,
    val discoName: String?,
    val discoId: Int?,
    val amount: Double?,
    val phone: String?,
    val meterNumber: String?,
    val token: String?,
    val meterType: String?,
    val paidAmount: Double?,
    val balanceBefore: Double?,
    val balanceAfter: Double?,
    val status: String?,
    val dateCreated: LocalDateTime?,
    val customerName: String?,
    val customerAddress: String?
)

data class RechargeMeterHistory(
    val count: Long,
    val next: String?,
    val prev: String?,
    val results: List<RechargeMeterHistoryItem>
)

data class TransactionStatus(
    val header: String = "",
    val description: String = "",
    val status: Status = Status.PROCESSING,
    val itemId: Int? = null,
) {
    val canShowReceipt: Boolean get() {
        return itemId != null 
    }
}
