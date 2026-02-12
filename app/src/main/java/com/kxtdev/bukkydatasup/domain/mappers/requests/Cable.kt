package com.kxtdev.bukkydatasup.domain.mappers.requests

import com.kxtdev.bukkydatasup.common.models.SubscribeCableTVRequest
import com.kxtdev.bukkydatasup.network.models.NetworkSubscribeCableTVRequest


fun SubscribeCableTVRequest.convertToSubscribeCableNetwork() = NetworkSubscribeCableTVRequest(
    cableId = cableId,
    planId = planId,
    smartCardNumber = smartCardNumber
)