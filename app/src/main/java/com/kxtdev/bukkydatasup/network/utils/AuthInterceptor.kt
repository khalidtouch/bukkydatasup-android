package com.kxtdev.bukkydatasup.network.utils

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authReq = chain.request().newBuilder().apply {
            sessionManager.getToken()?.let { header("Authorization", "Token $it") }
        }.build()

        return chain.proceed(authReq)
    }
}