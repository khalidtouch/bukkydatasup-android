package com.kxtdev.bukkydatasup.common.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kxtdev.bukkydatasup.common.database.models.RoomWalletSummary
import kotlinx.coroutines.flow.Flow

@Dao
abstract class WalletSummaryDao {

    @Upsert
    abstract  suspend fun insertAll(records: List<RoomWalletSummary>)

    @Query(value = """
        select * from RoomWalletSummary where product like '%' || :search || '%' order by id asc
    """)
    abstract  fun getHistoryRecordsPaged(search: String): PagingSource<Int, RoomWalletSummary>

    @Query(value = """
        select * from RoomWalletSummary order by id asc limit :limit offset :offset  
    """)
    abstract  fun getHistoryRecordsPaged(limit: Int, offset: Int): PagingSource<Int, RoomWalletSummary>

    @Query(value = """
        select * from RoomWalletSummary where product like '%' || :search || '%' order by id asc
    """)
    abstract  fun getHistoryRecords(search: String): Flow<List<RoomWalletSummary>>

    @Query(value = """
        select * from RoomWalletSummary order by id asc limit :limit offset :offset  
    """)
    abstract  fun getHistoryRecords(limit: Int, offset: Int): Flow<List<RoomWalletSummary>>

    @Query(value = "delete from RoomWalletSummary")
    abstract  suspend fun clear()

    @Query(value = "select count(id) from RoomWalletSummary")
    abstract fun getCount(): Flow<Long>

    @Query(value = "select * from RoomWalletSummary where id like :id")
    abstract suspend fun getHistoryRecordItem(id: Int): RoomWalletSummary?

}