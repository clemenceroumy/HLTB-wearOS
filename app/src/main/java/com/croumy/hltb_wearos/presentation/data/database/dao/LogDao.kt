package com.croumy.hltb_wearos.presentation.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.croumy.hltb_wearos.presentation.data.database.entity.LogEntity

@Dao
interface LogDao {
    @Query("SELECT * FROM LogEntity")
    fun getAll(): List<LogEntity>

    @Insert
    fun insert(log: LogEntity)
}