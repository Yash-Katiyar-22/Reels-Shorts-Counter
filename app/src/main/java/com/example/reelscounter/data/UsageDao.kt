package com.example.reelscounter.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageDao {
    @Query("SELECT * FROM usage_stats WHERE date = :date")
    suspend fun getUsageForDate(date: String): UsageStats?

    @Query("SELECT * FROM usage_stats WHERE date = :date")
    fun getUsageFlowForDate(date: String): Flow<UsageStats?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(stats: UsageStats)
    
    @Query("UPDATE usage_stats SET reelsCount = reelsCount + 1 WHERE date = :date")
    suspend fun incrementReels(date: String)

    @Query("UPDATE usage_stats SET shortsCount = shortsCount + 1 WHERE date = :date")
    suspend fun incrementShorts(date: String)
    
    // Fallback if record doesn't exist, we might need a transaction to check and insert
}
