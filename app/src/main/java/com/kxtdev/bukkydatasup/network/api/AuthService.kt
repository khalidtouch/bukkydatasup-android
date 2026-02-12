package com.kxtdev.bukkydatasup.network.api

import com.kxtdev.bukkydatasup.network.models.NetworkLoginRequest
import com.kxtdev.bukkydatasup.network.models.NetworkLoginResponse
import com.kxtdev.bukkydatasup.network.models.NetworkRegisterRequest
import com.kxtdev.bukkydatasup.network.models.NetworkRegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService {
        @POST("/rest-auth/login/")
    suspend fun login(@Body request: NetworkLoginRequest): Response<NetworkLoginResponse>

    @POST("/api/register/")
    suspend fun register(@Body request: NetworkRegisterRequest):
            Response<NetworkRegisterResponse>
}

