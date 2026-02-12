package com.kxtdev.bukkydatasup.common.models

import java.time.LocalDateTime


class RechargeAirtimeRequest(
    val network: Int,
    val phone: String,
    val amount: Double,
    val airtimeType: String,
    val isPorted: Boolean,
)

class RechargeAirtimeResponse(
    val id: Int,
    val airtimeType: String,
    val network: Int,
    val reference: String,
    val phone: String,
    val amount: String,
    val planAmount: String,
    val planNetwork: String,
    val paidAmount: String,
    val balanceBefore: String,
    val balanceAfter: String,
    val status: String,
    val dateCreated: LocalDateTime,
    val customerName: String? = null,
    val isPorted: Boolean? = null,
)

class RechargeAirtimeHistoryItemResponse(
    val id: Int? = null,
    val airtimeType: String? = null,
    val network: Int? = null,
    val reference: String? = null,
    val phone: String? = null,
    val amount: String? = null,
    val planAmount: String? = null,
    val planNetwork: String? = null,
    val paidAmount: String? = null,
    val balanceBefore: String? = null,
    val balanceAfter: String? = null,
    val status: String? = null,
    val dateCreated: LocalDateTime? = null,
    val isPorted: Boolean? = null,
)

class RechargeAirtimeHistory(
    val count: Long,
    val next: String?,
    val prev: String?,
    val results: List<RechargeAirtimeHistoryItemResponse>
)