package com.kxtdev.bukkydatasup.common.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kxtdev.bukkydatasup.common.database.models.RoomResultCheckerHistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ResultCheckerDao {

    @Upsert
    abstract  suspend fun insertAll(records: List<RoomResultCheckerHistoryItem>)

    @Query(
        value = """
        select * from RoomResultCheckerHistoryItem where exam_name like '%' || :search || '%' or
         status like '%' || :search || '%' order by id desc
    """
    )
    abstract  fun getHistoryRecordsPaged(search: String): PagingSource<Int, RoomResultCheckerHistoryItem>

    @Query(
        value = """
        select * from RoomResultCheckerHistoryItem order by id desc limit :limit offset :offset  
    """
    )
    abstract  fun getHistoryRecordsPaged(
        limit: Int,
        offset: Int
    ): PagingSource<Int, RoomResultCheckerHistoryItem>


    @Query(
        value = """
        select * from RoomResultCheckerHistoryItem where exam_name like '%' || :search || '%' or
         status like '%' || :search || '%' order by id desc
    """
    )
    abstract  fun getHistoryRecords(search: String): Flow<List<RoomResultCheckerHistoryItem>>

    @Query(
        value = """
        select * from RoomResultCheckerHistoryItem order by id desc limit :limit offset :offset  
    """
    )
    abstract  fun getHistoryRecords(
        limit: Int,
        offset: Int
    ): Flow<List<RoomResultCheckerHistoryItem>>


    @Query(value = "delete from RoomResultCheckerHistoryItem")
    abstract  suspend fun clear()

    @Query(value = "select count(id) from RoomResultCheckerHistoryItem")
    abstract fun getCount(): Flow<Long>

    @Query(value = "select * from RoomResultCheckerHistoryItem where id like :id")
    abstract suspend fun getHistoryRecordItem(id: Int): RoomResultCheckerHistoryItem?


}