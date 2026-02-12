package com.kxtdev.bukkydatasup.domain.mappers.requests

import com.kxtdev.bukkydatasup.common.models.SendBulkSMSRequest
import com.kxtdev.bukkydatasup.network.models.NetworkSendBulkSMSRequest


fun SendBulkSMSRequest.convertToNetworkBulkSMSRequest(): NetworkSendBulkSMSRequest =
    NetworkSendBulkSMSRequest(
        recipient = this.recipient,
        message = this.message,
        sender = this.sender,
        dnd = this.dnd,
    )


