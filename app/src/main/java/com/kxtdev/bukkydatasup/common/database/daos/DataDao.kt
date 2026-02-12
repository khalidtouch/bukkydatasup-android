package com.kxtdev.bukkydatasup.common.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kxtdev.bukkydatasup.common.database.models.RoomDataHistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DataDao {

    @Upsert
    abstract  suspend fun insertAll(records: List<RoomDataHistoryItem>)

    @Query(value = """
        select * from RoomDataHistoryItem where mobile_number like '%' || :search || '%' or
         status like '%' || :search || '%' order by id desc
    """)
    abstract  fun getHistoryRecordsPaged(search: String): PagingSource<Int, RoomDataHistoryItem>

    @Query(value = """
        select * from RoomDataHistoryItem order by id desc limit :limit offset :offset  
    """)
    abstract  fun getHistoryRecordsPaged(
        limit: Int,
        offset: Int
    ): PagingSource<Int, RoomDataHistoryItem>

    @Query(value = """
        select * from RoomDataHistoryItem where mobile_number like '%' || :search || '%' or
         status like '%' || :search || '%' order by id desc
    """)
    abstract  fun getHistoryRecords(search: String): Flow<List<RoomDataHistoryItem>>

    @Query(value = """
        select * from RoomDataHistoryItem order by id desc limit :limit offset :offset  
    """)
    abstract  fun getHistoryRecords(
        limit: Int,
        offset: Int
    ): Flow<List<RoomDataHistoryItem>>

    @Query(value = "delete from RoomDataHistoryItem")
    abstract  suspend fun clear()

    @Query(value = "select count(id) from RoomDataHistoryItem")
    abstract fun getCount(): Flow<Long>

    @Query(value = "select * from RoomDataHistoryItem where id like :id")
    abstract suspend fun getHistoryRecordItem(id: Int): RoomDataHistoryItem?


}