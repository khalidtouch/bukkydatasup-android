package com.kxtdev.bukkydatasup.common.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomMeterHistoryItem(
    @PrimaryKey @ColumnInfo("id") val id: Int,
    @ColumnInfo("reference") val reference: String? = null,
    @ColumnInfo("disco_name") val discoName: String? = null,
    @ColumnInfo("disco_id") val discoId: Int? = null,
    @ColumnInfo("amount") val amount: String? = null,
    @ColumnInfo("phone") val phone: String? = null,
    @ColumnInfo("meter_number") val meterNumber: String? = null,
    @ColumnInfo("token") val token: String? = null,
    @ColumnInfo("meter_type") val meterType: String? = null,
    @ColumnInfo("paid_amount") val paidAmount: String? = null,
    @ColumnInfo("balance_before") val balanceBefore: String? = null,
    @ColumnInfo("balance_after") val balanceAfter: String? = null,
    @ColumnInfo("status") val status: String? = null,
    @ColumnInfo("date_created") val dateCreated: String? = null,
    @ColumnInfo("customer_name") val customerName: String? = null,
    @ColumnInfo("customer_address") val customerAddress: String? = null
)