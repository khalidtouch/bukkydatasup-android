package com.kxtdev.bukkydatasup.common.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kxtdev.bukkydatasup.common.database.converters.LocalDateTimeConverter
import com.kxtdev.bukkydatasup.common.database.daos.AirtimeDao
import com.kxtdev.bukkydatasup.common.database.daos.BankDao
import com.kxtdev.bukkydatasup.common.database.daos.BulkSMSDao
import com.kxtdev.bukkydatasup.common.database.daos.CableDao
import com.kxtdev.bukkydatasup.common.database.daos.CablePlanDao
import com.kxtdev.bukkydatasup.common.database.daos.DataDao
import com.kxtdev.bukkydatasup.common.database.daos.DataPlanDao
import com.kxtdev.bukkydatasup.common.database.daos.MeterDao
import com.kxtdev.bukkydatasup.common.database.daos.PrintCardDao
import com.kxtdev.bukkydatasup.common.database.daos.ResultCheckerDao
import com.kxtdev.bukkydatasup.common.database.daos.UserDao
import com.kxtdev.bukkydatasup.common.database.daos.WalletSummaryDao
import com.kxtdev.bukkydatasup.common.database.models.RoomAirtimeHistoryItem
import com.kxtdev.bukkydatasup.common.database.models.RoomBank
import com.kxtdev.bukkydatasup.common.database.models.RoomBulkSMSHistoryItem
import com.kxtdev.bukkydatasup.common.database.models.RoomCableHistoryItem
import com.kxtdev.bukkydatasup.common.database.models.RoomCablePlan
import com.kxtdev.bukkydatasup.common.database.models.RoomDataHistoryItem
import com.kxtdev.bukkydatasup.common.database.models.RoomDataPlan
import com.kxtdev.bukkydatasup.common.database.models.RoomMeterHistoryItem
import com.kxtdev.bukkydatasup.common.database.models.RoomPrintCardHistoryItem
import com.kxtdev.bukkydatasup.common.database.models.RoomResultCheckerHistoryItem
import com.kxtdev.bukkydatasup.common.database.models.RoomUser
import com.kxtdev.bukkydatasup.common.database.models.RoomWalletSummary

@Database(
    entities = [
        RoomUser::class,
        RoomBank::class,
        RoomAirtimeHistoryItem::class,
        RoomDataHistoryItem::class,
        RoomCableHistoryItem::class,
        RoomMeterHistoryItem::class,
        RoomResultCheckerHistoryItem::class,
        RoomDataPlan::class,
        RoomCablePlan::class,
        RoomWalletSummary::class,
        RoomPrintCardHistoryItem::class,
        RoomBulkSMSHistoryItem::class,
    ],
    version = 1, exportSchema = true,
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun airtimeDao(): AirtimeDao
    abstract fun dataDao(): DataDao
    abstract fun cableDao(): CableDao
    abstract fun meterDao(): MeterDao
    abstract fun resultCheckerDao(): ResultCheckerDao
    abstract fun bankDao(): BankDao
    abstract fun cablePlanDao(): CablePlanDao
    abstract fun dataPlanDao(): DataPlanDao
    abstract fun walletSummaryDao(): WalletSummaryDao
    abstract fun printCardDao(): PrintCardDao
    abstract fun bulkSMSDao(): BulkSMSDao
}