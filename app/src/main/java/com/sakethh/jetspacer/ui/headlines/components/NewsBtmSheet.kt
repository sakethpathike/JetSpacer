package com.sakethh.jetspacer.ui.headlines.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsBtmSheet(isVisible: MutableState<Boolean>, bottomModalBottomSheetState: SheetState) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val isImageAssociatedWithTheLinkIsExpanded = rememberSaveable {
        mutableStateOf(false)
    }
    if (isVisible.value) {
        ModalBottomSheet(
            dragHandle = {},
            sheetState = bottomModalBottomSheetState,
            onDismissRequest = {
                coroutineScope.launch {
                    bottomModalBottomSheetState.hide()
                }.invokeOnCompletion {
                    isVisible.value = false
                }
            }) {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp)
                        .wrapContentHeight()
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data("https://pbs.twimg.com/media/Gafzi7bW4AA0f3T?format=jpg&name=small")
                            .crossfade(true).build(),
                        contentDescription = null,
                        modifier = Modifier
                            .animateContentSize()
                            .fillMaxWidth()
                            .then(
                                if (isImageAssociatedWithTheLinkIsExpanded.value) Modifier.wrapContentHeight() else Modifier.heightIn(
                                    max = 150.dp
                                )
                            ),
                        contentScale = ContentScale.Crop
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .padding(end = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledTonalIconButton(
                            onClick = {
                                isImageAssociatedWithTheLinkIsExpanded.value =
                                    !isImageAssociatedWithTheLinkIsExpanded.value
                            }, modifier = Modifier
                                .alpha(0.75f)
                                .padding(5.dp)
                        ) {
                            Icon(
                                imageVector = if (!isImageAssociatedWithTheLinkIsExpanded.value) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = ""
                            )
                        }
                        Text(
                            text = "linkTitle",
                            style = MaterialTheme.typography.titleSmall,
                            fontSize = 16.sp,
                            maxLines = 2,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IndividualMenuComponent(
    onOptionClick: () -> Unit,
    elementName: String,
    elementImageVector: ImageVector
) {
    Row(
        modifier = Modifier
            .combinedClickable(
                interactionSource = remember {
                    MutableInteractionSource()
                }, indication = null,
                onClick = {
                    onOptionClick()
                },
                onLongClick = {

                })
            .padding(end = 10.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                modifier = Modifier.padding(10.dp), onClick = { onOptionClick() },
                colors = IconButtonDefaults.filledTonalIconButtonColors()
            ) {
                Icon(imageVector = elementImageVector, contentDescription = null)
            }
            Text(
                text = elementName,
                style = MaterialTheme.typography.titleSmall,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}