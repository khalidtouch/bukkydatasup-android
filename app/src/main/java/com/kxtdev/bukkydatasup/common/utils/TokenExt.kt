package com.kxtdev.bukkydatasup.common.utils

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging

fun getFCMToken(callback: (String) -> Unit) {
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) return@OnCompleteListener
        val token = task.result.toString()
        callback.invoke(token)
        Firebase.messaging.subscribeToTopic(Settings.TOPIC_ALL_CUSTOMERS)
    })
}