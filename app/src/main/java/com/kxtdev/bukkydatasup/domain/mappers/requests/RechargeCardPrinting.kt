package com.kxtdev.bukkydatasup.domain.mappers.requests

import com.kxtdev.bukkydatasup.common.models.PrintCardRequest
import com.kxtdev.bukkydatasup.network.models.NetworkPrintCardRequest


fun PrintCardRequest.convertToNetworkPrintCardRequest():
        NetworkPrintCardRequest = NetworkPrintCardRequest(
    networkId = this.networkId,
    networkAmount = this.networkAmount,
    quantity = this.quantity,
    nameOnCard = this.nameOnCard
)
