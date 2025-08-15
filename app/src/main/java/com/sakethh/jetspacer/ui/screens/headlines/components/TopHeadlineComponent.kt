package com.sakethh.jetspacer.ui.screens.headlines.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.domain.model.article.Article
import com.sakethh.jetspacer.ui.components.pulsateEffect
import com.sakethh.jetspacer.ui.utils.iconModifier

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TopHeadlineComponent(
    animatedVisibilityScope: AnimatedVisibilityScope,
    article: Article,
    colors: List<androidx.compose.ui.graphics.Color>,
    onItemClick: () -> Unit
) {
    val context = LocalContext.current
    val localClipboardManager = LocalClipboardManager.current
    val localUriHandler = LocalUriHandler.current
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .then(
                if (colors.isNotEmpty()) Modifier.background(
                    alpha = 0.115f,
                    brush = Brush.radialGradient(
                        colors = colors
                    ),
                ) else Modifier
            )
            .pulsateEffect()
            .clickable(onClick = {
                onItemClick()
            }, interactionSource = remember {
                MutableInteractionSource()
            }, indication = null)
            .padding(top = 7.5.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 7.5.dp, start = 15.dp, end = 15.dp)
    ) {
        if (article.urlToImage.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(context).data(article.urlToImage).crossfade(true)
                    .build(), contentDescription = null, modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = "HEADLINE_IMG_${article.url}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(
                        RoundedCornerShape(15.dp)
                    )
                    .border(
                        1.5.dp, LocalContentColor.current.copy(0.25f), RoundedCornerShape(15.dp)
                    ), contentScale = ContentScale.Crop
            )
        }
        Text(
            modifier = Modifier
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = "HEADLINE_SOURCE_${article.url}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .padding(top = 10.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(0.1f),
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(5.dp),
            text = article.source.name,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp
        )
        Text(
            text = article.title,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
            modifier = Modifier
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = "HEADLINE_TITLE_${article.url}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .padding(top = 8.dp),
            maxLines = 4,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier
                .sharedElement(
                    sharedContentState = rememberSharedContentState(key = "HEADLINE_DESC_${article.url}"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
                .padding(top = 5.dp),
            text = article.description,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp,
            maxLines = 2
        )
        Row(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .fillMaxWidth()
                .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(key = "HEADLINE_OIB_${article.url}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .iconModifier(colorScheme) {
                                localUriHandler.openUri(article.url)
                            }
                            .pulsateEffect(0.75f),
                        imageVector = Icons.Outlined.OpenInBrowser,
                        contentDescription = null)
                    Icon(
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(key = "HEADLINE_CL_${article.url}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .iconModifier(colorScheme) {
                                localClipboardManager.setText(AnnotatedString(article.url))
                            }
                            .pulsateEffect(0.75f),
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = null)
                    Icon(
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(key = "HEADLINE_SHARE_${article.url}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .iconModifier(colorScheme) {
                                val intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, article.url)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(intent, null)
                                context.startActivity(shareIntent)
                            }
                            .pulsateEffect(0.75f),
                        imageVector = Icons.Outlined.Share,
                        contentDescription = null)
                }
            }
        }
    }
}