package com.kxtdev.bukkydatasup.domain.mappers.responses

import com.kxtdev.bukkydatasup.common.database.models.RoomDataHistoryItem
import com.kxtdev.bukkydatasup.common.models.BuyDataResponse
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.getTotalPages
import com.kxtdev.bukkydatasup.common.utils.toDatetime
import com.kxtdev.bukkydatasup.network.models.NetworkBuyDataHistoryItemResponse
import com.kxtdev.bukkydatasup.network.models.NetworkBuyDataHistoryResponse
import com.kxtdev.bukkydatasup.network.models.NetworkBuyDataResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import java.time.LocalDateTime


fun NetworkResult<NetworkBuyDataResponse>.convertToBuyDataResponse(): NetworkResult<BuyDataResponse> {
    return when (this) {
        is NetworkResult.Success -> {
            NetworkResult.success(
                BuyDataResponse(
                    id = this.data.id,
                    network = this.data.network,
                    reference = this.data.reference,
                    balanceBefore = this.data.balanceBefore,
                    balanceAfter = this.data.balanceAfter,
                    phone = this.data.phone,
                    planId = this.data.planId,
                    status = this.data.status,
                    apiResponse = this.data.apiResponse,
                    planNetwork = this.data.planNetwork,
                    planName = this.data.planName,
                    planAmount = this.data.planAmount,
                    dateCreated = this.data.dateCreated.toDatetime() ?: LocalDateTime.MIN,
                    isPorted = this.data.isPorted
                )
            )
        }

        is NetworkResult.Failed -> {
            NetworkResult.failed(
                BuyDataResponse(
                    id = this.data.id,
                    network = this.data.network,
                    reference = this.data.reference,
                    balanceBefore = this.data.balanceBefore,
                    balanceAfter = this.data.balanceAfter,
                    phone = this.data.phone,
                    planId = this.data.planId,
                    status = this.data.status,
                    apiResponse = this.data.apiResponse,
                    planNetwork = this.data.planNetwork,
                    planName = this.data.planName,
                    planAmount = this.data.planAmount,
                    dateCreated = this.data.dateCreated.toDatetime() ?: LocalDateTime.MIN,
                    isPorted = this.data.isPorted
                )
            )
        }

        is NetworkResult.Error -> {
            NetworkResult.error<BuyDataResponse>(this.message)
        }
    }
}



fun NetworkBuyDataHistoryItemResponse.toRoomDataHistoryItem(): RoomDataHistoryItem {
    return RoomDataHistoryItem(
        id = id,
        networkId = network,
        reference = reference,
        balanceBefore = balanceBefore,
        balanceAfter = balanceAfter,
        phone = phone,
        planId = planId,
        status = status,
        apiResponse = apiResponse,
        planNetwork = planNetwork,
        planName = planName,
        planAmount = planAmount,
        dateCreated = dateCreated,
        isPorted = isPorted,
    )
}


fun List<NetworkBuyDataHistoryItemResponse>.toRoomDataHistoryItems(): List<RoomDataHistoryItem> {
    return this.map { it.toRoomDataHistoryItem() }
}


fun NetworkResult<NetworkBuyDataHistoryResponse>.getRoomDataHistoryItems(): List<RoomDataHistoryItem> {
    return when(this) {
        is NetworkResult.Success -> this.data.results.toRoomDataHistoryItems()
        is NetworkResult.Failed -> listOf()
        is NetworkResult.Error -> listOf()
    }
}


fun NetworkResult<NetworkBuyDataHistoryResponse>.getDataHistoryTotalPages(): Long {
    return when(this) {
        is NetworkResult.Success -> getTotalPages(this.data.count, Settings.PAGING_SIZE.toLong())
        is NetworkResult.Failed -> 1
        is NetworkResult.Error -> 1
    }
}

