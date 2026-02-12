package com.kxtdev.bukkydatasup.common.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomDataHistoryItem(
    @PrimaryKey @ColumnInfo("id") val id: Int,
    @ColumnInfo("network_id") val networkId: Int? = null,
    @ColumnInfo("reference") val reference: String? = null,
    @ColumnInfo("balance_before") val balanceBefore: String? = null,
    @ColumnInfo("balance_after") val balanceAfter: String? = null,
    @ColumnInfo("mobile_number") val phone: String? = null,
    @ColumnInfo("plan_id") val planId: Int? = null,
    @ColumnInfo("status") val status: String? = null,
    @ColumnInfo("api_response") val apiResponse: String? = null,
    @ColumnInfo("plan_network") val planNetwork: String? = null,
    @ColumnInfo("plan_name") val planName: String? = null,
    @ColumnInfo("plan_amount") val planAmount: String? = null,
    @ColumnInfo("date_created") val dateCreated: String? = null,
    @ColumnInfo("is_ported") val isPorted: Boolean? = null,
)