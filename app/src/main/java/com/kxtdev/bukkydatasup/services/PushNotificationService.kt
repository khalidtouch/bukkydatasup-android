package com.kxtdev.bukkydatasup.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.main.MainActivity
import com.kxtdev.bukkydatasup.ui.design.PoshIcon
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PushNotificationService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("PushNotificationService", "onNewToken: new token => $token generated!", )
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let { msg ->
            Log.e("PushNotificationService", "onMessageReceived: title => ${msg.title.orEmpty()} body => ${msg.body.orEmpty()}", )
            send(
                NotificationData(
                    title = msg.title.orEmpty(),
                    body = msg.body.orEmpty(),
                    channelId = getString(R.string.default_notification_channel_id),
                    brandIcon = PoshIcon.Logo
                )
            )
        }
    }
}



fun PushNotificationService.send(data: NotificationData) {
    val requestCode = 2433
    val intent = Intent(this, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    val pendingIntent = TaskStackBuilder.create(this).run {
        addNextIntentWithParentStack(Intent(Intent.ACTION_VIEW, "bukkydatasup://home_screen".toUri()))
        getPendingIntent(requestCode, PendingIntent.FLAG_IMMUTABLE)
    }
    val channelId = data.channelId
    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val builder = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(data.brandIcon)
        .setContentTitle(data.title)
        .setContentText(data.body)
        .setAutoCancel(true)
        .setSound(defaultSoundUri)
        .setContentIntent(pendingIntent)

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "bukkydatasup_notification_channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    val notificationId = 637
    notificationManager.notify(notificationId, builder.build())
}


data class NotificationData(
    val title: String,
    val body: String,
    val channelId: String,
    @DrawableRes val brandIcon: Int
)