package com.sakethh.jetspacer.ui.explore.marsGallery

import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.domain.model.rover.RoverImage
import com.sakethh.jetspacer.domain.model.rover_latest_images.LatestPhoto
import com.sakethh.jetspacer.ui.components.LabelValueCard
import com.sakethh.jetspacer.ui.components.ShareAndDownloadMenu
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoverImageDetailsBtmSheet(
    image: LatestPhoto, visible: MutableState<Boolean>, btmSheetState: SheetState
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val localUriHandler = LocalUriHandler.current
    val localClipboardManager = LocalClipboardManager.current
    val roverImageDetailsBtmSheetViewModel: RoverImageDetailsBtmSheetViewModel = viewModel()
    if (visible.value) {
        LaunchedEffect(Unit) {
            roverImageDetailsBtmSheetViewModel.doesThisImageExists(image.imgSrc)
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
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp)
                        .verticalScroll(rememberScrollState())
                        .animateContentSize()
                ) {
                    Text(
                        image.rover.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(0.25f))
                            .padding(5.dp)
                    )
                    Spacer(Modifier.height(15.dp))
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(image.imgSrc).crossfade(true)
                            .build(),
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
                    Row {
                        LabelValueCard(
                            title = "Sol",
                            value = image.sol.toString(),
                            outerPaddingValues = PaddingValues(top = 15.dp, end = 5.dp)
                        )
                        LabelValueCard(
                            title = "Earth Date",
                            value = image.earthDate,
                            outerPaddingValues = PaddingValues(top = 15.dp)
                        )
                    }
                    LabelValueCard(
                        title = "Captured by",
                        value = image.camera.fullName,
                        outerPaddingValues = PaddingValues(top = 15.dp, end = 15.dp)
                    )
                    Spacer(Modifier.padding(if (isBtmColumnExpanded.value) 150.dp else 75.dp))
                }
                ShareAndDownloadMenu(
                    bookMarkIcon =
                    if (roverImageDetailsBtmSheetViewModel.doesImageExistsInLocalDB.value) Icons.Filled.BookmarkRemove else Icons.Outlined.BookmarkAdd,
                    isBtmColumnExpanded = isBtmColumnExpanded,
                    onOpenInBrowserClick = {
                        localUriHandler.openUri(image.imgSrc)
                    },
                    onContentCopyClick = {
                        localClipboardManager.setText(AnnotatedString(image.imgSrc))
                    },
                    onShareClick = {
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, image.imgSrc)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(intent, null)
                        context.startActivity(shareIntent)
                    },
                    onBookMarkClick = {
                        if (roverImageDetailsBtmSheetViewModel.doesImageExistsInLocalDB.value) {
                            roverImageDetailsBtmSheetViewModel.deleteAnExistingImageFromLocalDB(
                                image.imgSrc
                            )
                        } else {
                            roverImageDetailsBtmSheetViewModel.addANewImageInLocalDB(
                                RoverImage(
                                    cameraFullName = image.camera.fullName,
                                    cameraAbbreviation = image.camera.name,
                                    earthDate = image.earthDate,
                                    imgUrl = image.imgSrc,
                                    roverId = image.rover.id.toLong(),
                                    sol = image.sol,
                                    isBookmarked = true,
                                    roverName = image.rover.name
                                )
                            )
                        }
                    })
            }
        }
    }
}