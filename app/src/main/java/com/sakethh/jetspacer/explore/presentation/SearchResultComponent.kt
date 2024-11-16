package com.sakethh.jetspacer.explore.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.explore.domain.model.local.NASAImageLibrarySearchModifiedDTO

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchResultComponent(
    nasaImageLibrarySearchModifiedDTO: NASAImageLibrarySearchModifiedDTO,
    onItemClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    Column(
        Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary.copy(0.5f),
                shape = RoundedCornerShape(5.dp)
            )
            .clickable { onItemClick() }
            .animateContentSize()
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(nasaImageLibrarySearchModifiedDTO.imageUrl)
                    .crossfade(true).build(),
                contentDescription = null,
                modifier = Modifier
                    .animateContentSize()
                    .wrapContentHeight()
            )
        }
        Text(
            text = nasaImageLibrarySearchModifiedDTO.title,
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.titleSmall,
            overflow = TextOverflow.Ellipsis, fontSize = 12.sp
        )
        FlowRow(modifier = Modifier.padding(end = 10.dp)) {
            nasaImageLibrarySearchModifiedDTO.keywords.forEach {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(
                            start = 10.dp, top = 5.dp,
                        )
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(0.25f),
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(5.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 10.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(Modifier.height(15.dp))
    }
}