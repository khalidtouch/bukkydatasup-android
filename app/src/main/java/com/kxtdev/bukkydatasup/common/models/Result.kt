package com.kxtdev.bukkydatasup.common.models

import java.time.LocalDateTime


data class ResultCheckerRequest(
    val examName: String,
    val quantity: Int
)

data class ResultCheckerResponse(
    val examName: String,
    val quantity: Int,
    val jambProfileId: String?,
    val data: String?,
    val id: Int,
    val status: String,
    val balanceBefore: Double,
    val balanceAfter: Double,
    val amount: Double,
    val dateCreated: LocalDateTime
)


data class ResultCheckerHistory(
    val count: Long,
    val next: String?,
    val prev: String?,
    val results: List<ResultCheckerHistoryItemResponse>
)

data class ResultCheckerHistoryItemResponse(
    val examName: String? = null,
    val quantity: Int? = null,
    val jambProfileId: String? = null,
    val pins: List<String>? = null,
    val id: Int,
    val status: String? = null,
    val balanceBefore: Double? = null,
    val balanceAfter: Double? = null,
    val amount: Double? = null,
    val dateCreated: LocalDateTime? = null,
)


