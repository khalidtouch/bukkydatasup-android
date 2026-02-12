package com.kxtdev.bukkydatasup.common.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomUser(
    @PrimaryKey @ColumnInfo(name = "id", index = true) val id: Int,
    @ColumnInfo("email") val email: String,
    @ColumnInfo("username") val username: String,
    @ColumnInfo("fullname", defaultValue = "") val fullname: String? = null,
    @ColumnInfo("pin", defaultValue = "") val pin: String? = null,
    @ColumnInfo("address", defaultValue = "") val address: String? = null,
    @ColumnInfo("phone", defaultValue = "") val phone: String? = null,
    @ColumnInfo("user_type", defaultValue = "") val userType: String? = null,
    @ColumnInfo("email_verify") val isEmailVerified: Boolean? = null,
    @ColumnInfo("account_verify") val isAccountVerified: Boolean? = null,
    @ColumnInfo("password") val passwordHash: String? = null,
    @ColumnInfo("account_balance") val accountBalance: Double? = null,
    @ColumnInfo("wallet_balance") val walletBalance: String? = null,
    @ColumnInfo("bonus_balance") val bonusBalance: String? = null,
    @ColumnInfo("referer_username") val refererUsername: String? = null,
    @ColumnInfo("reserved_account_number") val reservedAccountNumber: String? = null,
    @ColumnInfo("reserved_bank_name") val reservedBankName: String? = null,
    @ColumnInfo("bvn") val bvn: String? = null,
    @ColumnInfo("nin") val nin: String? = null
)