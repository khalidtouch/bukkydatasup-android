package com.kxtdev.bukkydatasup.workers

import androidx.work.Constraints
import androidx.work.NetworkType

val internetConstraints = Constraints.Builder().apply {
    setRequiredNetworkType(NetworkType.CONNECTED)
}.build()