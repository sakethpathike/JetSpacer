package com.sakethh.jetspacer.headlines.presentation.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.sakethh.jetspacer.headlines.domain.model.Article

@Composable
fun TopHeadlineComponent(
    article: Article,
    onImgClick: () -> Unit,
    onItemClick: () -> Unit,
    bookMarkIcon: ImageVector,
    onBookMarkClick: () -> Unit
) {
    val context = LocalContext.current
    val localClipboardManager = LocalClipboardManager.current
    val localUriHandler = LocalUriHandler.current
    Column(
        modifier = Modifier
            .clickable {
                onItemClick()
            }
            .padding(start = 15.dp, top = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 15.dp)
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleSmall,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .padding(end = 15.dp),
                maxLines = 4,
                lineHeight = 20.sp,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis
            )
            if (article.urlToImage.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(article.urlToImage)
                        .crossfade(true).build(),
                    contentDescription = null,
                    modifier = Modifier
                        .width(95.dp)
                        .height(60.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .border(
                            1.5.dp,
                            LocalContentColor.current.copy(0.25f),
                            RoundedCornerShape(15.dp)
                        )
                        .clickable {
                            onImgClick()
                        },
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(95.dp)
                        .height(60.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimary,
                        imageVector = Icons.Rounded.ImageNotSupported,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                    )
                }
            }
        }
        Text(
            modifier = Modifier
                .padding(top = 15.dp, end = 15.dp),
            text = article.description,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp,
            maxLines = 2
        )
        Text(
            modifier = Modifier
                .padding(
                    top = 15.dp,
                    end = 15.dp
                )
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        localUriHandler.openUri(article.url)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.OpenInBrowser,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = {
                        localClipboardManager.setText(AnnotatedString(article.url))
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = {
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, article.url)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(intent, null)
                        context.startActivity(shareIntent)
                    }) {
                        Icon(imageVector = Icons.Outlined.Share, contentDescription = null)
                    }
                    IconButton(onClick = {
                        onBookMarkClick()
                    }) {
                        Icon(
                            imageVector = bookMarkIcon,
                            contentDescription = null
                        )
                    }
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outline.copy(0.25f)
        )
    }
}