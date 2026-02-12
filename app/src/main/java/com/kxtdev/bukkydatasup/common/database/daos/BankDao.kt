package com.kxtdev.bukkydatasup.common.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kxtdev.bukkydatasup.common.database.models.RoomBank
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BankDao {

    @Upsert
    abstract suspend fun insertAll(records: List<RoomBank>)

    @Query(value = """
        select * from RoomBank order by id asc  
    """)
    abstract fun getAll(): Flow<List<RoomBank>>

    @Query(value = "delete from RoomBank")
    abstract suspend fun clear()
}