package com.kxtdev.bukkydatasup.common.models

import java.time.LocalDateTime

data class BuyDataRequest(
    val network: Int,
    val phone: String,
    val plan: Int,
    val isPorted: Boolean,
)

data class BuyDataResponse(
    val id: Int,
    val network: Int,
    val reference: String,
    val balanceBefore: String,
    val balanceAfter: String,
    val phone: String,
    val planId: Int,
    val status: String,
    val apiResponse: String,
    val planNetwork: String,
    val planName: String,
    val planAmount: String,
    val dateCreated: LocalDateTime,
    val isPorted: Boolean,
)

data class BuyDataHistoryItemResponse(
    val id: Int,
    val network: Int,
    val reference: String,
    val balanceBefore: String,
    val balanceAfter: String,
    val phone: String,
    val planId: Int,
    val status: String,
    val apiResponse: String,
    val planNetwork: String,
    val planName: String,
    val planAmount: String,
    val dateCreated: LocalDateTime,
    val isPorted: Boolean,
)

data class BuyDataHistoryResponse(
    val count: Long,
    val next: String?,
    val prev: String?,
    val results: List<BuyDataHistoryItemResponse>
)

data class DataBucketItem(
    val title: String,
    val value: String,
)