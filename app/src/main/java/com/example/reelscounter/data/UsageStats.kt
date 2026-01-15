package com.example.reelscounter.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usage_stats")
data class UsageStats(
    @PrimaryKey
    val date: String, // Format: YYYY-MM-DD
    val reelsCount: Int = 0,
    val shortsCount: Int = 0,
    val totalTimeMillis: Long = 0L
)
