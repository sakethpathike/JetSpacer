package com.sakethh.jetspacer.ui.utils

import android.app.DownloadManager
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.palette.graphics.Palette
import coil3.ImageLoader
import coil3.asDrawable
import coil3.request.ImageRequest
import coil3.request.allowHardware

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

suspend fun retrievePaletteFromUrl(context: Context, url: String): Palette? {
    val loader = ImageLoader(context)
    val request = ImageRequest.Builder(context).data(url).allowHardware(false).build()

    val result = loader.execute(request)
    val bitmap = (result.image?.asDrawable(Resources.getSystem()) as? BitmapDrawable)?.bitmap
    return if (bitmap == null) null else Palette.from(bitmap).generate()
}

fun generateColorPaletteList(palette: Palette): List<Color> {
    return buildList {
        palette.vibrantSwatch?.rgb?.let { add(Color(it)) }
        palette.lightVibrantSwatch?.rgb?.let { add(Color(it)) }
        palette.mutedSwatch?.rgb?.let { add(Color(it)) }
        palette.darkMutedSwatch?.rgb?.let { add(Color(it)) }
    }
}

fun downloadImage(context: Context, imgURL: String, fileName: String, description: String) {
    val downloadRequest =
        DownloadManager.Request(imgURL.toUri()).setTitle(fileName).setDescription(description)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName)

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(downloadRequest)
}