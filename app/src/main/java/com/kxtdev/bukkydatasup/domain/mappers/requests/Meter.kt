package com.kxtdev.bukkydatasup.domain.mappers.requests

import com.kxtdev.bukkydatasup.common.models.SubscribeBillRequest
import com.kxtdev.bukkydatasup.network.models.NetworkSubscribeBillRequest


fun SubscribeBillRequest.convertToLocalSubscribeBillRequest() =
    NetworkSubscribeBillRequest(
        discoId = this.discoId,
        amount = this.amount.toLong(),
        meterNumber = this.meterNumber,
        meterType = this.meterType,
        phone = this.phone,
        customerName = this.customerName,
        customerAddress = this.customerAddress,
    )
