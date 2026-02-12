package com.kxtdev.bukkydatasup.common.enums

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import com.kxtdev.bukkydatasup.ui.theme.Green40
import com.kxtdev.bukkydatasup.ui.theme.PromoColor
import com.kxtdev.bukkydatasup.ui.theme.Red40
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Status(val title: String): Parcelable {
    SUCCESSFUL("Successful"),
    DELIVERED("Delivered"),
    FAILED("Failed"),
    PROCESSING("Processing");

    val color: Color get() {
        return when(this){
            SUCCESSFUL, DELIVERED -> Green40
            PROCESSING -> PromoColor
            else -> Red40
        }
    }

    companion object {
        fun getByTitle(title: String): Status? {
            return Status.entries.find { it.title.lowercase() == title.lowercase() }
        }
        fun getStatusFromResponse(response: String): Status? {
            return when (response.lowercase()) {
                SUCCESSFUL.title.lowercase(),
                DELIVERED.title.lowercase() -> SUCCESSFUL
                PROCESSING.title.lowercase() -> PROCESSING
                FAILED.title.lowercase() -> FAILED
                else -> null
            }
        }
    }
}