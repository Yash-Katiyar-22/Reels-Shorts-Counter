package com.example.reelscounter.util

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.example.reelscounter.BuildConfig

object UpdateManager {

    // TODO: REPLACE WITH YOUR GITHUB REPO DETAILS
    private const val GITHUB_OWNER = "Yash-Katiyar-22" 
    private const val GITHUB_REPO = "Reels-Shorts-Counter"
    
    // Example: https://api.github.com/repos/yashk/insta-scroller/releases/latest
    private const val GITHUB_API_URL = "https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/releases/latest"

    fun checkForUpdates(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(GITHUB_API_URL)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode == 200) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = reader.readText()
                    reader.close()

                    val json = JSONObject(response)
                    val latestTag = json.getString("tag_name") // e.g., "v1.2"
                    
                    // Basic version comparison (assumes format v1.0)
                    // You should implement robust semver parsing for real production apps
                    val currentVersion = "v${BuildConfig.VERSION_NAME}" 
                    
                    Log.d("UpdateManager", "Current: $currentVersion, Latest: $latestTag")

                    if (latestTag != currentVersion) {
                        // Found new version
                        val assets = json.getJSONArray("assets")
                        if (assets.length() > 0) {
                            val apkUrl = assets.getJSONObject(0).getString("browser_download_url")
                            withContext(Dispatchers.Main) {
                                showUpdateAvailable(context, apkUrl, latestTag)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("UpdateManager", "Update check failed", e)
            }
        }
    }

    private fun showUpdateAvailable(context: Context, downloadUrl: String, version: String) {
        // Simple Toast for now, ideally a Dialog
        Toast.makeText(context, "Update Available: $version. Downloading...", Toast.LENGTH_LONG).show()
        downloadAndInstall(context, downloadUrl)
    }

    private fun downloadAndInstall(context: Context, url: String) {
        val destination = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/update.apk"
        val uri = Uri.parse("file://$destination")
        
        // Delete old file if exists
        val file = File(destination)
        if (file.exists()) file.delete()

        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle("App Update")
        request.setDescription("Downloading version...")
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "update.apk")
        request.setMimeType("application/vnd.android.package-archive")

        val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = manager.enqueue(request)

        // Listen for completion
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context, intent: Intent) {
                if (downloadId == intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)) {
                    context.unregisterReceiver(this)
                    installApk(context, file)
                }
            }
        }
        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE), Context.RECEIVER_NOT_EXPORTED)
    }

    private fun installApk(context: Context, file: File) {
        try {
            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
             Log.e("UpdateManager", "Install failed", e)
             Toast.makeText(context, "Install failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
