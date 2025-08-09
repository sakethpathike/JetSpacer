package com.sakethh.jetspacer.ui.screens.headlines.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.pillStar
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.domain.model.article.Article

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TopHeadlineComponent(
    article: Article, onImgClick: () -> Unit, onItemClick: () -> Unit
) {
    val context = LocalContext.current
    val localClipboardManager = LocalClipboardManager.current
    val localUriHandler = LocalUriHandler.current
    val colorScheme = MaterialTheme.colorScheme
    val iconModifier: (onClick: () -> Unit) -> Modifier = {
        Modifier
            .padding(start = 5.dp, end = 5.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable {
                it()
            }
            .background(colorScheme.primary.copy(0.1f))
            .padding(10.dp)
            .size(20.dp)
    }
    Column(modifier = Modifier
        .clickable {
            onItemClick()
        }
        .padding(start = 15.dp, end = 15.dp, top = 15.dp)
        .fillMaxWidth()
        .wrapContentHeight()) {
        if (article.urlToImage.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(context).data(article.urlToImage).crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(
                        RoundedCornerShape(15.dp)
                    )
                    .border(
                        1.5.dp, LocalContentColor.current.copy(0.25f), RoundedCornerShape(15.dp)
                    ),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.onPrimary,
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        Text(
            modifier = Modifier
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
            modifier = Modifier.padding(top = 8.dp),
            maxLines = 4,
            lineHeight = 22.sp,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = article.description,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp,
            maxLines = 2
        )
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = iconModifier {
                            localUriHandler.openUri(article.url)
                        }, imageVector = Icons.Outlined.OpenInBrowser, contentDescription = null
                    )
                    Icon(
                        modifier = iconModifier {
                            localClipboardManager.setText(AnnotatedString(article.url))
                        }, imageVector = Icons.Outlined.ContentCopy, contentDescription = null
                    )
                    Icon(modifier = iconModifier {
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, article.url)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(intent, null)
                        context.startActivity(shareIntent)
                    }, imageVector = Icons.Outlined.Share, contentDescription = null)
                }
            }
        }
    }
    HorizontalDivider(modifier = Modifier.padding(top = 12.dp, start = 7.5.dp, end = 7.5.dp),thickness = 1.dp, color = MaterialTheme.colorScheme.primary.copy(0.1f))
}