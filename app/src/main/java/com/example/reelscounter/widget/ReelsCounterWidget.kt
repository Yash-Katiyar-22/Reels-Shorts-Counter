package com.example.reelscounter.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.reelscounter.R
import com.example.reelscounter.ReelsCounterApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class ReelsCounterWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                val app = context.applicationContext as ReelsCounterApplication
                val today = LocalDate.now().toString()
                val stats = app.database.usageDao().getUsageForDate(today)
                val reels = stats?.reelsCount ?: 0
                val shorts = stats?.shortsCount ?: 0
                
                // Fetch Usage Stats
                val instaTime = com.example.reelscounter.util.UsageStatsHelper.getDailyUsageDuration(context, "com.instagram.android")
                val youtubeTime = com.example.reelscounter.util.UsageStatsHelper.getDailyUsageDuration(context, "com.google.android.youtube")

                val views = RemoteViews(context.packageName, R.layout.widget_reels_counter)
                views.setTextViewText(R.id.appwidget_reels_count, reels.toString())
                views.setTextViewText(R.id.appwidget_shorts_count, shorts.toString())
                
                views.setTextViewText(R.id.appwidget_reels_time, formatTime(instaTime))
                views.setTextViewText(R.id.appwidget_shorts_time, formatTime(youtubeTime))

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
        
        private fun formatTime(timeMillis: Long): String {
             if (timeMillis <= 0) return "0m"
             val hours = timeMillis / 1000 / 3600
             val minutes = (timeMillis / 1000 % 3600) / 60
             return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
        }
        
        fun updateAllWidgets(context: Context) {
             val appWidgetManager = AppWidgetManager.getInstance(context)
             val ids = appWidgetManager.getAppWidgetIds(
                 android.content.ComponentName(context, ReelsCounterWidget::class.java)
             )
             for (id in ids) {
                 updateAppWidget(context, appWidgetManager, id)
             }
        }
    }
}
