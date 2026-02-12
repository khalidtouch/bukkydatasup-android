package com.kxtdev.bukkydatasup.common.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomCableHistoryItem(
    @PrimaryKey @ColumnInfo("id") val id: Int,
    @ColumnInfo("reference") val reference: String? = null,
    @ColumnInfo("cable_id") val cableId: Int? = null,
    @ColumnInfo("plan_id") val planId: Int? = null,
    @ColumnInfo("package_name") val packageName: String? = null,
    @ColumnInfo("plan_amount") val planAmount: String? = null,
    @ColumnInfo("paid_amount") val paidAmount: String? = null,
    @ColumnInfo("smart_card_number") val smartCardNumber: String? = null,
    @ColumnInfo("balance_before") val balanceBefore: String? = null,
    @ColumnInfo("balance_after") val balanceAfter: String? = null,
    @ColumnInfo("status") val status: String? = null,
    @ColumnInfo("date_created") val dateCreated: String? = null,
    @ColumnInfo("customer_name") val customerName: String? = null,
)