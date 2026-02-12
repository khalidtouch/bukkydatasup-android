package com.kxtdev.bukkydatasup.common.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomDataPlan(
    @PrimaryKey @ColumnInfo("id") val id: Int,
    @ColumnInfo("plan_id") val planId: String? = null,
    @ColumnInfo("network_id") val networkId: Int? = null,
    @ColumnInfo("plan_type") val planType: String? = null,
    @ColumnInfo("plan_network") val planNetwork: String? = null,
    @ColumnInfo("validity") val validity: String? = null,
    @ColumnInfo("plan_size") val planSize: String? = null,
    @ColumnInfo("plan_amount") val planAmount: String? = null,
    @ColumnInfo("affiliate_price") val affiliatePrice: Double? = null,
    @ColumnInfo("top_user_price") val topUserPrice: Double? = null,
)