package com.kxtdev.bukkydatasup.domain.mappers.responses

import androidx.paging.PagingData
import androidx.paging.map
import com.kxtdev.bukkydatasup.common.database.models.RoomAirtimeHistoryItem
import com.kxtdev.bukkydatasup.common.models.RechargeAirtimeHistory
import com.kxtdev.bukkydatasup.common.models.RechargeAirtimeHistoryItemResponse
import com.kxtdev.bukkydatasup.common.models.RechargeAirtimeResponse
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.getTotalPages
import com.kxtdev.bukkydatasup.common.utils.toDatetime
import com.kxtdev.bukkydatasup.network.models.NetworkRechargeAirtimeHistoryItemResponse
import com.kxtdev.bukkydatasup.network.models.NetworkRechargeAirtimeHistoryResponse
import com.kxtdev.bukkydatasup.network.models.NetworkRechargeAirtimeResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime



fun NetworkResult<NetworkRechargeAirtimeResponse>.convertToRechargeAirtimeResponse(): NetworkResult<RechargeAirtimeResponse> {
    return when (this) {
        is NetworkResult.Success -> {
            NetworkResult.success(
                RechargeAirtimeResponse(
                    id = this.data.id,
                    airtimeType = this.data.airtimeType,
                    network = this.data.network,
                    reference = this.data.reference,
                    phone = this.data.phone,
                    amount = this.data.amount,
                    planAmount = this.data.planAmount,
                    planNetwork = this.data.planNetwork,
                    paidAmount = this.data.paidAmount,
                    balanceBefore = this.data.balanceBefore,
                    balanceAfter = this.data.balanceAfter,
                    status = this.data.status,
                    dateCreated = this.data.dateCreated.toDatetime() ?: LocalDateTime.MIN,
                    customerName = this.data.customerName,
                    isPorted = this.data.isPorted,
                )
            )
        }

        is NetworkResult.Failed -> {
            NetworkResult.failed(
                RechargeAirtimeResponse(
                    id = this.data.id,
                    airtimeType = this.data.airtimeType,
                    network = this.data.network,
                    reference = this.data.reference,
                    phone = this.data.phone,
                    amount = this.data.amount,
                    planAmount = this.data.planAmount,
                    planNetwork = this.data.planNetwork,
                    paidAmount = this.data.paidAmount,
                    balanceBefore = this.data.balanceBefore,
                    balanceAfter = this.data.balanceAfter,
                    status = this.data.status,
                    dateCreated = this.data.dateCreated.toDatetime() ?: LocalDateTime.MIN,
                    customerName = this.data.customerName,
                    isPorted = this.data.isPorted,
                )
            )
        }

        is NetworkResult.Error -> {
            NetworkResult.error<RechargeAirtimeResponse>(this.message)
        }
    }
}


fun NetworkRechargeAirtimeHistoryItemResponse.convertToAirtimeHistory():
        RechargeAirtimeHistoryItemResponse {
    return RechargeAirtimeHistoryItemResponse(
        id = this.id,
        airtimeType = this.airtimeType,
        network = this.network,
        reference = this.reference,
        phone = this.phone,
        amount = this.amount,
        planAmount = this.planAmount,
        planNetwork = this.planNetwork,
        paidAmount = this.paidAmount,
        balanceBefore = this.balanceBefore,
        balanceAfter = this.balanceAfter,
        status = this.status,
        dateCreated = this.dateCreated.toDatetime() ?: LocalDateTime.MIN,
        isPorted = this.isPorted,
    )
}

fun NetworkRechargeAirtimeHistoryResponse.convertToLocalAirtimeHistory():
        RechargeAirtimeHistory {
    return RechargeAirtimeHistory(
        count = this.count,
        next = this.next,
        prev = this.previous,
        results = this.results.map { it.convertToAirtimeHistory() }
    )
}

fun NetworkRechargeAirtimeHistoryItemResponse.toRoomAirtimeHistoryItem(): RoomAirtimeHistoryItem {
    return RoomAirtimeHistoryItem(
        id = id,
        airtimeType = airtimeType,
        networkId = network,
        reference = reference,
        phone = phone,
        amount = amount,
        planAmount = planAmount,
        planNetwork = planNetwork,
        paidAmount = paidAmount,
        balanceBefore = balanceBefore,
        balanceAfter = balanceAfter,
        status = status,
        dateCreated = dateCreated,
        isPorted = isPorted,
    )
}


fun RoomAirtimeHistoryItem.toLocalAirtimeHistoryCached(): RechargeAirtimeHistoryItemResponse {
    return RechargeAirtimeHistoryItemResponse(
        id = id,
        airtimeType = airtimeType,
        network = networkId,
        reference = reference,
        phone = phone,
        amount = amount,
        planAmount = planAmount,
        planNetwork = planNetwork,
        paidAmount = paidAmount,
        balanceBefore = balanceBefore,
        balanceAfter = balanceAfter,
        status = status,
        dateCreated = dateCreated?.toDatetime(),
        isPorted = isPorted ?: false,
    )
}

fun Flow<PagingData<RoomAirtimeHistoryItem>>.convertToLocalAirtimeHistoryItems(): Flow<PagingData<RechargeAirtimeHistoryItemResponse>> {
    return this.map { page -> page.map { data -> data.toLocalAirtimeHistoryCached() } }
}

fun List<NetworkRechargeAirtimeHistoryItemResponse>.toRoomAirtimeHistoryItems(): List<RoomAirtimeHistoryItem> {
    return this.map { it.toRoomAirtimeHistoryItem() }
}



fun NetworkResult<NetworkRechargeAirtimeHistoryResponse>.getRoomAirtimeHistoryItems(): List<RoomAirtimeHistoryItem> {
    return when(this) {
        is NetworkResult.Success -> this.data.results.toRoomAirtimeHistoryItems()
        is NetworkResult.Failed -> listOf()
        is NetworkResult.Error -> listOf()
    }
}

fun NetworkResult<NetworkRechargeAirtimeHistoryResponse>.getAirtimeHistoryTotalPages(): Long {
    return when(this) {
        is NetworkResult.Success -> getTotalPages(this.data.count, Settings.PAGING_SIZE.toLong())
        is NetworkResult.Failed -> 1
        is NetworkResult.Error -> 1
    }
}


fun NetworkResult<NetworkRechargeAirtimeHistoryResponse>.convertToLocalAirtimeHistoryResponse():
        NetworkResult<RechargeAirtimeHistory> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalAirtimeHistory())
        is NetworkResult.Failed -> NetworkResult.success(this.data.convertToLocalAirtimeHistory())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}


fun Flow<PagingData<NetworkRechargeAirtimeHistoryItemResponse>>.convertToAirtimeHistory():
        Flow<PagingData<RechargeAirtimeHistoryItemResponse>> {
    return this.map { page -> page.map { data -> data.convertToAirtimeHistory() } }
}


fun NetworkResult<NetworkRechargeAirtimeHistoryItemResponse>.convertToLocalAirtimeHistoryItem():
        NetworkResult<RechargeAirtimeHistoryItemResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToAirtimeHistory())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToAirtimeHistory())
        is NetworkResult.Error -> NetworkResult.error<RechargeAirtimeHistoryItemResponse>(this.message)
    }
}

