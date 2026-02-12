package com.kxtdev.bukkydatasup.common.utils

import kotlin.math.ceil

fun Int.isEven(): Boolean {
    return this % 2 == 0
}

fun Int.isFirst(): Boolean {
    return this == 0
}

fun Double?.asGigabyte(): String {
    if(this == null) return "0.0GB"
    return "${this}GB"
}

fun getTotalPages(count: Long, limit: Long): Long {
    return ceil(count.toDouble() / limit.toDouble()).toLong()
}

fun String?.toHandledDouble(): Double {
    if(this == null) return 0.0
    return try {
        this.toDouble()
    } catch (e: Exception) {
        val possibleNumbers = Regex("\\d+").findAll(this).map { it.value }.toList()
        possibleNumbers.first().toDouble()
    }
}