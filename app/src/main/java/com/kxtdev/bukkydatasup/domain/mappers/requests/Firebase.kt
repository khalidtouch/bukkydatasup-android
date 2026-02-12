package com.kxtdev.bukkydatasup.domain.mappers.requests

import com.kxtdev.bukkydatasup.common.models.RegisterFCMTokenRequest
import com.kxtdev.bukkydatasup.network.models.NetworkRegisterFCMTokenRequest


fun RegisterFCMTokenRequest.convertToFCMRequest(): NetworkRegisterFCMTokenRequest {
    return NetworkRegisterFCMTokenRequest(
        token = this.token
    )
}
