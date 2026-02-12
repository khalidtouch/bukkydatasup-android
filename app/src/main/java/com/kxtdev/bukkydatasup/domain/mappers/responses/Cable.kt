package com.kxtdev.bukkydatasup.domain.mappers.responses

import androidx.paging.PagingData
import androidx.paging.map
import com.kxtdev.bukkydatasup.common.database.models.RoomCableHistoryItem
import com.kxtdev.bukkydatasup.common.models.SubscribeCableTVHistory
import com.kxtdev.bukkydatasup.common.models.SubscribeCableTVHistoryItem
import com.kxtdev.bukkydatasup.common.models.SubscribeCableTVResponse
import com.kxtdev.bukkydatasup.common.models.ValidateSmartCardNumberResponse
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.getTotalPages
import com.kxtdev.bukkydatasup.common.utils.toDatetime
import com.kxtdev.bukkydatasup.network.models.NetworkSubscribeCableTVHistoryItemResponse
import com.kxtdev.bukkydatasup.network.models.NetworkSubscribeCableTVHistoryResponse
import com.kxtdev.bukkydatasup.network.models.NetworkSubscribeCableTVResponse
import com.kxtdev.bukkydatasup.network.models.NetworkValidateSmartCardNumberResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime


fun NetworkSubscribeCableTVHistoryItemResponse.toRoomCableHistoryItem(): RoomCableHistoryItem {
    return RoomCableHistoryItem(
        id = id,
        reference = reference,
        cableId = cableId,
        planId = planId,
        packageName = packageName,
        planAmount = planAmount,
        paidAmount = paidAmount,
        smartCardNumber = smartCardNumber,
        balanceBefore = balanceBefore,
        balanceAfter = balanceAfter,
        status = status,
        dateCreated = dateCreated,
        customerName = customerName,
    )
}

fun List<NetworkSubscribeCableTVHistoryItemResponse>.toRoomCableHistoryItems(): List<RoomCableHistoryItem> {
    return this.map { it.toRoomCableHistoryItem() }
}

fun NetworkResult<NetworkSubscribeCableTVHistoryResponse>.getRoomCableHistoryItems(): List<RoomCableHistoryItem> {
    return when(this) {
        is NetworkResult.Success -> this.data.results.toRoomCableHistoryItems()
        is NetworkResult.Failed -> listOf()
        is NetworkResult.Error -> listOf()
    }
}

fun NetworkResult<NetworkSubscribeCableTVHistoryResponse>.getCableHistoryTotalPages(): Long {
    return when(this) {
        is NetworkResult.Success -> getTotalPages(this.data.count, Settings.PAGING_SIZE.toLong())
        is NetworkResult.Failed -> 1
        is NetworkResult.Error -> 1
    }
}


fun NetworkSubscribeCableTVHistoryItemResponse.convertToLocalResponse():
        SubscribeCableTVHistoryItem {
    return SubscribeCableTVHistoryItem(
        id = this.id,
        reference = this.reference,
        cableId = this.cableId,
        planId = this.planId,
        packageName = this.packageName,
        planAmount = this.planAmount,
        paidAmount = this.paidAmount,
        smartCardNumber = this.smartCardNumber,
        balanceBefore = this.balanceBefore,
        balanceAfter = this.balanceAfter,
        status = this.status,
        dateCreated = this.dateCreated.toDatetime() ?: LocalDateTime.MIN,
        customerName = this.customerName,
    )
}


fun NetworkResult<NetworkSubscribeCableTVHistoryItemResponse>.convertToLocalResponse():
        NetworkResult<SubscribeCableTVHistoryItem> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalResponse())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalResponse())
        is NetworkResult.Error -> NetworkResult.error<SubscribeCableTVHistoryItem>(this.message)
    }
}

fun Flow<PagingData<NetworkSubscribeCableTVHistoryItemResponse>>.convertToLocalResponse():
        Flow<PagingData<SubscribeCableTVHistoryItem>> {
    return this.map { page ->
        page.map { data ->
            data.convertToLocalResponse()
        }
    }
}

fun NetworkSubscribeCableTVHistoryResponse.convertToLocalCableHistory():
        SubscribeCableTVHistory = SubscribeCableTVHistory(
    count = this.count,
    next = this.next,
    prev = this.previous,
    results = this.results.map { it.convertToLocalResponse() }
)

fun NetworkResult<NetworkSubscribeCableTVHistoryResponse>.convertToLocalCableHistoryResponse():
        NetworkResult<SubscribeCableTVHistory> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalCableHistory())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalCableHistory())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}


fun NetworkResult<NetworkSubscribeCableTVResponse>.convertToSubscribeCableTVResponse():
        NetworkResult<SubscribeCableTVResponse> {
    return when (this) {
        is NetworkResult.Success -> {
            NetworkResult.success(
                SubscribeCableTVResponse(
                    id = this.data.id,
                    reference = this.data.reference,
                    cableId = this.data.cableId,
                    planId = this.data.planId,
                    packageName = this.data.packageName,
                    planAmount = this.data.planAmount,
                    paidAmount = this.data.paidAmount,
                    smartCardNumber = this.data.smartCardNumber,
                    balanceBefore = this.data.balanceBefore,
                    balanceAfter = this.data.balanceAfter,
                    status = this.data.status,
                    dateCreated = this.data.dateCreated.toDatetime() ?: LocalDateTime.MIN,
                    customerName = this.data.customerName,
                )
            )
        }

        is NetworkResult.Failed -> {
            NetworkResult.failed(
                SubscribeCableTVResponse(
                    id = this.data.id,
                    reference = this.data.reference,
                    cableId = this.data.cableId,
                    planId = this.data.planId,
                    packageName = this.data.packageName,
                    planAmount = this.data.planAmount,
                    paidAmount = this.data.paidAmount,
                    smartCardNumber = this.data.smartCardNumber,
                    balanceBefore = this.data.balanceBefore,
                    balanceAfter = this.data.balanceAfter,
                    status = this.data.status,
                    dateCreated = this.data.dateCreated.toDatetime() ?: LocalDateTime.MIN,
                    customerName = this.data.customerName,
                )
            )
        }

        is NetworkResult.Error -> {
            NetworkResult.error<SubscribeCableTVResponse>(this.message)
        }
    }
}


fun NetworkResult<NetworkValidateSmartCardNumberResponse>.convertToValidateSmartCardNumberResponse():
        NetworkResult<ValidateSmartCardNumberResponse> {
    return when (this) {
        is NetworkResult.Success -> {
            NetworkResult.success(
                ValidateSmartCardNumberResponse(
                    isInvalid = this.data.isInvalid,
                    name = this.data.name,
                )
            )
        }

        is NetworkResult.Failed -> {
            NetworkResult.success(
                ValidateSmartCardNumberResponse(
                    isInvalid = this.data.isInvalid,
                    name = this.data.name,
                )
            )
        }

        is NetworkResult.Error -> {
            NetworkResult.error<ValidateSmartCardNumberResponse>(this.message)
        }
    }
}



