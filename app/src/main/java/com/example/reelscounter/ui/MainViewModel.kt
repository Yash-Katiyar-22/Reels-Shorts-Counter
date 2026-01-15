package com.example.reelscounter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.reelscounter.data.UsageRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainViewModel(private val repository: UsageRepository) : ViewModel() {

    val todayStats = repository.todayUsage.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private val _instagramTime = kotlinx.coroutines.flow.MutableStateFlow(0L)
    val instagramTime = _instagramTime.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    private val _youtubeTime = kotlinx.coroutines.flow.MutableStateFlow(0L)
    val youtubeTime = _youtubeTime.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    fun refreshUsageStats(context: android.content.Context) {
        _instagramTime.value = com.example.reelscounter.util.UsageStatsHelper.getDailyUsageDuration(context, "com.instagram.android")
        _youtubeTime.value = com.example.reelscounter.util.UsageStatsHelper.getDailyUsageDuration(context, "com.google.android.youtube")
    }

    class Factory(private val repository: UsageRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
