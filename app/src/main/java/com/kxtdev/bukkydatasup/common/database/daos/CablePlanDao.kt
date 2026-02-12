package com.kxtdev.bukkydatasup.common.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kxtdev.bukkydatasup.common.database.models.RoomCablePlan
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CablePlanDao {

    @Upsert
    abstract suspend fun insertAll(records: List<RoomCablePlan>)

    @Query(
        value = """
         select * from RoomCablePlan where cable_name like :cableName
         order by CAST(plan_amount as DECIMAL(10,2)) asc
     """
    )
    abstract fun getAll(cableName: String): Flow<List<RoomCablePlan>>

    @Query(value = "delete from RoomCablePlan")
    abstract suspend fun clear()

}