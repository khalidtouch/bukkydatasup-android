package com.kxtdev.bukkydatasup.common.utils

fun String.equivalent(value: String): Boolean {
    return this.lowercase() == value.lowercase()
}