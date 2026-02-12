package com.kxtdev.bukkydatasup.common.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kxtdev.bukkydatasup.common.database.models.RoomCableHistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CableDao {

    @Upsert
    abstract  suspend fun insertAll(records: List<RoomCableHistoryItem>)

    @Query(value = """
        select * from RoomCableHistoryItem where smart_card_number like '%' || :search || '%' or
         status like '%' || :search || '%' order by id desc
    """)
    abstract  fun getHistoryRecordsPaged(search: String): PagingSource<Int, RoomCableHistoryItem>

    @Query(value = """
        select * from RoomCableHistoryItem order by id desc limit :limit offset :offset  
    """)
    abstract  fun getHistoryRecordsPaged(
        limit: Int,
        offset: Int
    ): PagingSource<Int, RoomCableHistoryItem>


    @Query(value = """
        select * from RoomCableHistoryItem where smart_card_number like '%' || :search || '%' or
         status like '%' || :search || '%' order by id desc
    """)
    abstract  fun getHistoryRecords(search: String): Flow<List<RoomCableHistoryItem>>

    @Query(value = """
        select * from RoomCableHistoryItem order by id desc limit :limit offset :offset  
    """)
    abstract  fun getHistoryRecords(
        limit: Int,
        offset: Int
    ): Flow<List<RoomCableHistoryItem>>


    @Query(value = "delete from RoomCableHistoryItem")
    abstract  suspend fun clear()

    @Query(value = "select count(id) from RoomCableHistoryItem")
    abstract fun getCount(): Flow<Long>

    @Query(value = "select * from RoomCableHistoryItem where id like :id")
    abstract suspend fun getHistoryRecordItem(id: Int): RoomCableHistoryItem?


}