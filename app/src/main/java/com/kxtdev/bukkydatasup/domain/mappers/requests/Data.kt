package com.kxtdev.bukkydatasup.domain.mappers.requests

import com.kxtdev.bukkydatasup.common.models.BuyDataRequest
import com.kxtdev.bukkydatasup.network.models.NetworkBuyDataRequest



fun BuyDataRequest.convertToBuyDataRequestNetwork() = NetworkBuyDataRequest(
    network = this.network,
    phone = this.phone,
    plan = this.plan,
    isPorted = this.isPorted,
)
