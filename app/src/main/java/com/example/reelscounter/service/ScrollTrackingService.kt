package com.example.reelscounter.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.example.reelscounter.ReelsCounterApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import android.util.Log
import com.example.reelscounter.widget.ReelsCounterWidget

class ScrollTrackingService : AccessibilityService() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private var lastScrollTime = 0L
    private val SCROLL_DEBOUNCE = 1500L // Increased to 1.5s to filter quick scrolls

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString() ?: return
        
        // Filter: Only count scrolls from Scrollable containers (RecyclerView, ViewPager, etc)
        // This helps avoid counting scrolls on settings pages or comments if they use generic views.
        val className = event.className?.toString()
        val isScrollableContainer = className?.contains("RecyclerView") == true || 
                                    className?.contains("ViewPager") == true ||
                                    className?.contains("ListView") == true
                                    
        if (!isScrollableContainer && event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
             // If it's not a known scroll container, we might want to be cautious.
             // But for Shorts/Reels, the main feed is usually a RecyclerView or ViewPager.
             // Let's rely on the text check mostly, but this is a good secondary filter.
        }
        
        // Basic filtering
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastScrollTime < SCROLL_DEBOUNCE) {
                return
            }
            
            try {
                if (packageName == "com.instagram.android") {
                    handleInstagramScroll()
                    lastScrollTime = currentTime
                } else if (packageName == "com.google.android.youtube") {
                    handleYoutubeScroll()
                    lastScrollTime = currentTime
                }
            } catch (e: Exception) {
                Log.e("ScrollTrackingService", "Error processing scroll event", e)
            }
        }
    }

    private fun handleInstagramScroll() {
        val rootNode = rootInActiveWindow ?: return
        
        // Refined Detection: Look for "Reels" text on screen.
        // This confirms we are likely in the Reels UI or at least viewing Reel content.
        val reelsNodes = rootNode.findAccessibilityNodeInfosByText("Reels")
        val isReels = reelsNodes.isNotEmpty()
        
        if (isReels) {
             serviceScope.launch {
                val repo = (application as ReelsCounterApplication).repository
                repo.incrementReels()
                Log.d("ScrollTrackingService", "Instagram Reels Detected & Counted")
                ReelsCounterWidget.updateAllWidgets(this@ScrollTrackingService)
            }
        }
    }

    private fun handleYoutubeScroll() {
        val rootNode = rootInActiveWindow ?: return

        // Refined Detection: Look for "Shorts" text AND specific player elements like "Remix".
        // The "Shorts" tab text is always visible, causing false positives in Home feed.
        // "Remix" button text is usually present on the Shorts player screen but not on the Home feed.
        
        val shortsNodes = rootNode.findAccessibilityNodeInfosByText("Shorts")
        val isShortsTextPresent = shortsNodes.isNotEmpty()
        
        if (isShortsTextPresent) {
            // Secondary check: Look for "Remix" (case insensitive usually, but API is case insensitive by default)
            val remixNodes = rootNode.findAccessibilityNodeInfosByText("Remix")
            val isRemixPresent = remixNodes.isNotEmpty()
            
            // Alternative: Look for "Dislike" description if Remix isn't reliable, but Remix is good for now.
            // Also checking for "Subscriptions" being selected might help, but let's try positive reinforcement first.
            
            if (isRemixPresent) {
                 serviceScope.launch {
                    val repo = (application as ReelsCounterApplication).repository
                    repo.incrementShorts()
                    Log.d("ScrollTrackingService", "Youtube Shorts Detected & Counted (Confirmed by Remix)")
                    ReelsCounterWidget.updateAllWidgets(this@ScrollTrackingService)
                }
            }
        }
    }

    override fun onInterrupt() {
        // Service interrupted
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Cancel scope if needed, though service lifecycle is long
    }
}
