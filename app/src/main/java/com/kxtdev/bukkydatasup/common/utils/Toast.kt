package com.kxtdev.bukkydatasup.common.utils

import android.content.Context
import android.widget.Toast

fun appToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun appToastLong(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}