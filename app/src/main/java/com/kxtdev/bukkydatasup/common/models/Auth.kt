package com.kxtdev.bukkydatasup.common.models

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String
)

data class LogoutResponse(
    val message: String
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val confirmPassword: String,
    val phone: String,
    val email: String,
    val fullname: String?,
    val address: String?,
    val refererUsername: String?
)


data class RegisterResponse(
    val message: String
)
