package com.kxtdev.bukkydatasup.common.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kxtdev.bukkydatasup.common.database.models.RoomAirtimeHistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AirtimeDao {

    @Upsert
    abstract  suspend fun insertAll(records: List<RoomAirtimeHistoryItem>)

    @Query(value = """
        select * from RoomAirtimeHistoryItem where mobile_number like '%' || :search || '%' or
         status like '%' || :search || '%' order by id desc
    """)
    abstract  fun getHistoryRecordsPaged(search: String): PagingSource<Int, RoomAirtimeHistoryItem>

    @Query(value = """
        select * from RoomAirtimeHistoryItem order by id desc limit :limit offset :offset  
    """)
    abstract  fun getHistoryRecordsPaged(limit: Int, offset: Int): PagingSource<Int, RoomAirtimeHistoryItem>

    @Query(value = """
        select * from RoomAirtimeHistoryItem where mobile_number like '%' || :search || '%' or
         status like '%' || :search || '%' order by id desc
    """)
    abstract  fun getHistoryRecords(search: String): Flow<List<RoomAirtimeHistoryItem>>

    @Query(value = """
        select * from RoomAirtimeHistoryItem order by id desc limit :limit offset :offset  
    """)
    abstract  fun getHistoryRecords(limit: Int, offset: Int): Flow<List<RoomAirtimeHistoryItem>>

    @Query(value = "delete from RoomAirtimeHistoryItem")
    abstract  suspend fun clear()

    @Query(value = "select count(id) from RoomAirtimeHistoryItem")
    abstract fun getCount(): Flow<Long>

    @Query(value = "select * from RoomAirtimeHistoryItem where id like :id")
    abstract suspend fun getHistoryRecordItem(id: Int): RoomAirtimeHistoryItem?


}