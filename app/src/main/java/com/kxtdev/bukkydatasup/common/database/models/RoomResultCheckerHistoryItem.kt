package com.kxtdev.bukkydatasup.common.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomResultCheckerHistoryItem(
    @PrimaryKey @ColumnInfo("id") val id: Int,
    @ColumnInfo("exam_name") val examName: String? = null,
    @ColumnInfo("quantity") val quantity: Int? = null,
    @ColumnInfo("customer_jamb_profile_id") val jambProfileId: String? = null,
    @ColumnInfo("data") val pins: String? = null,
    @ColumnInfo("status") val status: String? = null,
    @ColumnInfo("balance_before") val balanceBefore: String? = null,
    @ColumnInfo("balance_after") val balanceAfter: String? = null,
    @ColumnInfo("amount") val amount: Double? = null,
    @ColumnInfo("date_created") val dateCreated: String? = null
)