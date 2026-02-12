package com.kxtdev.bukkydatasup.common.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

fun Exception.recordException() {
    FirebaseCrashlytics.getInstance().recordException(this)
}