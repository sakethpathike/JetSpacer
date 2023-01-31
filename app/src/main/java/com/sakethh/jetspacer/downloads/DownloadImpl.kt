package com.sakethh.jetspacer.downloads

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

interface DownloadService {
fun downloadNewFile(title:String,url:String):Long
}

class DownloadImpl (context:Context):DownloadService{
    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    override fun downloadNewFile(title: String,url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/jpeg")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setTitle(title)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,title)

        return downloadManager.enqueue(request)
    }

}