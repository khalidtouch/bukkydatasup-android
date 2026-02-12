package com.kxtdev.bukkydatasup.common.database.di

import android.content.Context
import androidx.room.Room
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
import com.kxtdev.bukkydatasup.common.database.db.Database
import com.kxtdev.bukkydatasup.common.database.migrations.migrations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): Database {
        return Room.databaseBuilder(
            context.applicationContext,
            Database::class.java,
            "bukkydatasup-database"
        )
            .addMigrations(*migrations)
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: Database): UserDao = db.userDao()

    @Singleton
    @Provides
    fun provideBankDao(db: Database): BankDao = db.bankDao()

    @Singleton
    @Provides
    fun provideAirtimeDao(db: Database): AirtimeDao = db.airtimeDao()

    @Singleton
    @Provides
    fun provideDataDao(db: Database): DataDao = db.dataDao()

    @Singleton
    @Provides
    fun provideCableDao(db: Database): CableDao = db.cableDao()

    @Singleton
    @Provides
    fun provideMeterDao(db: Database): MeterDao = db.meterDao()

    @Singleton
    @Provides
    fun provideResultCheckerDao(db: Database): ResultCheckerDao = db.resultCheckerDao()

    @Singleton
    @Provides
    fun provideCablePlanDao(db: Database): CablePlanDao = db.cablePlanDao()

    @Singleton
    @Provides
    fun provideDataPlanDao(db: Database): DataPlanDao = db.dataPlanDao()

    @Singleton
    @Provides
    fun provideWalletSummaryDao(db: Database): WalletSummaryDao = db.walletSummaryDao()

    @Singleton
    @Provides
    fun providePrintCardDao(db: Database): PrintCardDao = db.printCardDao()

    @Singleton
    @Provides
    fun provideBulkSMSDao(db: Database): BulkSMSDao = db.bulkSMSDao()

}