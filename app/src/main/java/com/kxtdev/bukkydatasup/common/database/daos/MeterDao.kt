package com.kxtdev.bukkydatasup.common.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kxtdev.bukkydatasup.common.database.models.RoomMeterHistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MeterDao {

    @Upsert
    abstract  suspend fun insertAll(records: List<RoomMeterHistoryItem>)

    @Query(value = """
        select * from RoomMeterHistoryItem where meter_number like '%' || :search || '%' or
         status like '%' || :search || '%' order by id desc
    """)
    abstract  fun getHistoryRecordsPaged(search: String): PagingSource<Int, RoomMeterHistoryItem>

    @Query(value = """
        select * from RoomMeterHistoryItem order by id desc limit :limit offset :offset  
    """)
    abstract  fun getHistoryRecordsPaged(
        limit: Int,
        offset: Int
    ): PagingSource<Int, RoomMeterHistoryItem>


    @Query(value = """
        select * from RoomMeterHistoryItem where meter_number like '%' || :search || '%' or
         status like '%' || :search || '%' order by id desc
    """)
    abstract  fun getHistoryRecords(search: String): Flow<List<RoomMeterHistoryItem>>

    @Query(value = """
        select * from RoomMeterHistoryItem order by id desc limit :limit offset :offset  
    """)
    abstract  fun getHistoryRecords(
        limit: Int,
        offset: Int
    ): Flow<List<RoomMeterHistoryItem>>

    @Query(value = "delete from RoomMeterHistoryItem")
    abstract  suspend fun clear()

    @Query(value = "select count(id) from RoomMeterHistoryItem")
    abstract fun getCount(): Flow<Long>

    @Query(value = "select * from RoomMeterHistoryItem where id like :id")
    abstract suspend fun getHistoryRecordItem(id: Int): RoomMeterHistoryItem?


}