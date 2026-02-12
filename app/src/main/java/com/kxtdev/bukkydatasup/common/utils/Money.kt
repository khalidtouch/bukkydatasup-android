package com.kxtdev.bukkydatasup.common.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

fun String.asMoney(): String {
    return try {
        val value = this.toHandledDouble()
        val format = NumberFormat.getNumberInstance(Locale.US)
        format.maximumFractionDigits = 2
        format.format(value)
    } catch (e: NumberFormatException) {
        this
    } catch (e: Exception) {
        this
    }
}

fun String.convertMoneyToDouble(): Double {
    return try {
        var value = this.substring(1)
        value = value.replace(",", "")
        return value.toHandledDouble()
    } catch (e: NumberFormatException) {
        0.0
    } catch (e: Exception) {
        0.0
    }
}

fun BigDecimal.asThousand(): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(this)
}

fun String.clearThousand(): String {
    return this.replace(",", "")
}