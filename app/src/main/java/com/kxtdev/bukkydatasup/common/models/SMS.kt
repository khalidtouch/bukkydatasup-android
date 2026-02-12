package com.kxtdev.bukkydatasup.common.models

import java.time.LocalDateTime

data class SendBulkSMSRequest(
    val recipient: String,
    val message: String,
    val sender: String,
    val dnd: Boolean,
)

data class SendBulkSMSResponse(
    val message: String
)

data class BulkSMSHistoryItem(
    val total: Long? = null,
    val unit: Long? = null,
    val sender: String? = null,
    val message: String? = null,
    val page: Double? = null,
    val amount: Double? = null,
    val recipient: String? = null,
    val reference: String? = null,
    val dateCreated: LocalDateTime? = null,
    val dnd: Boolean? = null,
)

data class BulkSMSHistoryResponse(
    val count: Long,
    val next: String?,
    val prev: String?,
    val results: List<BulkSMSHistoryItem>
)
