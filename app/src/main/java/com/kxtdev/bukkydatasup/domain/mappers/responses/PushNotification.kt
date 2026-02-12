package com.kxtdev.bukkydatasup.domain.mappers.responses

import androidx.paging.PagingData
import androidx.paging.map
import com.kxtdev.bukkydatasup.common.models.PushNotificationResponse
import com.kxtdev.bukkydatasup.common.utils.toDatetime
import com.kxtdev.bukkydatasup.network.models.NetworkPushNotificationResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime


fun NetworkPushNotificationResponse.convertToLocalPushNotification(): PushNotificationResponse {
    return PushNotificationResponse(
        id = this.id,
        title = this.title,
        body = this.body,
        dateCreated = this.dateCreated.toDatetime() ?: LocalDateTime.MIN,
    )
}

fun Flow<PagingData<NetworkPushNotificationResponse>>.convertToLocalPushNotificationPaging():
        Flow<PagingData<PushNotificationResponse>> {
    return this.map { page ->
        page.map { data ->
            data.convertToLocalPushNotification()
        }
    }
}



