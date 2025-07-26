package com.sakethh.jetspacer.ui.screens.headlines

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.domain.model.Headline
import com.sakethh.jetspacer.ui.utils.customRememberSavable
import kotlinx.serialization.json.Json

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopHeadlineDetailScreen(encodedString: String) {
    val headline = customRememberSavable {
        Json.decodeFromString<Headline>(encodedString)
    }
    val isBookmarked = rememberSaveable {
        mutableStateOf(headline.isBookmarked)
    }
    val topHeadlineDetailScreenViewmodel: TopHeadlineDetailScreenViewmodel = viewModel()
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
                model = ImageRequest.Builder(context).data(headline.imageUrl).crossfade(true)
                    .build(),
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(top = 15.dp, bottom = 15.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .border(
                        1.5.dp, LocalContentColor.current.copy(0.25f), RoundedCornerShape(15.dp)
                    ),
                contentDescription = null
            )
            Text(
                text = headline.title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (headline.author.isNotBlank()) 15.dp else 0.dp)
            )
            if (headline.author.isNotBlank()) {
                HeadlineDetailComponent(
                    string = headline.author, imageVector = Icons.Default.PersonOutline
                )
            }
            if (headline.sourceName.isNotBlank()) {
                Spacer(Modifier.height(if (headline.author.isNotBlank()) 5.dp else 15.dp))
                HeadlineDetailComponent(
                    string = headline.sourceName, imageVector = Icons.Default.Public
                )
            }
            if (headline.publishedAt.isNotBlank()) {
                Spacer(Modifier.height(if (headline.sourceName.isNotBlank()) 5.dp else 15.dp))
                HeadlineDetailComponent(
                    string = headline.publishedAt, imageVector = Icons.Default.AccessTime
                )
            }
            Spacer(Modifier.height(15.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(15.dp))
            Text(
                text = headline.description,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                FilledTonalButton(onClick = {
                    localUriHandler.openUri(headline.url)
                }) {
                    Icon(Icons.Outlined.OpenInBrowser, null)
                }
                FilledTonalButton(onClick = {
                    localClipboardManager.setText(AnnotatedString(headline.url))
                }) {
                    Icon(Icons.Outlined.ContentCopy, null)
                }
                FilledTonalButton(onClick = {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, headline.url)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent, null)
                    context.startActivity(shareIntent)
                }) {
                    Icon(Icons.Outlined.Share, null)
                }
            }/* FilledTonalButton(
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
             }*/
        }
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun HeadlineDetailComponent(
    string: String, imageVector: ImageVector, fontSize: TextUnit = 16.sp, iconSize: Dp = 24.dp,
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
        AnimatedContent(targetState = string, transitionSpec = {
            if (targetState > initialState) {
                slideInVertically(animationSpec = tween(300)) { height -> height } + fadeIn() togetherWith slideOutVertically(
                    animationSpec = tween(300)
                ) { height -> -height } + fadeOut()
            } else {
                slideInVertically(animationSpec = tween(300)) { height -> -height } + fadeIn() togetherWith slideOutVertically(
                    animationSpec = tween(300)
                ) { height -> height } + fadeOut()
            }.using(
                SizeTransform(clip = false)
            )
        }) {
            Icon(imageVector, contentDescription = null, modifier = Modifier.size(iconSize))
        }
        Spacer(Modifier.width(5.dp))
        AnimatedContent(targetState = string) {
            Text(
                text = it, style = MaterialTheme.typography.titleSmall, fontSize = fontSize
            )
        }
        Spacer(Modifier.width(5.dp))
    }
}