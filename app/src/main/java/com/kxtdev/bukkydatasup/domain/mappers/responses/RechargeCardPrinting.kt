package com.kxtdev.bukkydatasup.domain.mappers.responses

import com.kxtdev.bukkydatasup.common.database.models.RoomPrintCardHistoryItem
import com.kxtdev.bukkydatasup.common.models.PrintCardPin
import com.kxtdev.bukkydatasup.common.models.PrintCardPinField
import com.kxtdev.bukkydatasup.common.models.PrintCardResponse
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.getTotalPages
import com.kxtdev.bukkydatasup.common.utils.toDatetime
import com.kxtdev.bukkydatasup.network.models.NetworkPrintCardHistoryItem
import com.kxtdev.bukkydatasup.network.models.NetworkPrintCardHistoryResponse
import com.kxtdev.bukkydatasup.network.models.NetworkPrintCardPinField
import com.kxtdev.bukkydatasup.network.models.NetworkPrintCardPinResponse
import com.kxtdev.bukkydatasup.network.models.NetworkPrintCardResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult



fun NetworkPrintCardResponse.convertToLocalPrintCard(): PrintCardResponse {
    return PrintCardResponse(
        status = status,
        network = this.network,
        networkAmount = this.networkAmount,
        nameOnCard = this.nameOnCard,
        quantity = this.quantity,
        dataPins = this.dataPins?.toPrintCardPins(),
        id = this.id,
        balanceBefore = this.balanceBefore,
        balanceAfter = this.balanceAfter,
        amount = this.amount,
        dateCreated = this.dateCreated?.toDatetime()
    )
}

fun List<NetworkPrintCardPinResponse>.toPrintCardPins(): List<PrintCardPin> {
    return this.map { pin -> pin.convertToLocalPrintCardPin() }
}

fun List<NetworkPrintCardPinResponse>.toStringedPrintCardPins(): String? {
    val pins = this.map { pin -> pin.convertToLocalPrintCardPin() }
    val rawPins = pins.map { rawPin -> rawPin.field?.pin }
    return rawPins.joinToString(",")
}

fun NetworkPrintCardPinResponse.convertToLocalPrintCardPin(): PrintCardPin {
    return PrintCardPin(
        model = this.model,
        pk = this.pk,
        field = this.field?.convertToLocalPrintCardPinField()
    )
}

fun NetworkPrintCardPinField.convertToLocalPrintCardPinField(): PrintCardPinField {
    return PrintCardPinField(
        network = this.network,
        isAvailable = this.isAvailable,
        amount = this.amount,
        pin = this.pin,
        serial = this.serial,
        loadCode = this.loadCode,
    )
}

fun NetworkResult<NetworkPrintCardResponse>.convertToLocalPrintCardResponse():
        NetworkResult<PrintCardResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalPrintCard())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalPrintCard())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}

fun NetworkPrintCardHistoryItem.toRoomPrintCardHistoryItem(): RoomPrintCardHistoryItem {
    return RoomPrintCardHistoryItem(
        status = this.status,
        network = this.network,
        networkAmount = this.networkAmount,
        nameOnCard = this.nameOnCard,
        quantity = this.quantity,
        dataPins = this.dataPins?.toStringedPrintCardPins(),
        id = this.id ?: 1,
        balanceBefore = this.balanceBefore,
        balanceAfter = this.balanceAfter,
        amount = this.amount,
        dateCreated = this.dateCreated,
    )
}

fun List<NetworkPrintCardHistoryItem>.toRoomPrintCardHistoryItems(): List<RoomPrintCardHistoryItem> {
    return this.map { it.toRoomPrintCardHistoryItem() }
}


fun NetworkResult<NetworkPrintCardHistoryResponse>.getPrintCardHistoryItems():
        List<RoomPrintCardHistoryItem> {
    return when(this) {
        is NetworkResult.Success -> this.data.results.toRoomPrintCardHistoryItems()
        is NetworkResult.Failed -> listOf()
        is NetworkResult.Error -> listOf()
    }
}

fun NetworkResult<NetworkPrintCardHistoryResponse>.getPrintCardHistoryTotalPages(): Long {
    return when(this) {
        is NetworkResult.Success -> getTotalPages(this.data.count, Settings.PAGING_SIZE.toLong())
        is NetworkResult.Failed -> 1
        is NetworkResult.Error -> 1
    }
}

