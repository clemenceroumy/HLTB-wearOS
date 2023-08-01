package com.croumy.hltb_wearos.presentation.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class LogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val gameId: Int,
    val submissionId: Int,
    val timePlayed: Long, // AS MILLISECONDS
    val date: Date,
    val saved: Boolean,
    val title: String,
    val platform: String,
    val storefront: String,
    val progress: Long,
    val progressBefore: Long
)