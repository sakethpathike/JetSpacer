package com.sakethh.jetspacer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

class Coil_Image {
    @Composable
    fun CoilImage(
        imgURL: String,
        contentDescription: String,
        modifier: Modifier,
        onError: Painter,
        contentScale: ContentScale = ContentScale.Fit,
        onSuccess: (() -> Unit)? = null,
        onLoading: (() -> Unit)? = null
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imgURL)
                .crossfade(true).build(),
            contentDescription = contentDescription,
            modifier = modifier,
            error = onError,
            contentScale = contentScale,
            onSuccess = { onSuccess?.invoke() },
            onLoading = { onLoading?.invoke() }
        )
    }
}