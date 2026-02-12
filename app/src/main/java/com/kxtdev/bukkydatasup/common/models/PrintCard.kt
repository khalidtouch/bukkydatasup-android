package com.kxtdev.bukkydatasup.common.models

import java.time.LocalDateTime

data class PrintCardRequest(
    val networkId: Int,
    val networkAmount: Int,
    val quantity: Int,
    val nameOnCard: String
)


data class PrintCardResponse(
    val status: String? = null,
    val network: Int? = null,
    val networkAmount: Int? = null,
    val nameOnCard: String? = null,
    val quantity: Int? = null,
    val dataPins: List<PrintCardPin>? = null,
    val id: Int? = null,
    val balanceBefore: String? = null,
    val balanceAfter: String? = null,
    val amount: Double? = null,
    val dateCreated: LocalDateTime? = null,
)

data class PrintCardPin(
    val model: String? = null,
    val pk: Int? = null,
    val field: PrintCardPinField? = null,
)

data class PrintCardPinField(
    val network: Int? = null,
    val isAvailable: Boolean? = null,
    val amount: Double? = null,
    val pin: String? = null,
    val serial: String? = null,
    val loadCode: String? = null
)


fun List<PrintCardPin>.getPins(): List<String> {
    return this.map { pins -> pins.field?.pin.orEmpty() }
}

data class PrintCardHistoryItem(
    val status: String? = null,
    val network: Int? = null,
    val networkAmount: Int? = null,
    val nameOnCard: String? = null,
    val quantity: Int? = null,
    private val dataPins: String? = null,
    val id: Int? = null,
    val balanceBefore: String? = null,
    val balanceAfter: String? = null,
    val amount: Double? = null,
    val dateCreated: LocalDateTime? = null,
) {
    val pins: List<String> get() {
        val generatedPins = mutableListOf<String>()
        generatedPins += dataPins?.split(",") ?: listOf("")
        return generatedPins
    }
}


data class PrintCardHistoryResponse(
    val count: Long,
    val next: String?,
    val prev: String?,
    val results: List<PrintCardHistoryItem>
)