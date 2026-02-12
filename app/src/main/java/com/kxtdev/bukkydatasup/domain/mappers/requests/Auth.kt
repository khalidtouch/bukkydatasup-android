package com.kxtdev.bukkydatasup.domain.mappers.requests

import com.kxtdev.bukkydatasup.common.models.LoginRequest
import com.kxtdev.bukkydatasup.common.models.RegisterRequest
import com.kxtdev.bukkydatasup.network.models.NetworkLoginRequest
import com.kxtdev.bukkydatasup.network.models.NetworkRegisterRequest

fun LoginRequest.convertLoginRequestToNetwork() = NetworkLoginRequest(username, password)


fun RegisterRequest.convertToNetworkRequest(): NetworkRegisterRequest =
    NetworkRegisterRequest(
        username = this.username,
        password = this.password,
        confirmPassword = this.confirmPassword,
        phone = this.phone,
        email = this.email,
        fullname = this.fullname,
        address = this.address,
        refererUsername = this.refererUsername,
    )
