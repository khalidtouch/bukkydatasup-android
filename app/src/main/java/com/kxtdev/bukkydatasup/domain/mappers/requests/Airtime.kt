package com.kxtdev.bukkydatasup.domain.mappers.requests

import com.kxtdev.bukkydatasup.common.models.RechargeAirtimeRequest
import com.kxtdev.bukkydatasup.network.models.NetworkRechargeAirtimeRequest


fun RechargeAirtimeRequest.convertToRechargeAirtimeRequestNetwork() = NetworkRechargeAirtimeRequest(
    network = this.network,
    phone = this.phone,
    amount = this.amount.toLong(),
    airtimeType = this.airtimeType,
    isPorted = this.isPorted,
)
