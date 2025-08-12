package com.sakethh.jetspacer.ui.utils

import android.app.DownloadManager
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import androidx.core.net.toUri

fun Modifier.iconModifier(colorScheme: ColorScheme, onClick: () -> Unit): Modifier {
    return this
        .padding(start = 5.dp, end = 5.dp)
        .clip(RoundedCornerShape(15.dp))
        .clickable {
            onClick()
        }
        .background(colorScheme.primary.copy(0.1f))
        .padding(10.dp)
        .size(20.dp)
}

suspend fun fetchSwatchesFromUrl(context: Context, url: String): Palette? {
    val loader = ImageLoader(context)
    val request = ImageRequest.Builder(context).data(url).allowHardware(false).build()

    val result = loader.execute(request)
    val bitmap = (result.drawable as? BitmapDrawable)?.bitmap
    return if (bitmap == null) null else Palette.from(bitmap).generate()
}

fun downloadImage(context: Context,imgURL: String,fileName: String,description: String){
    val downloadRequest = DownloadManager.Request(imgURL.toUri())
        .setTitle(fileName)
        .setDescription(description)
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName)

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(downloadRequest)
}