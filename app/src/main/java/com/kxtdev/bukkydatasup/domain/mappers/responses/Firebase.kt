package com.kxtdev.bukkydatasup.domain.mappers.responses

import com.kxtdev.bukkydatasup.common.models.RegisterFCMTokenResponse
import com.kxtdev.bukkydatasup.network.models.NetworkRegisterFCMTokenResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult


fun NetworkRegisterFCMTokenResponse.convertToFCMTokenResponse(): RegisterFCMTokenResponse =
    RegisterFCMTokenResponse(message = this.message)

fun NetworkResult<NetworkRegisterFCMTokenResponse>.convertToFCMTokenRes():
        NetworkResult<RegisterFCMTokenResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToFCMTokenResponse())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToFCMTokenResponse())
        is NetworkResult.Error -> NetworkResult.Error(this.message)
    }
}


