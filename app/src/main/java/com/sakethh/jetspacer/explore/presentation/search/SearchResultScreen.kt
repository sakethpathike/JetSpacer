package com.sakethh.jetspacer.explore.presentation.search

import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.common.presentation.utils.customRememberSavable
import com.sakethh.jetspacer.explore.domain.model.local.NASAImageLibrarySearchModifiedDTO
import com.sakethh.jetspacer.news.presentation.HeadlineDetailComponent
import kotlinx.serialization.json.Json

@Composable
fun SearchResultScreen(encodedNasaImageLibrarySearchModifiedDTO: String) {
    val searchResult = customRememberSavable {
        Json.decodeFromString<NASAImageLibrarySearchModifiedDTO>(
            encodedNasaImageLibrarySearchModifiedDTO
        )
    }
    val context = LocalContext.current
    val localUriHandler = LocalUriHandler.current
    val localClipboardManager = LocalClipboardManager.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp)
                .align(Alignment.BottomCenter)
        ) {
            Spacer(Modifier.height(50.dp))
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(searchResult.imageUrl)
                    .crossfade(true).build(),
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(top = 15.dp, bottom = 15.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .border(
                        1.5.dp, LocalContentColor.current.copy(0.25f), RoundedCornerShape(15.dp)
                    ), contentDescription = null
            )
            Text(
                text = searchResult.title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )
            if (searchResult.photographer.isNotBlank()) {
                Spacer(Modifier.height(15.dp))
                HeadlineDetailComponent(
                    string = searchResult.photographer, imageVector = Icons.Default.PhotoCamera
                )
            }
            if (searchResult.dateCreated.isNotBlank()) {
                Spacer(Modifier.height(if (searchResult.photographer.isNotBlank()) 5.dp else 10.dp))
                HeadlineDetailComponent(
                    string = searchResult.dateCreated, imageVector = Icons.Default.AccessTime
                )
            }
            if (searchResult.location.isNotBlank()) {
                Spacer(Modifier.height(if (searchResult.dateCreated.isNotBlank()) 5.dp else 10.dp))
                HeadlineDetailComponent(
                    string = searchResult.location, imageVector = Icons.Default.LocationOn
                )
            }

            Spacer(Modifier.height(15.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(15.dp))
            if (searchResult.title != searchResult.description) {
                Text(
                    text = searchResult.description,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                FilledTonalButton(onClick = {
                    localUriHandler.openUri("https://images.nasa.gov/details/${searchResult.nasaId}")
                }) {
                    Icon(Icons.Outlined.OpenInBrowser, null)
                }
                FilledTonalButton(onClick = {
                    localClipboardManager.setText(AnnotatedString("https://images.nasa.gov/details/${searchResult.nasaId}"))
                }) {
                    Icon(Icons.Outlined.ContentCopy, null)
                }
                FilledTonalButton(onClick = {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "https://images.nasa.gov/details/${searchResult.nasaId}"
                        )
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent, null)
                    context.startActivity(shareIntent)
                }) {
                    Icon(Icons.Outlined.Share, null)
                }
                FilledTonalButton(onClick = {}) {
                    Icon(Icons.Outlined.BookmarkAdd, null)
                }
            }
            Spacer(Modifier.height(5.dp))
            FilledTonalButton(
                onClick = {}, modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    painterResource(if (isSystemInDarkTheme()) R.drawable.instagram_white else R.drawable.instagram_black),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(5.dp))
                Text("Share via Instagram Stories", style = MaterialTheme.typography.titleSmall)
            }
            FilledTonalButton(
                onClick = {}, modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Default.FolderZip,
                    contentDescription = null,
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    "Download images in all resolutions",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}