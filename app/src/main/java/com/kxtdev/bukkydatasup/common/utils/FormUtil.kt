package com.kxtdev.bukkydatasup.common.utils

object FormUtil {
    fun splitPerPhoneLengthComplete(text: String): List<String> {
        if(text.isNotBlank() && text.length >= Settings.MAX_PHONE_LENGTH) {
            val mod = text.length % Settings.MAX_PHONE_LENGTH
            val tags = mutableListOf<String>()
            val divisions: Int = if(mod == 0) {
                text.length / Settings.MAX_PHONE_LENGTH
            } else {
                (text.length - mod) / Settings.MAX_PHONE_LENGTH
            }
            repeat(divisions) { i ->
                val start = i * Settings.MAX_PHONE_LENGTH
                val end = (i + 1) * Settings.MAX_PHONE_LENGTH
                tags += text.substring(start, end)
            }

            return tags.toList()
        }

        return listOf()
    }

}