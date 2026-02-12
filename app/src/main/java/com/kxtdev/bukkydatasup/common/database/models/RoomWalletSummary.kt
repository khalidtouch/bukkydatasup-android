package com.kxtdev.bukkydatasup.common.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomWalletSummary(
    @PrimaryKey @ColumnInfo("id") val id: Int? = null,
    @ColumnInfo("ident") val reference: String? = null,
    @ColumnInfo("product") val product: String? = null,
    @ColumnInfo("amount") val amount: String? = null,
    @ColumnInfo("balance_before") val balanceBefore: String? = null,
    @ColumnInfo("balance_after") val balanceAfter: String? = null,
    @ColumnInfo("create_date") val dateCreated: String? = null,
)