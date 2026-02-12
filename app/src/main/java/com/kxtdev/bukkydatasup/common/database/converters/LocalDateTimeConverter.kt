package com.kxtdev.bukkydatasup.common.database.converters

import androidx.room.TypeConverter
import com.kxtdev.bukkydatasup.common.utils.formatted
import com.kxtdev.bukkydatasup.common.utils.toDatetime
import java.time.LocalDateTime

class LocalDateTimeConverter {
    @TypeConverter
    fun toString(time: LocalDateTime): String = time.formatted().orEmpty()

    @TypeConverter
    fun toTime(time: String): LocalDateTime = time.toDatetime() ?: LocalDateTime.MIN
}