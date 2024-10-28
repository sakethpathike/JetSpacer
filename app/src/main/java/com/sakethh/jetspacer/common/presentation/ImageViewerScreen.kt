package com.sakethh.jetspacer.common.presentation

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.common.utils.jetSpacerLog
import kotlin.math.roundToInt

@Composable
fun ImageViewerScreen() {
    val context = LocalContext.current
    val offSetX = rememberSaveable {
        mutableFloatStateOf(0f)
    }
    val offSetY = rememberSaveable {
        mutableFloatStateOf(0f)
    }
    val scaleX = rememberSaveable {
        mutableFloatStateOf(1f)
    }
    val scaleY = rememberSaveable {
        mutableFloatStateOf(1f)
    }
    LaunchedEffect(scaleX.floatValue, scaleY.floatValue) {
        if (scaleX.floatValue == 1f && scaleY.floatValue == 1f) {
            offSetY.floatValue = 0f
            offSetX.floatValue = 0f
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("https://apod.nasa.gov/apod/image/2409/Chicagohenge_Artese_1320.jpg")
                .crossfade(true).build(),
            modifier = Modifier
                .offset {
                    IntOffset(
                        offSetX.floatValue.roundToInt(),
                        offSetY.floatValue.roundToInt()
                    )
                }
                .graphicsLayer(scaleX = scaleX.floatValue, scaleY = scaleY.floatValue)
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, rotation ->
                        scaleX.floatValue = (scaleX.floatValue * zoom).coerceIn(1f, 3f)
                        scaleY.floatValue = (scaleX.floatValue * zoom).coerceIn(1f, 3f)
                    }
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        change.consume()
                        if (scaleX.floatValue > 1f) {
                            offSetX.floatValue += (dragAmount * 3f)
                            jetSpacerLog("offSetX : " + offSetX.floatValue)
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, dragAmount ->
                        change.consume()
                        if (scaleX.floatValue > 1f) {
                            offSetY.floatValue += (dragAmount * 3f)
                            jetSpacerLog("offSetY : " + offSetY.floatValue)
                        }
                    }
                }, contentDescription = null
        )
    }
}