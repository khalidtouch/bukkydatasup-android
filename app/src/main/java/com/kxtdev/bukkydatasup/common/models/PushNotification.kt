package com.kxtdev.bukkydatasup.common.models

import java.time.LocalDateTime

data class PushNotificationResponse(
    val id: Int,
    val title: String,
    val body: String,
    val dateCreated: LocalDateTime
)