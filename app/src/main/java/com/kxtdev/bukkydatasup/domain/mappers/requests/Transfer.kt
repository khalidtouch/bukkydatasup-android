package com.kxtdev.bukkydatasup.domain.mappers.requests

import com.kxtdev.bukkydatasup.common.models.TransferFundsRequest
import com.kxtdev.bukkydatasup.network.models.NetworkTransferRequest


fun TransferFundsRequest.convertToNetworkTransferFundsRequest():
        NetworkTransferRequest = NetworkTransferRequest(
    amount = this.amount.toLong(),
    recipientUsername = this.recipientUsername,
)
