package com.kxtdev.bukkydatasup.common.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kxtdev.bukkydatasup.common.database.models.RoomDataPlan
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DataPlanDao {

    @Upsert
    abstract suspend fun insertAll(records: List<RoomDataPlan>)

    @Query(value = """
        select * from RoomDataPlan where network_id like :networkId and plan_type like :planType
            order by CAST(plan_amount as DECIMAL(10,2)) asc
    """)
    abstract fun getAll(networkId: Int, planType: String): Flow<List<RoomDataPlan>>

    @Query(value = """
        select * from RoomDataPlan where network_id like :networkId 
            order by CAST(plan_amount as DECIMAL(10,2)) asc
    """)
    abstract fun getAll(networkId: Int): Flow<List<RoomDataPlan>>

    @Query(value = "delete from RoomDataPlan")
    abstract suspend fun clear()

}