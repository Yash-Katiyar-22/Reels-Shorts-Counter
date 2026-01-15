package com.example.reelscounter

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reelscounter.service.ScrollTrackingService
import com.example.reelscounter.ui.DashboardScreen
import com.example.reelscounter.ui.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val app = application as ReelsCounterApplication
            val viewModel: MainViewModel = viewModel(
                factory = MainViewModel.Factory(app.repository)
            )
            
            var isServiceEnabled by remember { mutableStateOf(checkAccessibilityPermission()) }
            var hasUsagePermission by remember { mutableStateOf(com.example.reelscounter.util.UsageStatsHelper.hasUsageStatsPermission(this)) }
            
            // Re-check permission on resume
            OnLifecycleEvent { event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    isServiceEnabled = checkAccessibilityPermission()
                    hasUsagePermission = com.example.reelscounter.util.UsageStatsHelper.hasUsageStatsPermission(this)
                    if (hasUsagePermission) {
                        viewModel.refreshUsageStats(this)
                    }
                }
            }

            DashboardScreen(
                viewModel = viewModel,
                isServiceEnabled = isServiceEnabled,
                hasUsagePermission = hasUsagePermission,
                onEnableServiceClick = {
                    startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                },
                onEnableUsageClick = {
                    startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                }
            )
        }
    }

    private fun checkAccessibilityPermission(): Boolean {
        val am = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        for (service in enabledServices) {
            if (service.resolveInfo.serviceInfo.packageName == packageName &&
                service.resolveInfo.serviceInfo.name == ScrollTrackingService::class.java.name
            ) {
                return true
            }
        }
        return false
    }
}

@Composable
fun OnLifecycleEvent(onEvent: (Lifecycle.Event) -> Unit) {
    val eventHandler = remember { onEvent }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            eventHandler(event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
