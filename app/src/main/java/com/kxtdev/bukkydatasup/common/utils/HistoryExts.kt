package com.kxtdev.bukkydatasup.common.utils

import com.kxtdev.bukkydatasup.common.database.models.RoomAirtimeHistoryItem
import com.kxtdev.bukkydatasup.common.database.models.RoomBulkSMSHistoryItem
import com.kxtdev.bukkydatasup.common.database.models.RoomCableHistoryItem
import com.kxtdev.bukkydatasup.common.database.models.RoomDataHistoryItem
import com.kxtdev.bukkydatasup.common.database.models.RoomMeterHistoryItem
import com.kxtdev.bukkydatasup.common.database.models.RoomPrintCardHistoryItem
import com.kxtdev.bukkydatasup.common.database.models.RoomResultCheckerHistoryItem
import com.kxtdev.bukkydatasup.common.database.models.RoomWalletSummary
import com.kxtdev.bukkydatasup.common.enums.CableTV
import com.kxtdev.bukkydatasup.common.enums.DiscoProvider
import com.kxtdev.bukkydatasup.common.enums.ExamType
import com.kxtdev.bukkydatasup.common.enums.Network
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.common.enums.Status
import com.kxtdev.bukkydatasup.common.models.HistoryDetailItem
import com.kxtdev.bukkydatasup.common.models.HistoryListItem
import com.kxtdev.bukkydatasup.network.models.NetworkBuyDataHistoryItemResponse
import com.kxtdev.bukkydatasup.network.models.NetworkRechargeAirtimeHistoryItemResponse
import com.kxtdev.bukkydatasup.network.models.NetworkRechargeMeterHistoryItem
import com.kxtdev.bukkydatasup.network.models.NetworkResultCheckerHistoryItemResponse
import com.kxtdev.bukkydatasup.network.models.NetworkSubscribeCableTVHistoryItemResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Flow<List<RoomWalletSummary>>.convertToWalletSummaryHistoryListItems(): Flow<List<HistoryListItem>> {
    return this.map { items -> items.map { item -> item.convertToWalletSummaryHistoryListItem() } }
}

fun RoomWalletSummary.convertToWalletSummaryHistoryListItem(): HistoryListItem {
    return HistoryListItem(
        id = id,
        icon = Product.WALLET_HISTORY.icon,
        timestamp = dateCreated?.toDatetime(),
        product = Product.WALLET_HISTORY,
        status = Status.SUCCESSFUL,
        amount = amount?.toHandledDouble(),
        recipient = product?.take(20),
    )
}

fun HistoryListItem.revertToWalletSummaryHistoryItem(): RoomWalletSummary {
    return RoomWalletSummary(id)
}

fun RoomWalletSummary?.convertToWalletSummaryDetailItem(): HistoryDetailItem? {
    if(this == null) return null

    return HistoryDetailItem(
        id = id,
        recipient = "Balance Before: N${balanceBefore?.toHandledDouble()} \nBalance After: N${balanceAfter?.toHandledDouble()}",
        providerName = Product.WALLET_HISTORY.description,
        amount = amount?.toHandledDouble(),
        paidAmount = amount?.toHandledDouble(),
        status = Status.SUCCESSFUL,
        apiResponse = product,
        product = Product.WALLET_HISTORY,
        reference = reference,
        timestamp = dateCreated?.toDatetime(),
    )
}

fun Flow<List<RoomResultCheckerHistoryItem>>.convertToResultCheckerHistoryListItems():
        Flow<List<HistoryListItem>> {
    return this.map { items -> items.map { item -> item.convertToResultCheckerHistoryListItem() } }
}

fun RoomResultCheckerHistoryItem.convertToResultCheckerHistoryListItem(): HistoryListItem {
    val exam = ExamType.getByTitle(this.examName ?: ExamType.NECO.title) ?: ExamType.NECO
    val status = Status.getStatusFromResponse(this.status ?: Status.FAILED.title) ?: Status.FAILED
    val amount = this.amount

    return HistoryListItem(
        id = this.id,
        icon = exam.icon,
        timestamp = this.dateCreated?.toDatetime(),
        product = Product.RESULT_CHECKER,
        status = status,
        amount = amount,
        recipient = this.pins,
    )
}

fun HistoryListItem.revertToResultCheckerHistoryItem(): RoomResultCheckerHistoryItem {
    return RoomResultCheckerHistoryItem(id ?: -1)
}

fun RoomResultCheckerHistoryItem?.convertToResultCheckerDetailItem(): HistoryDetailItem? {
    if(this == null) return null

    val status = Status.getStatusFromResponse(this.status ?: Status.FAILED.title)
    val exam = ExamType.getByTitle(this.examName ?: ExamType.NECO.title) ?: ExamType.NECO
    val amount = this.amount
    val apiResponse = this.pins

    return HistoryDetailItem(
        id = this.id,
        timestamp = this.dateCreated?.toDatetime(),
        product = Product.RESULT_CHECKER,
        status = status,
        reference = null,
        recipient = null,
        providerName = exam.title,
        amount = amount,
        paidAmount = amount,
        apiResponse = apiResponse,
    )
}

fun Flow<List<RoomMeterHistoryItem>>.convertToMeterHistoryListItems(): Flow<List<HistoryListItem>> {
    return this.map { items -> items.map { item -> item.convertToMeterHistoryListItem() } }
}

fun RoomMeterHistoryItem.convertToMeterHistoryListItem(): HistoryListItem {
    val disco = DiscoProvider.getById(this.discoId ?: DiscoProvider.IKEJA_ELECTRIC.id) ?: DiscoProvider.IKEJA_ELECTRIC
    val status = Status.getStatusFromResponse(this.status ?: Status.FAILED.title) ?: Status.FAILED
    val amount = this.amount

    return HistoryListItem(
        id = this.id,
        icon = disco.icon,
        timestamp = this.dateCreated?.toDatetime(),
        product = Product.ELECTRICITY,
        status = status,
        amount = amount?.toHandledDouble(),
        recipient = this.meterNumber,
    )
}

fun HistoryListItem.revertToMeterHistoryItem(): RoomMeterHistoryItem {
    return RoomMeterHistoryItem(id ?: -1)
}

fun RoomMeterHistoryItem?.convertToMeterHistoryDetailItem(): HistoryDetailItem? {
    if(this == null) return null

    val status = Status.getStatusFromResponse(this.status ?: Status.FAILED.title)
    val disco = DiscoProvider.getById(this.discoId ?: DiscoProvider.IKEJA_ELECTRIC.id) ?: DiscoProvider.IKEJA_ELECTRIC
    val amount = this.amount
    val paidAmount = this.paidAmount
    val apiResponse = this.token

    return HistoryDetailItem(
        id = this.id,
        timestamp = this.dateCreated?.toDatetime(),
        product = Product.ELECTRICITY,
        status = status,
        reference = this.reference,
        recipient = this.meterNumber,
        providerName = disco.title,
        amount = amount?.toHandledDouble(),
        paidAmount = paidAmount?.toHandledDouble(),
        apiResponse = apiResponse,
    )
}

fun RoomCableHistoryItem?.convertToCableHistoryDetailItem(): HistoryDetailItem? {
    if(this == null) return null

    val status = Status.getStatusFromResponse(this.status ?: Status.FAILED.title)
    val cable = CableTV.getById(this.cableId ?: CableTV.GOTV.id) ?: CableTV.GOTV
    val amount = this.planAmount?.toHandledDouble()
    val apiResponse = this.packageName

    return HistoryDetailItem(
        id = this.id,
        timestamp = this.dateCreated?.toDatetime(),
        product = Product.CABLE,
        status = status,
        reference = this.reference,
        recipient = this.smartCardNumber,
        providerName = cable.title,
        amount = amount,
        paidAmount = amount,
        apiResponse = apiResponse,
    )
}

fun HistoryListItem.revertToCableHistoryItem(): RoomCableHistoryItem {
    return RoomCableHistoryItem(
        id = id ?: -1,
    )
}

fun Flow<List<RoomCableHistoryItem>>.convertToCableHistoryListItems(): Flow<List<HistoryListItem>> {
    return this.map { items -> items.map { item -> item.convertToCableHistoryListItem() } }
}

fun RoomCableHistoryItem.convertToCableHistoryListItem(): HistoryListItem {
    val cable = CableTV.getById(this.cableId ?: CableTV.GOTV.id) ?: CableTV.GOTV
    val status = Status.getStatusFromResponse(this.status ?: Status.FAILED.title) ?: Status.FAILED
    val amount = this.planAmount?.toHandledDouble()

    return HistoryListItem(
        id = this.id,
        icon = cable.icon,
        timestamp = this.dateCreated?.toDatetime(),
        product = Product.CABLE,
        status = status,
        amount = amount,
        recipient = this.smartCardNumber,
    )
}

fun RoomDataHistoryItem?.convertToDataHistoryDetailItem(): HistoryDetailItem? {
    if(this == null) return null

    val status = Status.getStatusFromResponse(this.status ?: Status.FAILED.title)
    val network = Network.getById(this.networkId ?: 1) ?: Network.MTN
    val amount = this.planAmount?.toHandledDouble()
    val apiResponse = this.apiResponse

    return HistoryDetailItem(
        id = this.id,
        timestamp = this.dateCreated?.toDatetime(),
        product = Product.DATA,
        status = status,
        reference= this.reference,
        recipient = this.phone,
        providerName = network.title,
        amount = amount,
        paidAmount = amount,
        apiResponse = apiResponse,
    )
}

fun HistoryListItem.revertToRoomDataHistoryItem(): RoomDataHistoryItem {
    return RoomDataHistoryItem(
        id = id ?: -1,
        networkId = null,
        reference = null,
        balanceBefore = null,
        balanceAfter = null,
        phone = null,
        planId = null,
        status = null,
        apiResponse = null,
        planNetwork = null,
        planName = null,
        planAmount = null,
        dateCreated = null,
        isPorted = null,
    )
}

fun Flow<List<RoomDataHistoryItem>>.convertToDataHistoryListItems(): Flow<List<HistoryListItem>> {
    return this.map { items -> items.map { item -> item.convertToDataHistoryListItem() } }
}

fun RoomDataHistoryItem.convertToDataHistoryListItem(): HistoryListItem {

    val network = Network.getById(this.networkId ?: 1) ?: Network.MTN
    val status = Status.getStatusFromResponse(this.status ?: Status.FAILED.title) ?: Status.FAILED
    val amount = this.planAmount?.toHandledDouble()

    return HistoryListItem(
        id = this.id,
        icon = network.icon,
        timestamp = this.dateCreated?.toDatetime(),
        product = Product.DATA,
        status = status,
        amount = amount,
        recipient = this.phone,
    )
}

fun HistoryListItem.revertToRoomAirtimeHistoryItem(): RoomAirtimeHistoryItem {
    return RoomAirtimeHistoryItem(
        id = id ?: -1,
        airtimeType = null,
        networkId = null,
        reference = null,
        phone = null,
        amount = null,
        planAmount = null,
        planNetwork = null,
        paidAmount = null,
        balanceBefore = null,
        balanceAfter = null,
        status = null,
        dateCreated = null,
        isPorted = null,
    )
}

fun Flow<List<RoomAirtimeHistoryItem>>.convertToAirtimeHistoryListItems(): Flow<List<HistoryListItem>> {
    return this.map { items -> items.map { item -> item.convertToAirtimeHistoryListItem() } }
}


fun RoomAirtimeHistoryItem.convertToAirtimeHistoryListItem(): HistoryListItem {
    val network = Network.getById(this.networkId ?: 1) ?: Network.MTN
    val status = Status.getStatusFromResponse(this.status ?: Status.FAILED.title) ?: Status.FAILED
    val amount = this.amount?.toHandledDouble()

    return HistoryListItem(
        id = id,
        icon = network.icon,
        timestamp = this.dateCreated?.toDatetime(),
        product = Product.AIRTIME,
        status = status,
        amount = amount,
        recipient = phone,
    )
}

fun RoomAirtimeHistoryItem?.convertToAirtimeHistoryDetailItem(): HistoryDetailItem? {
    if(this == null) return null

    val status = Status.getStatusFromResponse(this.status ?: Status.FAILED.title)
    val network = Network.getById(this.networkId ?: 1) ?: Network.MTN
    val amount = this.amount?.toHandledDouble()
    val paidAmount = this.paidAmount?.toHandledDouble()
    val apiResponse = this.airtimeType

    return HistoryDetailItem(
        id = this.id,
        timestamp = this.dateCreated?.toDatetime(),
        product = Product.AIRTIME,
        status = status,
        reference = this.reference,
        recipient = this.phone,
        providerName = network.title,
        amount = amount,
        paidAmount = paidAmount,
        apiResponse = apiResponse,
    )
}


fun RoomPrintCardHistoryItem.convertToPrintCardHistoryListItem(): HistoryListItem {
    val network = Network.getById(this.network ?: 1) ?: Network.MTN
    val status = Status.getStatusFromResponse(this.status ?: Status.FAILED.title) ?: Status.FAILED
    val amount = this.amount

    return HistoryListItem(
        id = id,
        icon = network.icon,
        timestamp = this.dateCreated?.toDatetime(),
        product = Product.PRINT_CARD,
        status = status,
        amount = amount,
        recipient = nameOnCard,
    )
}


fun Flow<List<RoomPrintCardHistoryItem>>.convertToPrintCardHistoryListItems(): Flow<List<HistoryListItem>> {
    return this.map { items -> items.map { item -> item.convertToPrintCardHistoryListItem() } }
}

fun RoomPrintCardHistoryItem?.convertToPrintCardHistoryDetailItem(): HistoryDetailItem? {
    if(this == null) return null

    val status = Status.getStatusFromResponse(this.status ?: Status.FAILED.title)
    val network = Network.getById(this.network ?: 1) ?: Network.MTN
    val amount = this.amount
    val apiResponse = this.dataPins
    val paidAmount = amount

    return HistoryDetailItem(
        id = this.id,
        timestamp = this.dateCreated?.toDatetime(),
        product = Product.PRINT_CARD,
        status = status,
        reference = "${this.quantity} pins",
        recipient = this.nameOnCard,
        providerName = network.title,
        amount = amount,
        paidAmount = paidAmount,
        apiResponse = apiResponse,
    )
}

fun RoomBulkSMSHistoryItem.convertToBulkSMSHistoryListItem(): HistoryListItem {
    val amount = this.amount
    val product = Product.BULK_SMS

    return HistoryListItem(
        id = id,
        icon = product.icon,
        timestamp = this.dateCreated?.toDatetime(),
        product = product,
        status = Status.SUCCESSFUL,
        amount = amount,
        recipient = sender,
    )
}

fun Flow<List<RoomBulkSMSHistoryItem>>.convertToBulkSMSHistoryListItems(): Flow<List<HistoryListItem>> {
    return this.map { items -> items.map { item -> item.convertToBulkSMSHistoryListItem() } }
}


fun RoomBulkSMSHistoryItem?.convertToBulkSMSHistoryDetailItem(): HistoryDetailItem? {
    if(this == null) return null

    return HistoryDetailItem(
        id = this.id,
        timestamp = this.dateCreated?.toDatetime(),
        product = Product.BULK_SMS,
        status = Status.SUCCESSFUL,
        reference = this.reference,
        recipient = this.recipient,
        providerName = this.sender,
        amount = amount,
        paidAmount = amount,
        apiResponse = this.message,
    )
}

fun NetworkResult<NetworkBuyDataHistoryItemResponse>.convertToDataTransactionReceipt(): HistoryDetailItem? {
    return when(this) {
        is NetworkResult.Success -> this.data.convertToDataReceipt()
        is NetworkResult.Failed -> this.data.convertToDataReceipt()
        is NetworkResult.Error -> null
    }
}

fun NetworkBuyDataHistoryItemResponse.convertToDataReceipt(): HistoryDetailItem {
    val status = Status.getStatusFromResponse(this.status)
    val network = Network.getById(this.network) ?: Network.MTN
    val amount = this.planAmount.toHandledDouble()
    val apiResponse = this.apiResponse

    return HistoryDetailItem(
        id = this.id,
        timestamp = this.dateCreated.toDatetime(),
        product = Product.DATA,
        status = status,
        reference= this.reference,
        recipient = this.phone,
        providerName = network.title,
        amount = amount,
        paidAmount = amount,
        apiResponse = apiResponse,
    )
}

fun NetworkResult<NetworkRechargeAirtimeHistoryItemResponse>.convertToAirtimeTransactionReceipt(): HistoryDetailItem? {
    return when(this) {
        is NetworkResult.Success -> this.data.convertToAirtimeReceipt()
        is NetworkResult.Failed -> this.data.convertToAirtimeReceipt()
        is NetworkResult.Error -> null
    }
}

fun NetworkRechargeAirtimeHistoryItemResponse.convertToAirtimeReceipt(): HistoryDetailItem {
    val status = Status.getStatusFromResponse(this.status)
    val network = Network.getById(this.network) ?: Network.MTN
    val amount = this.amount.toHandledDouble()
    val paidAmount = this.paidAmount.toHandledDouble()
    val apiResponse = this.airtimeType

    return HistoryDetailItem(
        id = this.id,
        timestamp = this.dateCreated.toDatetime(),
        product = Product.AIRTIME,
        status = status,
        reference = this.reference,
        recipient = this.phone,
        providerName = network.title,
        amount = amount,
        paidAmount = paidAmount,
        apiResponse = apiResponse,
    )
}

fun NetworkResult<NetworkSubscribeCableTVHistoryItemResponse>.convertToCableTransactionReceipt(): HistoryDetailItem? {
    return when(this) {
        is NetworkResult.Success -> this.data.convertToCableReceipt()
        is NetworkResult.Failed -> this.data.convertToCableReceipt()
        is NetworkResult.Error -> null
    }
}


fun NetworkSubscribeCableTVHistoryItemResponse.convertToCableReceipt(): HistoryDetailItem {
    val status = Status.getStatusFromResponse(this.status)
    val cable = CableTV.getById(this.cableId) ?: CableTV.GOTV
    val amount = this.planAmount.toHandledDouble()
    val apiResponse = this.packageName

    return HistoryDetailItem(
        id = this.id,
        timestamp = this.dateCreated.toDatetime(),
        product = Product.CABLE,
        status = status,
        reference = this.reference,
        recipient = this.smartCardNumber,
        providerName = cable.title,
        amount = amount,
        paidAmount = amount,
        apiResponse = apiResponse,
    )
}

fun NetworkResult<NetworkRechargeMeterHistoryItem>.convertToBillTransactionReceipt(): HistoryDetailItem? {
    return when(this) {
        is NetworkResult.Success -> this.data.convertToBillReceipt()
        is NetworkResult.Failed -> this.data.convertToBillReceipt()
        is NetworkResult.Error -> null
    }
}

fun NetworkRechargeMeterHistoryItem.convertToBillReceipt(): HistoryDetailItem {
    val status = Status.getStatusFromResponse(this.status ?: Status.FAILED.title)
    val disco = DiscoProvider.getById(this.discoId ?: DiscoProvider.IKEJA_ELECTRIC.id) ?: DiscoProvider.IKEJA_ELECTRIC
    val amount = this.amount
    val paidAmount = this.paidAmount
    val apiResponse = this.token

    return HistoryDetailItem(
        id = this.id,
        timestamp = this.dateCreated?.toDatetime(),
        product = Product.ELECTRICITY,
        status = status,
        reference = this.reference,
        recipient = this.meterNumber,
        providerName = disco.title,
        amount = amount?.toHandledDouble(),
        paidAmount = paidAmount?.toHandledDouble(),
        apiResponse = apiResponse,
    )
}

fun NetworkResult<NetworkResultCheckerHistoryItemResponse>.convertToResultCheckerTransactionReceipt(): HistoryDetailItem? {
    return when(this) {
        is NetworkResult.Success -> this.data.convertToResultCheckerReceipt()
        is NetworkResult.Failed -> this.data.convertToResultCheckerReceipt()
        is NetworkResult.Error -> null
    }
}

fun NetworkResultCheckerHistoryItemResponse.convertToResultCheckerReceipt(): HistoryDetailItem {
    val status = Status.getStatusFromResponse(this.status ?: Status.FAILED.title)
    val exam = ExamType.getByTitle(this.examName ?: ExamType.NECO.title) ?: ExamType.NECO
    val amount = this.amount
    val apiResponse = this.pins

    return HistoryDetailItem(
        id = this.id,
        timestamp = this.dateCreated?.toDatetime(),
        product = Product.RESULT_CHECKER,
        status = status,
        reference = null,
        recipient = null,
        providerName = exam.title,
        amount = amount,
        paidAmount = amount,
        apiResponse = apiResponse?.joinToString(","),
    )
}
