package com.example.reelscounter.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class UsageRepository(private val usageDao: UsageDao) {

    private fun getCurrentDate(): String {
        return LocalDate.now().toString()
    }

    val todayUsage: Flow<UsageStats?> = usageDao.getUsageFlowForDate(getCurrentDate())

    suspend fun incrementReels() {
        val date = getCurrentDate()
        val currentStats = usageDao.getUsageForDate(date)
        if (currentStats == null) {
            usageDao.insertOrUpdate(UsageStats(date = date, reelsCount = 1))
        } else {
            usageDao.incrementReels(date)
        }
    }

    suspend fun incrementShorts() {
        val date = getCurrentDate()
        val currentStats = usageDao.getUsageForDate(date)
        if (currentStats == null) {
            usageDao.insertOrUpdate(UsageStats(date = date, shortsCount = 1))
        } else {
            usageDao.incrementShorts(date)
        }
    }
}
