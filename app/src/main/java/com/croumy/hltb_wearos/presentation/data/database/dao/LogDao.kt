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
    fun getAll(): List<LogEntity>

    @Insert
    fun insert(log: LogEntity)

    @Update
    fun update(log: LogEntity)

    @Delete
    fun delete(log: LogEntity)

    @Query("DELETE FROM LogEntity WHERE saved = TRUE")
    fun deleteAll()
}