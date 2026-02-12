package com.kxtdev.bukkydatasup.common.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


const val pattern = "MMM dd, yyyy h:mma"
const val serverPattern = "yyyy-MM-dd HH:mm:ss.SSSSSS"
const val oldServerPattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
const val oldServerPatternTruncated = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX"
const val oldServerPatternTruncated2 = "yyyy-MM-dd'T'HH:mm:ssXXX"


fun String.toDatetime(): LocalDateTime? {
    return try {
        if(contains("T")) {
            LocalDateTime.parse(
                this,
                DateTimeFormatter.ofPattern(oldServerPattern)
            )
        } else {
            LocalDateTime.parse(
                this,
                DateTimeFormatter.ofPattern(serverPattern)
            )
        }
    } catch (e: Exception) {
        if(contains("T")) {
            try {
                LocalDateTime.parse(
                    this,
                    DateTimeFormatter.ofPattern(oldServerPatternTruncated)
                )
            } catch (e: Exception) {
                LocalDateTime.parse(
                    this,
                    DateTimeFormatter.ofPattern(oldServerPatternTruncated2)
                )
            }

        } else {
            e.printStackTrace()
            null
        }
    }
}

fun LocalDateTime.formatted(): String? {
    return try {
        val result = format(
            DateTimeFormatter.ofPattern(
                pattern
            )
        )
        result.replace("AM", "am").replace("PM", "pm")
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}