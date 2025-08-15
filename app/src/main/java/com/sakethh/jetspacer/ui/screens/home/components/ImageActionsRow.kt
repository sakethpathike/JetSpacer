package com.sakethh.jetspacer.ui.screens.home.components

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Hd
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Sd
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.sakethh.jetspacer.ui.components.pulsateEffect
import com.sakethh.jetspacer.ui.utils.downloadImage
import com.sakethh.jetspacer.ui.utils.iconModifier

@Composable
fun ImageActionsRow(
    openInBrowserURL: String,
    sdDownloadDesc: String,
    hdDownloadDesc: String?,
    supportsBothHDDAndSD: Boolean,
    hdURL: String?,
    sdURL: String,
    paddingValues: PaddingValues=PaddingValues(start = 10.dp, top = 10.dp, end = 10.dp)
) {
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    val localClipboardManager = LocalClipboardManager.current
    val localUriHandler = LocalUriHandler.current
    var downloadRowExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .animateContentSize()
            .horizontalScroll(rememberScrollState())
            .padding(paddingValues)
    ) {
        // a loop would've been better than this copy-paste of icon btn composables
        Icon(
            modifier = Modifier
                .pulsateEffect(0.85f)
                .iconModifier(colorScheme) {
                    localClipboardManager.setText(
                        AnnotatedString(
                            sdURL
                        )
                    )
                }, imageVector = Icons.Outlined.ContentCopy, contentDescription = null
        )
        Icon(
            modifier = Modifier
                .pulsateEffect(0.85f)
                .iconModifier(colorScheme) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT, sdURL
                        )
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent, null)
                    context.startActivity(shareIntent)
                }, imageVector = Icons.Outlined.Share, contentDescription = null
        )
        Row(
            modifier = (if (downloadRowExpanded) {
                Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(colorScheme.primary.copy(0.1f))
                    .padding(10.dp)
            } else Modifier).animateContentSize()
        ) {
            Icon(
                modifier = Modifier
                    .pulsateEffect(0.85f)
                    .iconModifier(colorScheme) {
                        if (supportsBothHDDAndSD) {
                            downloadRowExpanded = !downloadRowExpanded
                        } else {
                            downloadImage(
                                context = context,
                                imgURL = sdURL,
                                fileName = sdURL.substringAfterLast(
                                    "/"
                                ),
                                description = sdDownloadDesc
                            )
                        }
                    }, imageVector = Icons.Outlined.FileDownload, contentDescription = null
            )
            AnimatedVisibility(visible = downloadRowExpanded && supportsBothHDDAndSD) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .width(1.dp)
                            .height(20.dp)
                            .background(LocalContentColor.current.copy(alpha = 0.5f))
                    )
                    Icon(
                        modifier = Modifier
                            .pulsateEffect(0.85f)
                            .iconModifier(colorScheme) {
                                downloadImage(
                                    context = context,
                                    imgURL = hdURL!!,
                                    fileName = hdURL.substringAfterLast(
                                        "/"
                                    ),
                                    description = hdDownloadDesc!!
                                )
                                downloadRowExpanded = false
                            }, imageVector = Icons.Outlined.Hd, contentDescription = null
                    )
                    Icon(
                        modifier = Modifier
                            .pulsateEffect(0.85f)
                            .iconModifier(colorScheme) {
                                downloadImage(
                                    context = context,
                                    imgURL = sdURL,
                                    fileName = sdURL.substringAfterLast(
                                        "/"
                                    ),
                                    description = sdDownloadDesc
                                )
                                downloadRowExpanded = false
                            }, imageVector = Icons.Outlined.Sd, contentDescription = null
                    )
                }
            }
        }
        Icon(
            modifier = Modifier
                .pulsateEffect(0.85f)
                .iconModifier(colorScheme) {
                    localUriHandler.openUri(openInBrowserURL)
                }, imageVector = Icons.Outlined.OpenInBrowser, contentDescription = null
        )
    }
}