package com.kxtdev.bukkydatasup.domain.mappers.responses

import com.kxtdev.bukkydatasup.common.models.AlertNotification
import com.kxtdev.bukkydatasup.network.models.NetworkAlertNotificationResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult

fun NetworkResult<NetworkAlertNotificationResponse>.convertToAlertNotification():
        NetworkResult<AlertNotification> {
    return when(this) {
        is NetworkResult.Success -> NetworkResult.success(AlertNotification(this.data.alert))
        is NetworkResult.Failed -> NetworkResult.failed(AlertNotification(this.data.alert))
        is NetworkResult.Error -> NetworkResult.error(this.message)
    }
}