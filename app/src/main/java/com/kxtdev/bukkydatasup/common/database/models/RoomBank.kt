package com.kxtdev.bukkydatasup.common.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomBank(
    @PrimaryKey @ColumnInfo("id") val id: Int? = null,
    @ColumnInfo("bank_code") val bankCode: String? = null,
    @ColumnInfo("bank_name") val bankName: String? = null,
    @ColumnInfo("account_number") val accountNumber: String? = null,
    @ColumnInfo("account_name") val accountName: String? = null
)