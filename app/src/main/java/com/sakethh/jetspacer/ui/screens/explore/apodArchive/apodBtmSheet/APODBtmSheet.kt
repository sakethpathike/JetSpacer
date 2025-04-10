package com.sakethh.jetspacer.ui.screens.explore.apodArchive.apodBtmSheet

import android.content.Intent
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Copyright
import androidx.compose.material.icons.outlined.Hd
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.Sd
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.domain.model.APOD
import com.sakethh.jetspacer.ui.screens.headlines.HeadlineDetailComponent
import com.sakethh.jetspacer.ui.screens.home.state.apod.ModifiedAPODDTO
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun APODBtmSheet(
    modifiedAPODDTO: ModifiedAPODDTO, visible: MutableState<Boolean>, btmSheetState: SheetState
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val localUriHandler = LocalUriHandler.current
    val localClipboardManager = LocalClipboardManager.current
    val apodBtmSheetViewModel: APODBtmSheetViewModel = viewModel()

    if (visible.value) {
        LaunchedEffect(Unit) {
            apodBtmSheetViewModel.doesAPODExistsInLocalDB(modifiedAPODDTO.date)
        }
        ModalBottomSheet(sheetState = btmSheetState, onDismissRequest = {
            coroutineScope.launch {
                btmSheetState.hide()
            }.invokeOnCompletion {
                visible.value = false
            }
        }) {
            val isBtmColumnExpanded = rememberSaveable {
                mutableStateOf(false)
            }
            Box(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp)
                        .verticalScroll(rememberScrollState())
                        .animateContentSize()
                ) {
                    Text(
                        "APOD",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(0.25f))
                            .padding(5.dp)
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        "Astronomy Picture of the Day",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.height(15.dp))
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(modifiedAPODDTO.url)
                            .crossfade(true).build(),
                        modifier = Modifier
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(15.dp))
                            .border(
                                1.5.dp,
                                LocalContentColor.current.copy(0.25f),
                                RoundedCornerShape(15.dp)
                            ),
                        contentDescription = null
                    )
                    if (modifiedAPODDTO.date.isNotBlank()) {
                        Spacer(Modifier.height(15.dp))
                        HeadlineDetailComponent(
                            string = modifiedAPODDTO.date.trim(),
                            imageVector = Icons.Outlined.CalendarToday,
                            fontSize = 14.sp,
                            iconSize = 20.dp
                        )
                    }
                    if (modifiedAPODDTO.copyright.isNotBlank()) {
                        Spacer(Modifier.height(5.dp))
                        HeadlineDetailComponent(
                            string = modifiedAPODDTO.copyright.trim().replace("\n", ""),
                            imageVector = Icons.Outlined.Copyright,
                            fontSize = 14.sp,
                            iconSize = 20.dp
                        )
                    }
                    if (modifiedAPODDTO.title.isNotBlank()) {
                        Spacer(Modifier.height(15.dp))
                        Text(
                            "Title",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            modifiedAPODDTO.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (modifiedAPODDTO.explanation.trim().isNotBlank()) {
                        Spacer(Modifier.height(15.dp))
                        Text(
                            "Explanation",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.height(2.dp))
                        Box {
                            Text(
                                modifiedAPODDTO.explanation.trim(),
                                style = MaterialTheme.typography.titleSmall,
                                fontSize = 16.sp,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(Modifier.padding(if (isBtmColumnExpanded.value) 150.dp else 75.dp))
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
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
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        ) {
                            FilledTonalButton(onClick = {
                                localUriHandler.openUri(modifiedAPODDTO.apodWebPageURL)
                            }) {
                                Icon(Icons.Outlined.OpenInBrowser, null)
                            }
                            FilledTonalButton(onClick = {
                                localClipboardManager.setText(AnnotatedString(modifiedAPODDTO.apodWebPageURL))
                            }) {
                                Icon(Icons.Outlined.ContentCopy, null)
                            }
                            FilledTonalButton(onClick = {
                                val intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, modifiedAPODDTO.apodWebPageURL)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(intent, null)
                                context.startActivity(shareIntent)
                            }) {
                                Icon(Icons.Outlined.Share, null)
                            }
                            FilledTonalButton(onClick = {
                                if (apodBtmSheetViewModel.doesAPODExistsInLocalDB.value) {
                                    apodBtmSheetViewModel.deleteAnAPOD(modifiedAPODDTO.date)
                                } else {
                                    apodBtmSheetViewModel.addANewAPODInLocalDB(
                                        APOD(
                                            copyright = modifiedAPODDTO.copyright,
                                            date = modifiedAPODDTO.date,
                                            explanation = modifiedAPODDTO.explanation,
                                            hdUrl = modifiedAPODDTO.hdUrl,
                                            mediaType = modifiedAPODDTO.mediaType,
                                            title = modifiedAPODDTO.title,
                                            url = modifiedAPODDTO.url,
                                            isCurrentAPOD = false,
                                            isBookmarked = true
                                        )
                                    )
                                }
                            }) {
                                Icon(
                                    if (apodBtmSheetViewModel.doesAPODExistsInLocalDB.value) Icons.Filled.BookmarkRemove else Icons.Outlined.BookmarkAdd,
                                    null
                                )
                            }
                        }
                        if (isBtmColumnExpanded.value.not()) return@Column
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
                                "Share via Instagram Stories",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
                return@Box
                Column(modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .animateContentSize()) {
                    FilledTonalIconButton(
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .size(50.dp),
                        onClick = {
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
        }
    }
}