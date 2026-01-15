package com.example.reelscounter.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reelscounter.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalTextApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    isServiceEnabled: Boolean,
    hasUsagePermission: Boolean,
    onEnableServiceClick: () -> Unit,
    onEnableUsageClick: () -> Unit
) {
    val stats by viewModel.todayStats.collectAsState()
    val instagramTime by viewModel.instagramTime.collectAsState()
    val youtubeTime by viewModel.youtubeTime.collectAsState()
    
    val reelsCount = stats?.reelsCount ?: 0
    val shortsCount = stats?.shortsCount ?: 0
    val totalCount = reelsCount + shortsCount
    
    // Formatting date using standard Java Time
    val todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("EEE, MMM dd"))

    Scaffold(
        containerColor = Color(0xFF121212)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Reels & Shorts Counter",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Date
            Text(
                text = "Today, $todayDate",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Main Summary Card
            Card(
                modifier = Modifier.fillMaxWidth().height(180.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Brush.linearGradient(listOf(Color(0xFF00E5FF), Color(0xFF2979FF)))),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Gradient Text for Count
                    Text(
                        text = "$totalCount",
                        style = TextStyle(
                            fontSize = 80.sp,
                            fontWeight = FontWeight.Bold,
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF00E5FF), Color(0xFF2979FF))
                            )
                        )
                    )
                    Text(
                        text = "Total Items Scrolled Today",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Instagram Card
                ModernStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Reels Viewed",
                    count = reelsCount,
                    timeSpent = instagramTime,
                    iconRes = R.drawable.ic_instagram_gradient,
                    borderColor = Color(0xFF833AB4) // Purple/Pinkish
                )

                // YouTube Card
                ModernStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Shorts Viewed",
                    count = shortsCount,
                    timeSpent = youtubeTime,
                    iconRes = R.drawable.ic_youtube_red,
                    borderColor = Color(0xFFFF0000) // Red
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Progress Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A1A1A), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Daily Scrolling Level",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                // Custom Progress Bar with Gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF333333))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = (totalCount / 500f).coerceIn(0.05f, 1f)) // Mock target of 500
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF00E5FF), Color(0xFF2979FF))
                                )
                            )
                    )
                }
            }
            
            // Permission Alert (Subtle)
            if (!isServiceEnabled) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onEnableServiceClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C)),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFFF5252))
                ) {
                    Text("⚠️ Enable Tracking Service", color = Color(0xFFFF5252))
                }
            }

             // Usage Permission Alert
            if (!hasUsagePermission) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onEnableUsageClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C2C)),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFFFA000))
                ) {
                    Text("⚠️ Enable Usage Access", color = Color(0xFFFFA000))
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // AdMob Banner
            AdMobBanner()
        }
    }
}

@Composable
fun ModernStatCard(
    modifier: Modifier = Modifier,
    title: String,
    count: Int,
    timeSpent: Long = 0L,
    iconRes: Int,
    borderColor: Color
) {
    val timeString = if (timeSpent > 0) {
        val hours = timeSpent / 1000 / 3600
        val minutes = (timeSpent / 1000 % 3600) / 60
        if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    } else {
        "0m"
    }

    Card(
        modifier = modifier.aspectRatio(0.8f), // Slightly taller for time info
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, borderColor.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = androidx.compose.ui.res.painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "$count",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
             Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = timeString,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Time Spent",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AdMobBanner() {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-5492909060411264/5956168059"
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
