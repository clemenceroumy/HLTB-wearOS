package com.croumy.hltb_wearos.presentation.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.soywiz.klock.Time
import com.soywiz.klock.TimeSpan
import java.util.Date

@Entity
data class LogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var gameId: Int,
    var submissionId: Int,
    var timePlayed: Long, // AS MILLISECONDS
    var date: Date,
    var saved: Boolean,
    var title: String,
    var platform: String,
    var storefront: String,
    var progress: Long,
    var progressBefore: Long
) {
    val timePlayedTime: Time get() = Time(TimeSpan(timePlayed.toDouble()))
}