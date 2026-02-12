package com.kxtdev.bukkydatasup.common.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomPrintCardHistoryItem(
    @PrimaryKey @ColumnInfo("id") val id: Int,
    @ColumnInfo("status") val status: String? = null,
    @ColumnInfo("network") val network: Int? = null,
    @ColumnInfo("network_amount") val networkAmount: Int? = null,
    @ColumnInfo("name_on_card") val nameOnCard: String? = null,
    @ColumnInfo("quantity") val quantity: Int? = null,
    @ColumnInfo("data_pin") val dataPins: String? = null,
    @ColumnInfo("previous_balance") val balanceBefore: String? = null,
    @ColumnInfo("after_balance") val balanceAfter: String? = null,
    @ColumnInfo("amount") val amount: Double? = null,
    @ColumnInfo("create_date") val dateCreated: String? = null,
)