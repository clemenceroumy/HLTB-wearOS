package com.croumy.hltb_wearos.presentation.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.croumy.hltb_wearos.presentation.data.database.dao.LogDao
import com.croumy.hltb_wearos.presentation.data.database.entity.LogEntity
import com.croumy.hltb_wearos.presentation.helpers.extensions.DATETIME_FORMAT
import com.croumy.hltb_wearos.presentation.helpers.extensions.asString
import java.text.SimpleDateFormat
import java.util.Date

@Database(entities = [LogEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao
}

class DateConverter {
    @TypeConverter
    fun fromString(value: String?): Date? {
        return value?.let { SimpleDateFormat(DATETIME_FORMAT).parse(it) }
    }

    @TypeConverter
    fun dateToString(date: Date?): String? {
        return date?.asString()
    }
}