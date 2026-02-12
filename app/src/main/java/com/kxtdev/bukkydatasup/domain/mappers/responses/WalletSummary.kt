package com.kxtdev.bukkydatasup.domain.mappers.responses

import com.kxtdev.bukkydatasup.common.database.models.RoomWalletSummary
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.getTotalPages
import com.kxtdev.bukkydatasup.network.models.NetworkWalletSummaryHistoryResponse
import com.kxtdev.bukkydatasup.network.models.NetworkWalletSummaryResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult


fun NetworkWalletSummaryResponse.toRoomWalletSummaryHistoryItem(): RoomWalletSummary {
    return RoomWalletSummary(
        reference = reference,
        product = product,
        amount = amount,
        balanceBefore = balanceBefore,
        balanceAfter = balanceAfter,
        dateCreated = dateCreated
    )
}

fun List<NetworkWalletSummaryResponse>.toRoomWalletSummaryHistoryItems(): List<RoomWalletSummary> {
    return this.map { it.toRoomWalletSummaryHistoryItem() }
}

fun NetworkResult<NetworkWalletSummaryHistoryResponse>.getRoomWalletSummaryHistoryItems(): List<RoomWalletSummary> {
    return when(this) {
        is NetworkResult.Success -> this.data.results.toRoomWalletSummaryHistoryItems()
        is NetworkResult.Failed -> listOf()
        is NetworkResult.Error -> listOf()
    }
}

fun NetworkResult<NetworkWalletSummaryHistoryResponse>.getWalletSummaryHistoryTotalPages(): Long {
    return when(this) {
        is NetworkResult.Success -> getTotalPages(this.data.count, Settings.PAGING_SIZE.toLong())
        is NetworkResult.Failed -> 1
        is NetworkResult.Error -> 1
    }
}


