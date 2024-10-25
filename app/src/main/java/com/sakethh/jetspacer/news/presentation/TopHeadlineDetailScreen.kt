package com.sakethh.jetspacer.news.presentation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Public
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
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.news.domain.model.Article
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopHeadlineDetailScreen(encodedString: String) {
    val article = rememberSaveable(saver = ArticleSaver) {
        Json.decodeFromString<Article>(encodedString)
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
                    .data(article.urlToImage)
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
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (article.author.isNotBlank()) 15.dp else 0.dp)
            )
            if (article.author.isNotBlank()) {
                HeadlineDetailComponent(
                    string = article.author, imageVector = Icons.Default.PersonOutline
                )
            }
            if (article.source.name.isNotBlank()) {
                Spacer(Modifier.height(if (article.author.isNotBlank()) 5.dp else 15.dp))
                HeadlineDetailComponent(
                    string = article.source.name, imageVector = Icons.Default.Public
                )
            }
            if (article.publishedAt.isNotBlank()) {
                Spacer(Modifier.height(if (article.source.name.isNotBlank()) 5.dp else 15.dp))
                HeadlineDetailComponent(
                    string = article.publishedAt, imageVector = Icons.Default.AccessTime
                )
            }
            Spacer(Modifier.height(15.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(15.dp))
            Text(
                text = article.description,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                FilledTonalButton(onClick = {
                    localUriHandler.openUri(article.url)
                }) {
                    Icon(Icons.Outlined.OpenInBrowser, null)
                }
                FilledTonalButton(onClick = {
                    localClipboardManager.setText(AnnotatedString(article.url))
                }) {
                    Icon(Icons.Outlined.ContentCopy, null)
                }
                FilledTonalButton(onClick = {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, article.url)
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
                    .navigationBarsPadding()
            ) {
                Icon(
                    painterResource(if (isSystemInDarkTheme()) R.drawable.instagram_white else R.drawable.instagram_black),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(5.dp))
                Text("Share via Instagram Stories", style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}

@Composable
fun HeadlineDetailComponent(
    string: String,
    imageVector: ImageVector,
    fontSize: TextUnit = 16.sp,
    iconSize: Dp = 24.dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .clip(
                RoundedCornerShape(15.dp)
            )
            .background(MaterialTheme.colorScheme.primary.copy(0.1f))
            .padding(5.dp)
    ) {
        Spacer(Modifier.width(5.dp))
        Icon(imageVector, contentDescription = null, modifier = Modifier.size(iconSize))
        Spacer(Modifier.width(5.dp))
        Text(
            text = string, style = MaterialTheme.typography.titleSmall, fontSize = fontSize
        )
        Spacer(Modifier.width(5.dp))
    }
}

private val ArticleSaver = Saver<Article, String>(
    save = { Json.encodeToString(it) },
    restore = { Json.decodeFromString<Article>(it) }
)