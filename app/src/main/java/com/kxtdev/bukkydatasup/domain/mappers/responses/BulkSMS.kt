package com.kxtdev.bukkydatasup.domain.mappers.responses

import com.kxtdev.bukkydatasup.common.database.models.RoomBulkSMSHistoryItem
import com.kxtdev.bukkydatasup.common.models.SendBulkSMSResponse
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.getTotalPages
import com.kxtdev.bukkydatasup.network.models.NetworkBulkSMSHistoryItem
import com.kxtdev.bukkydatasup.network.models.NetworkBulkSMSHistoryResponse
import com.kxtdev.bukkydatasup.network.models.NetworkSendBulkSMSResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult


fun NetworkSendBulkSMSResponse.convertToLocalSendBulkSMSResponse(): SendBulkSMSResponse =
    SendBulkSMSResponse(message = this.message)

fun NetworkResult<NetworkSendBulkSMSResponse>.convertToLocalBulkSMS():
        NetworkResult<SendBulkSMSResponse> {
    return when(this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalSendBulkSMSResponse())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalSendBulkSMSResponse())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}


fun NetworkBulkSMSHistoryItem.toRoomBulkSMSHistoryItem(): RoomBulkSMSHistoryItem {
    return RoomBulkSMSHistoryItem(
        total = this.total,
        unit = this.unit,
        sender = this.sender,
        message = this.message,
        page = this.page,
        amount = this.amount,
        recipient = this.recipient,
        reference = this.reference,
        dateCreated = this.dateCreated,
        dnd = this.dnd,
    )
}

fun List<NetworkBulkSMSHistoryItem>.toRoomBulkSMSHistoryItems(): List<RoomBulkSMSHistoryItem> {
    return this.map { it.toRoomBulkSMSHistoryItem() }
}

fun NetworkResult<NetworkBulkSMSHistoryResponse>.getRoomBulkSMSHistoryItems(): List<RoomBulkSMSHistoryItem> {
    return when(this) {
        is NetworkResult.Success -> this.data.results.toRoomBulkSMSHistoryItems()
        is NetworkResult.Failed -> listOf()
        is NetworkResult.Error -> listOf()
    }
}

fun NetworkResult<NetworkBulkSMSHistoryResponse>.getBulkSMSHistoryTotalPages(): Long {
    return when(this) {
        is NetworkResult.Success -> getTotalPages(this.data.count, Settings.PAGING_SIZE.toLong())
        is NetworkResult.Failed -> 1
        is NetworkResult.Error -> 1
    }
}
