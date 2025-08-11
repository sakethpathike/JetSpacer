package com.sakethh.jetspacer.ui.screens.explore.search

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.domain.model.NASAImageLibrarySearchModifiedDTO
import com.sakethh.jetspacer.ui.components.pulsateEffect

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchResultComponent(
    palette: List<Color>,
    nasaImageLibrarySearchModifiedDTO: NASAImageLibrarySearchModifiedDTO,
    onItemClick: () -> Unit
) {
    val context = LocalContext.current
    val paletteContainsMultipleColors = rememberSaveable {
        palette.size >= 2
    }
    Column(
        Modifier
            .padding(4.dp)
            .pulsateEffect()
            .clip(RoundedCornerShape(15.dp))
            .then(
                if (paletteContainsMultipleColors) Modifier.background(
                    alpha = 0.15f, brush = Brush.verticalGradient(colors = palette)
                ) else Modifier.background(
                    CardDefaults.cardColors().containerColor
                )
            )
            .clickable { onItemClick() }
            .animateContentSize()) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(nasaImageLibrarySearchModifiedDTO.imageUrl)
                .crossfade(true).build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEnd = 15.dp, bottomStart = 15.dp))
                .border(
                    width = 1.5.dp,
                    color = (if (paletteContainsMultipleColors) palette.first() else LocalContentColor.current).copy(
                        0.5f
                    ),
                    shape = RoundedCornerShape(15.dp)
                )
                .animateContentSize()
                .wrapContentHeight(),
            contentScale = ContentScale.Crop
        )
        Text(
            text = nasaImageLibrarySearchModifiedDTO.title,
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis,
            fontSize = 15.sp
        )
    }
}