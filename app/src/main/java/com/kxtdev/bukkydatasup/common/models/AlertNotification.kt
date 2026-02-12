package com.kxtdev.bukkydatasup.common.models

data class AlertNotification(
    val alert: String? = null
) {
    val hasContent: Boolean get() {
        return !alert.isNullOrBlank()
    }
}