package com.kxtdev.bukkydatasup.common.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kxtdev.bukkydatasup.common.database.models.RoomUser
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao {

    @Upsert
    abstract suspend fun saveUserOrUpdate(user: RoomUser)

    @Query("select * from RoomUser where username like :username")
    abstract fun getLoggedInUser(username: String): Flow<RoomUser?>

    @Query("delete from RoomUser")
    abstract suspend fun clear()

}