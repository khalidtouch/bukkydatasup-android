package com.kxtdev.bukkydatasup.common.models

import java.time.LocalDateTime


data class TransferFundsRequest(
    val amount: Double,
    val recipientUsername: String,
)

data class TransferFundsResponse(
    val recipientUsername: String,
    val amount: Double,
    val id: Int,
    val reference: String,
    val status: String,
    val dateCreated: LocalDateTime
)