package com.kxtdev.bukkydatasup.common.models

import java.time.LocalDateTime

data class FundWithCouponRequest(
    val code: String
)

data class FundWithCouponResponse(
    val id: Int,
    val code: String,
    val amount: Double,
    val status: String,
    val dateCreated: LocalDateTime
)