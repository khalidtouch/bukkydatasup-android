package com.kxtdev.bukkydatasup.common.models

data class ProfileItem(
    val username: String?,
    val isAccountVerified: Boolean?,
    val email: String?,
    val isEmailVerified: Boolean?,
    val fullname: String?,
    val phone: String?,
    val userType: String?,
    val address: String?,
    val bonusBalance: Double?
)