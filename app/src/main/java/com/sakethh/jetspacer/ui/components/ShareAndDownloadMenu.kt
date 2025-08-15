package com.sakethh.jetspacer.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Hd
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Sd
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sakethh.jetspacer.R

@Composable
fun ShareAndDownloadMenu(
    isBtmColumnExpanded: MutableState<Boolean>,
    onOpenInBrowserClick: () -> Unit,
    onContentCopyClick: () -> Unit,
    onShareClick: () -> Unit,
    onBookMarkClick: () -> Unit,
    bookMarkIcon: ImageVector
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceColorAtElevation(
                    BottomAppBarDefaults.ContainerElevation
                )
            )
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 7.5.dp, bottom = 7.5.dp)
        ) {
            FilledTonalButton(onClick = {
                onContentCopyClick()
            }) {
                Icon(Icons.Outlined.ContentCopy, null)
            }
            FilledTonalButton(onClick = {
                onShareClick()
            }) {
                Icon(Icons.Outlined.Share, null)
            }
            FilledTonalButton(onClick = {
                onOpenInBrowserClick()
            }) {
                Icon(Icons.Outlined.OpenInBrowser, null)
            }
        }
        FilledTonalButton(
            onClick = {
                onBookMarkClick()

            }, modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = bookMarkIcon, null)
            Spacer(Modifier.width(5.dp))
            Text("Bookmark", style = MaterialTheme.typography.titleSmall)
        }
        if (!isBtmColumnExpanded.value) return@Column
        FilledTonalButton(
            onClick = {

            }, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
        ) {
            Icon(Icons.Outlined.Sd, null)
            Spacer(Modifier.width(5.dp))
            Text("Download in SD", style = MaterialTheme.typography.titleSmall)
        }
        FilledTonalButton(
            onClick = {

            }, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
        ) {
            Icon(Icons.Outlined.Hd, null)
            Spacer(Modifier.width(5.dp))
            Text("Download in HD", style = MaterialTheme.typography.titleSmall)
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)
        )
        FilledTonalButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
                .navigationBarsPadding()
        ) {
            Icon(
                painterResource(if (isSystemInDarkTheme()) R.drawable.instagram_white else R.drawable.instagram_black),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(5.dp))
            Text(
                "Share via Instagram Stories", style = MaterialTheme.typography.titleSmall
            )
        }
    }
    return
    Column(
        modifier = Modifier.animateContentSize()
    ) {
        FilledTonalIconButton(modifier = Modifier
            .padding(end = 15.dp)
            .size(50.dp), onClick = {
            isBtmColumnExpanded.value = !isBtmColumnExpanded.value
        }) {
            Icon(
                if (isBtmColumnExpanded.value) Icons.Default.ExpandMore else Icons.Default.ExpandLess,
                null
            )
        }
        Spacer(Modifier.height(if (isBtmColumnExpanded.value.not()) 90.dp else 245.dp))
    }
}