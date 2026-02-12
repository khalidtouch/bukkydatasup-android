package com.kxtdev.bukkydatasup.domain.mappers.responses

import com.kxtdev.bukkydatasup.common.models.LoginResponse
import com.kxtdev.bukkydatasup.common.models.LogoutResponse
import com.kxtdev.bukkydatasup.common.models.RegisterResponse
import com.kxtdev.bukkydatasup.network.models.NetworkLoginResponse
import com.kxtdev.bukkydatasup.network.models.NetworkLogoutResponse
import com.kxtdev.bukkydatasup.network.models.NetworkRegisterResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult


fun NetworkResult<NetworkLoginResponse>.convertLoginResponseToLocal(): NetworkResult<LoginResponse> {
    return when (this) {
        is NetworkResult.Success -> {
            NetworkResult.success(
                LoginResponse(
                    token = this.data.token
                )
            )
        }

        is NetworkResult.Failed -> {
            NetworkResult.failed(
                LoginResponse(
                    token = this.data.token,
                )
            )
        }

        is NetworkResult.Error -> {
            NetworkResult.error<LoginResponse>(
                this.message
            )
        }
    }
}


fun NetworkRegisterResponse.convertToLocalRegister(): RegisterResponse =
    RegisterResponse(message = this.message)


fun NetworkResult<NetworkRegisterResponse>.convertToLocalRegisterResponse():
        NetworkResult<RegisterResponse> =
    when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLocalRegister())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLocalRegister())
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }


fun NetworkLogoutResponse.convertToLogout(): LogoutResponse {
    return LogoutResponse(message = this.message)
}


fun NetworkResult<NetworkLogoutResponse>.convertToLogoutResponse():
        NetworkResult<LogoutResponse> {
    return when (this) {
        is NetworkResult.Success -> NetworkResult.success(this.data.convertToLogout())
        is NetworkResult.Failed -> NetworkResult.failed(this.data.convertToLogout())
        is NetworkResult.Error -> NetworkResult.Error(this.message)
    }
}


