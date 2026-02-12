package com.kxtdev.bukkydatasup.common.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomCablePlan(
    @PrimaryKey @ColumnInfo("id") val id: Int,
    @ColumnInfo("plan_id") val planId: String? = null,
    @ColumnInfo("cable_name") val cableName: String? = null,
    @ColumnInfo("package_name") val packageName: String? = null,
    @ColumnInfo("plan_amount") val planAmount: String? = null
)