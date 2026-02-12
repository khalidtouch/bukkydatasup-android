package com.kxtdev.bukkydatasup.common.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kxtdev.bukkydatasup.common.database.models.RoomBulkSMSHistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BulkSMSDao {

    @Upsert
    abstract  suspend fun insertAll(records: List<RoomBulkSMSHistoryItem>)

    @Query(value = """
        select * from RoomBulkSMSHistoryItem where sender like '%' || :search || '%' or
         recipient like '%' || :search || '%' order by id asc
    """)
    abstract  fun getHistoryRecordsPaged(search: String): PagingSource<Int, RoomBulkSMSHistoryItem>

    @Query(value = """
        select * from RoomBulkSMSHistoryItem order by id asc limit :limit offset :offset  
    """)
    abstract  fun getHistoryRecordsPaged(
        limit: Int,
        offset: Int
    ): PagingSource<Int, RoomBulkSMSHistoryItem>

    @Query(value = """
        select * from RoomBulkSMSHistoryItem where sender like '%' || :search || '%' or
         recipient like '%' || :search || '%' order by id asc
    """)
    abstract  fun getHistoryRecords(search: String): Flow<List<RoomBulkSMSHistoryItem>>

    @Query(value = """
        select * from RoomBulkSMSHistoryItem order by id asc limit :limit offset :offset  
    """)
    abstract  fun getHistoryRecords(
        limit: Int,
        offset: Int
    ): Flow<List<RoomBulkSMSHistoryItem>>

    @Query(value = "delete from RoomBulkSMSHistoryItem")
    abstract  suspend fun clear()

    @Query(value = "select count(id) from RoomBulkSMSHistoryItem")
    abstract fun getCount(): Flow<Long>

    @Query(value = "select * from RoomBulkSMSHistoryItem where id like :id")
    abstract suspend fun getHistoryRecordItem(id: Int): RoomBulkSMSHistoryItem?



}