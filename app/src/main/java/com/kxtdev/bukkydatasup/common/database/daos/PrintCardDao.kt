package com.kxtdev.bukkydatasup.common.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kxtdev.bukkydatasup.common.database.models.RoomPrintCardHistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PrintCardDao {

    @Upsert
    abstract  suspend fun insertAll(records: List<RoomPrintCardHistoryItem>)

    @Query(value = """
        select * from RoomPrintCardHistoryItem where name_on_card like '%' || :search || '%' or
         status like '%' || :search || '%' order by id desc
    """)
    abstract  fun getHistoryRecordsPaged(search: String): PagingSource<Int, RoomPrintCardHistoryItem>

    @Query(value = """
        select * from RoomPrintCardHistoryItem order by id desc limit :limit offset :offset  
    """)
    abstract  fun getHistoryRecordsPaged(
        limit: Int,
        offset: Int
    ): PagingSource<Int, RoomPrintCardHistoryItem>

    @Query(value = """
        select * from RoomPrintCardHistoryItem where name_on_card like '%' || :search || '%' or
         status like '%' || :search || '%' order by id desc
    """)
    abstract  fun getHistoryRecords(search: String): Flow<List<RoomPrintCardHistoryItem>>

    @Query(value = """
        select * from RoomPrintCardHistoryItem order by id desc limit :limit offset :offset  
    """)
    abstract  fun getHistoryRecords(
        limit: Int,
        offset: Int
    ): Flow<List<RoomPrintCardHistoryItem>>

    @Query(value = "delete from RoomPrintCardHistoryItem")
    abstract  suspend fun clear()

    @Query(value = "select count(id) from RoomPrintCardHistoryItem")
    abstract fun getCount(): Flow<Long>

    @Query(value = "select * from RoomPrintCardHistoryItem where id like :id")
    abstract suspend fun getHistoryRecordItem(id: Int): RoomPrintCardHistoryItem?



}