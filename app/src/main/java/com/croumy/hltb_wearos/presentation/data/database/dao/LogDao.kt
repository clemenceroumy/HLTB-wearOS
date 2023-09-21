package com.croumy.hltb_wearos.presentation.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.croumy.hltb_wearos.presentation.data.database.entity.LogEntity

@Dao
interface LogDao {
    @Query("SELECT * FROM LogEntity LIMIT 100")
    suspend fun getAll(): List<LogEntity>

    @Insert
    suspend fun insert(log: LogEntity)

    @Update
    suspend fun update(log: LogEntity)

    @Delete
    suspend fun delete(log: LogEntity)

    @Query("DELETE FROM LogEntity WHERE saved = TRUE")
    suspend fun deleteAll()
}