package com.kxtdev.bukkydatasup.domain.mappers.responses

import androidx.paging.PagingData
import androidx.paging.map
import com.kxtdev.bukkydatasup.common.database.models.RoomMeterHistoryItem
import com.kxtdev.bukkydatasup.common.models.RechargeMeterHistory
import com.kxtdev.bukkydatasup.common.models.RechargeMeterHistoryItem
import com.kxtdev.bukkydatasup.common.models.SubscribeBillResponse
import com.kxtdev.bukkydatasup.common.models.ValidateMeterResponse
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.common.utils.getTotalPages
import com.kxtdev.bukkydatasup.common.utils.toDatetime
import com.kxtdev.bukkydatasup.network.models.NetworkRechargeMeterHistory
import com.kxtdev.bukkydatasup.network.models.NetworkRechargeMeterHistoryItem
import com.kxtdev.bukkydatasup.network.models.NetworkSubscribeBillResponse
import com.kxtdev.bukkydatasup.network.models.NetworkValidateMeterResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime


fun NetworkRechargeMeterHistoryItem.toRoomMeterHistoryItem(): RoomMeterHistoryItem {
    return RoomMeterHistoryItem(
        id = id,
        reference = reference,
        discoName = discoName,
        discoId = discoId,
        amount = amount,
        phone = phone,
        meterNumber = meterNumber,
        token = token,
        meterType = meterType,
        paidAmount = paidAmount,
        balanceBefore = balanceBefore,
        balanceAfter = balanceAfter,
        status = status,
        dateCreated = dateCreated,
        customerName = customerName,
        customerAddress = customerAddress,
    )
}

fun List<NetworkRechargeMeterHistoryItem>.toRoomMeterHistoryItems(): List<RoomMeterHistoryItem> {
    return this.map { it.toRoomMeterHistoryItem() }
}

fun NetworkResult<NetworkRechargeMeterHistory>.getRoomMeterHistoryItems(): List<RoomMeterHistoryItem> {
    return when(this) {
        is NetworkResult.Success -> this.data.results.toRoomMeterHistoryItems()
        is NetworkResult.Failed -> listOf()
        is NetworkResult.Error -> listOf()
    }
}

fun NetworkResult<NetworkRechargeMeterHistory>.getMeterHistoryTotalPages(): Long {
    return when(this) {
        is NetworkResult.Success -> getTotalPages(this.data.count, Settings.PAGING_SIZE.toLong())
        is NetworkResult.Failed -> 1
        is NetworkResult.Error -> 1
    }
}

fun NetworkValidateMeterResponse.convertToLocalValidateMeterResponse():
        ValidateMeterResponse = ValidateMeterResponse(
    isInvalid = this.isInvalid,
    name = this.name,
    address = this.address,
)

fun NetworkResult<NetworkValidateMeterResponse>.convertToLocalValidateMeter():
        NetworkResult<ValidateMeterResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalValidateMeterResponse())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalValidateMeterResponse())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}

fun NetworkSubscribeBillResponse.convertToLocalSubscribeBill():
        SubscribeBillResponse = SubscribeBillResponse(
    id = this.id,
    reference = this.reference,
    discoId = this.discoId,
    discoName = this.discoName,
    amount = this.amount.toDouble(),
    phone = this.phone,
    meterNumber = this.meterNumber,
    token = this.token,
    meterType = this.meterType,
    paidAmount = this.paidAmount.toDouble(),
    balanceBefore = this.balanceBefore.toDouble(),
    balanceAfter = this.balanceAfter.toDouble(),
    status = this.status,
    dateCreated = this.dateCreated.toDatetime() ?: LocalDateTime.MIN,
    customerName = this.customerName,
    address = this.customerAddress,
)

fun NetworkResult<NetworkSubscribeBillResponse>.convertToLocalSubscribeBillResponse():
        NetworkResult<SubscribeBillResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalSubscribeBill())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalSubscribeBill())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}

fun NetworkRechargeMeterHistoryItem.convertToLocalRechargeHistory(): RechargeMeterHistoryItem =
    RechargeMeterHistoryItem(
        id = this.id,
        reference = this.reference,
        discoName = this.discoName,
        discoId = this.discoId,
        amount = this.amount?.toDouble(),
        phone = this.phone,
        meterNumber = this.meterNumber,
        token = this.token,
        meterType = this.meterType,
        paidAmount = this.paidAmount?.toDouble(),
        balanceBefore = this.balanceBefore?.toDouble(),
        balanceAfter = this.balanceAfter?.toDouble(),
        status = this.status,
        dateCreated = this.dateCreated?.toDatetime() ?: LocalDateTime.MIN,
        customerName = this.customerName,
        customerAddress = this.customerAddress,
    )

fun Flow<PagingData<NetworkRechargeMeterHistoryItem>>.convertToLocalRechargeHistoryPaging():
        Flow<PagingData<RechargeMeterHistoryItem>> {
    return this.map { page ->
        page.map { data ->
            data.convertToLocalRechargeHistory()
        }
    }
}

fun NetworkResult<NetworkRechargeMeterHistoryItem>.convertToLocalRechargeMeterHistory():
        NetworkResult<RechargeMeterHistoryItem> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalRechargeHistory())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalRechargeHistory())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}

fun NetworkRechargeMeterHistory.convertToLocalHistory(): RechargeMeterHistory =
    RechargeMeterHistory(
        count = this.count,
        next = this.next,
        prev = this.previous,
        results = this.results.map { it.convertToLocalRechargeHistory() }
    )

fun NetworkResult<NetworkRechargeMeterHistory>.convertToLocalHistoryResponse():
        NetworkResult<RechargeMeterHistory> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalHistory())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalHistory())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}



