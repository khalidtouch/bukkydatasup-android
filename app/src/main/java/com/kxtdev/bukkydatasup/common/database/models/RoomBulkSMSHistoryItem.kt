package com.kxtdev.bukkydatasup.common.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomBulkSMSHistoryItem(
    @PrimaryKey @ColumnInfo("id") val id: Int? = null,
    @ColumnInfo("total") val total: Long? = null,
    @ColumnInfo("unit") val unit: Long? = null,
    @ColumnInfo("sender") val sender: String? = null,
    @ColumnInfo("message") val message: String? = null,
    @ColumnInfo("page") val page: Double? = null,
    @ColumnInfo("amount") val amount: Double? = null,
    @ColumnInfo("recipient") val recipient: String? = null,
    @ColumnInfo("reference") val reference: String? = null,
    @ColumnInfo("create_date") val dateCreated: String? = null,
    @ColumnInfo("dnd") val dnd: Boolean? = null,
)