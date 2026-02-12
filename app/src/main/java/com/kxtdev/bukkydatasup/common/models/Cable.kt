package com.kxtdev.bukkydatasup.common.models

import java.time.LocalDateTime


data class SubscribeCableTVRequest(
    val cableId: Int,
    val planId: Int,
    val smartCardNumber: String
)

data class SubscribeCableTVResponse(
    val id: Int,
    val reference: String,
    val cableId: Int,
    val planId: Int,
    val packageName: String,
    val planAmount: String,
    val paidAmount: Double,
    val smartCardNumber: String,
    val balanceBefore: String,
    val balanceAfter: String,
    val status: String,
    val dateCreated: LocalDateTime,
    val customerName: String,
)

data class ValidateSmartCardNumberResponse(
    val isInvalid: Boolean,
    val name: String,
)

data class SubscribeCableTVHistoryItem(
    val id: Int,
    val reference: String,
    val cableId: Int,
    val planId: Int,
    val packageName: String,
    val planAmount: String,
    val paidAmount: String,
    val smartCardNumber: String,
    val balanceBefore: String,
    val balanceAfter: String,
    val status: String,
    val dateCreated: LocalDateTime,
    val customerName: String,
)

data class SubscribeCableTVHistory(
    val count: Long,
    val next: String?,
    val prev: String?,
    val results: List<SubscribeCableTVHistoryItem>
)