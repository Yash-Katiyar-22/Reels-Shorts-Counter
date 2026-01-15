package com.example.reelscounter

import android.app.Application
import com.example.reelscounter.data.AppDatabase
import com.example.reelscounter.data.UsageRepository

import com.google.android.gms.ads.MobileAds

class ReelsCounterApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { UsageRepository(database.usageDao()) }

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
    }
}
