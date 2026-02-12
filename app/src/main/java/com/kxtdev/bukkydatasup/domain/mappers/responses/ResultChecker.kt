package com.kxtdev.bukkydatasup.domain.mappers.responses

import androidx.paging.PagingData
import androidx.paging.map
import com.kxtdev.bukkydatasup.common.database.models.RoomResultCheckerHistoryItem
import com.kxtdev.bukkydatasup.common.models.ResultCheckerHistory
import com.kxtdev.bukkydatasup.common.models.ResultCheckerHistoryItemResponse
import com.kxtdev.bukkydatasup.common.models.ResultCheckerResponse
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.getTotalPages
import com.kxtdev.bukkydatasup.common.utils.toDatetime
import com.kxtdev.bukkydatasup.network.models.NetworkResultCheckerHistoryItemResponse
import com.kxtdev.bukkydatasup.network.models.NetworkResultCheckerHistoryResponse
import com.kxtdev.bukkydatasup.network.models.NetworkResultCheckerResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime


fun NetworkResultCheckerHistoryItemResponse.toRoomResultCheckerHistoryItem(): RoomResultCheckerHistoryItem {
    return RoomResultCheckerHistoryItem(
        id = id,
        examName = examName,
        quantity = quantity,
        jambProfileId = jambProfileId,
        pins = pins?.joinToString(","),
        status = status,
        balanceBefore = balanceBefore,
        balanceAfter = balanceAfter,
        amount = amount,
        dateCreated = dateCreated,
    )
}

fun List<NetworkResultCheckerHistoryItemResponse>.toRoomResultCheckerHistoryItems(): List<RoomResultCheckerHistoryItem> {
    return this.map { it.toRoomResultCheckerHistoryItem() }
}

fun NetworkResult<NetworkResultCheckerHistoryResponse>.getRoomResultCheckerHistoryItems(): List<RoomResultCheckerHistoryItem> {
    return when(this) {
        is NetworkResult.Success -> this.data.results.toRoomResultCheckerHistoryItems()
        is NetworkResult.Failed -> listOf()
        is NetworkResult.Error -> listOf()
    }
}

fun NetworkResult<NetworkResultCheckerHistoryResponse>.getResultCheckerTotalPages(): Long {
    return when(this) {
        is NetworkResult.Success -> getTotalPages(this.data.count, Settings.PAGING_SIZE.toLong())
        is NetworkResult.Failed -> 1
        is NetworkResult.Error -> 1
    }
}

fun NetworkResultCheckerResponse.convertToLocalResultCheckRes(): ResultCheckerResponse =
    ResultCheckerResponse(
        examName = this.examName,
        data = this.data,
        quantity = this.quantity,
        jambProfileId = this.jambProfileId,
        id = this.id,
        status = this.status,
        balanceBefore = this.balanceBefore.toDouble(),
        balanceAfter = this.balanceAfter.toDouble(),
        amount = this.amount,
        dateCreated = this.dateCreated.toDatetime() ?: LocalDateTime.MIN,
    )


fun NetworkResult<NetworkResultCheckerResponse>.convertToResultCheckerResponse():
        NetworkResult<ResultCheckerResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalResultCheckRes())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalResultCheckRes())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}

fun NetworkResultCheckerHistoryItemResponse.convertToLocalResultChecker():
        ResultCheckerHistoryItemResponse = ResultCheckerHistoryItemResponse(
    examName = this.examName,
    quantity = this.quantity,
    jambProfileId = this.jambProfileId,
    pins = this.pins,
    id = this.id,
    status = this.status,
    balanceBefore = this.balanceBefore?.toDouble(),
    balanceAfter = this.balanceAfter?.toDouble(),
    amount = this.amount,
    dateCreated = this.dateCreated?.toDatetime() ?: LocalDateTime.MIN,
)

fun Flow<PagingData<NetworkResultCheckerHistoryItemResponse>>.convertToResultCheckerResponse():
        Flow<PagingData<ResultCheckerHistoryItemResponse>> {
    return this.map { page ->
        page.map { data ->
            data.convertToLocalResultChecker()
        }
    }
}


fun NetworkResult<NetworkResultCheckerHistoryItemResponse>.convertToLocalResultCheckerHistoryItem():
        NetworkResult<ResultCheckerHistoryItemResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalResultChecker())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalResultChecker())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}

fun NetworkResultCheckerHistoryResponse.convertToLocalResultChecker(): ResultCheckerHistory =
    ResultCheckerHistory(
        count = this.count,
        next = this.next,
        prev = this.previous,
        results = this.results.map { it.convertToLocalResultChecker() }
    )

fun NetworkResult<NetworkResultCheckerHistoryResponse>.convertToLocalResultCheckerResponse():
        NetworkResult<ResultCheckerHistory> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalResultChecker())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalResultChecker())
        is NetworkResult.Error -> NetworkResult.error(this.message)

    }
}
