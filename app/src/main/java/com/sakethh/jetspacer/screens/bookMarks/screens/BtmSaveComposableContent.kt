package com.sakethh.jetspacer.screens.bookMarks.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sakethh.jetspacer.Coil_Image
import com.sakethh.jetspacer.R
import com.sakethh.jetspacer.currentDestination
import com.sakethh.jetspacer.localDB.*
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BtmSaveComposableContent(
    coroutineScope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    data: CustomBookMarkData
) {
    val bookMarksVM: BookMarksVM = viewModel()
    val btmScreenData = bookMarksVM.localDB.getCustomBookMarkTopicData()
        .collectAsState(initial = emptyList()).value
    val isNewCollectionDialogEnabled = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var doesElementExistsInDB = false
    val imgURL: String =
        when (data.dataType) {
            SavedDataType.APOD -> {
                data.data as APOD_DB_DTO
                data.data.imageURL
            }

            SavedDataType.ROVERS -> {
                data.data as MarsRoversDBDTO
                data.data.imageURL
            }

            SavedDataType.NEWS -> {
                data.data as NewsDB
                data.data.imageURL
            }

            else -> {
                ""
            }
        }
    val nonAllowedData = setOf("APOD", "Rovers", "News")
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Row(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Save to",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 12.dp),
                    overflow = TextOverflow.Ellipsis
                )
                Icon(imageVector = Icons.Default.Add,
                    modifier = Modifier
                        .clickable {
                            isNewCollectionDialogEnabled.value = true
                        }
                        .padding(10.dp),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimary)
            }
            Divider(
                color = MaterialTheme.colorScheme.onPrimary,
                thickness = 0.dp
            )
            if (btmScreenData.any { it.name !in nonAllowedData }) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(btmScreenData) { index, element ->
                    if (element.name != "APOD" && element.name != "News" && element.name != "Rovers") {
                        Row(
                            modifier = Modifier
                                .padding(
                                    top = 15.dp,
                                    start = 15.dp,
                                    end = 15.dp,
                                    bottom = 15.dp
                                )
                                .fillMaxWidth()
                                .clip(
                                    RoundedCornerShape(5.dp)
                                )
                                .clickable {
                                    BtmSaveComposableContent.indexedValue = index
                                    coroutineScope
                                        .launch {
                                            doesElementExistsInDB = bookMarksVM.localDB
                                                .getCustomBookMarkTopicData()
                                                .map { list ->
                                                    list[BtmSaveComposableContent.indexedValue].data.any { element ->
                                                        when (data.dataType) {
                                                            SavedDataType.APOD -> {
                                                                element.data as APOD_DB_DTO
                                                                data.data as APOD_DB_DTO
                                                                element.data.imageURL == data.data.imageURL
                                                            }

                                                            SavedDataType.ROVERS -> {
                                                                element.data as MarsRoversDBDTO
                                                                data.data as MarsRoversDBDTO
                                                                element.data.imageURL == data.data.imageURL
                                                            }

                                                            SavedDataType.NEWS -> {
                                                                element.data as NewsDB
                                                                data.data as NewsDB
                                                                element.data.imageURL == data.data.imageURL
                                                            }

                                                            else -> {
                                                                false
                                                            }
                                                        }
                                                    }
                                                }
                                                .equals(true)
                                        }
                                        .invokeOnCompletion {
                                            if (doesElementExistsInDB) {
                                                Toast
                                                    .makeText(
                                                        context,
                                                        "This already exists in the \"${btmScreenData[BtmSaveComposableContent.indexedValue].name}\" collection:)",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                            } else {
                                                coroutineScope
                                                    .launch {
                                                        bookMarksVM.localDB.addDataInAnExistingBookmarkTopic(
                                                            tableName = element.name,
                                                            newData = data
                                                        )
                                                    }
                                                    .invokeOnCompletion {
                                                        Toast
                                                            .makeText(
                                                                context,
                                                                "Added in the \"${btmScreenData[BtmSaveComposableContent.indexedValue].name}\" collection:)",
                                                                Toast.LENGTH_SHORT
                                                            )
                                                            .show()
                                                    }
                                            }
                                        }
                                }
                                .border(
                                    BorderStroke(0.dp, MaterialTheme.colorScheme.onSurface),
                                    RoundedCornerShape(5.dp)
                                )
                                .background(MaterialTheme.colorScheme.primary)) {

                            Coil_Image().CoilImage(
                                imgURL = element.imgUrlForGrid,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(75.dp)
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(5.dp)),
                                onError = painterResource(id = R.drawable.baseline_image_24),
                                contentScale = ContentScale.Crop
                            )

                            Box(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    text = element.name,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 18.sp,
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(end = 20.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
                item {
                    if (currentDestination == NavigationRoutes.NEWS_SCREEN || currentDestination == NavigationRoutes.HOME_SCREEN || currentDestination == NavigationRoutes.SPACE_SCREEN || currentDestination == NavigationRoutes.BOOKMARKS_SCREEN) {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp)
            ) {
                Text(
                    text = "Nothing here, create a new collection to get started",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.Center),
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Start
                )
            }
        }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                TextButton(onClick = {
                    coroutineScope.launch {
                        modalBottomSheetState.hide()
                    }
                }, modifier = Modifier.align(Alignment.Center)) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(15.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            val collectionName = remember { mutableStateOf("") }
            val imageURLForGrid: String =
                when (data.dataType) {
                    SavedDataType.APOD -> {
                        data.data as APOD_DB_DTO
                        data.data.imageURL
                    }

                    SavedDataType.ROVERS -> {
                        data.data as MarsRoversDBDTO
                        data.data.imageURL
                    }

                    SavedDataType.NEWS -> {
                        data.data as NewsDB
                        data.data.imageURL
                    }

                    else -> {
                        ""
                    }
                }
            var doesNameExistsInDb = false
            if (isNewCollectionDialogEnabled.value) {
                AlertDialog(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(5.dp)
                        ),
                    onDismissRequest = { isNewCollectionDialogEnabled.value = false },
                    shape = RoundedCornerShape(5.dp),
                    confirmButton = {
                        Button(
                            onClick = {
                                isNewCollectionDialogEnabled.value = false
                                coroutineScope.launch {
                                    bookMarksVM.localDB.getCustomBookMarkTopicData().map { list ->
                                        doesNameExistsInDb = list.any { element ->
                                            element.name == collectionName.value
                                        }
                                    }
                                }.invokeOnCompletion {
                                    if (!doesNameExistsInDb) {
                                        coroutineScope.launch {
                                            bookMarksVM.localDB.addCustomBookMarkTopic(
                                                BookMarkScreenGridNames(
                                                    name = collectionName.value,
                                                    imgUrlForGrid = imageURLForGrid,
                                                    data = listOf(data)
                                                )
                                            )
                                        }.invokeOnCompletion {
                                            Toast.makeText(
                                                context,
                                                "Created new collection without any failure:)",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Collection with the name \"${collectionName.value}\" already exists, use a different name",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary)
                        ) {
                            Text(
                                text = "Create NOW!",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = {
                                isNewCollectionDialogEnabled.value = false
                            },
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text(
                                text = "Um-mm, Never mind",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }, title = {
                        Text(
                            text = "New collection:",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp
                        )
                    }, text = {
                        TextField(value = collectionName.value, onValueChange = {
                            collectionName.value = it
                        }, singleLine = true, supportingText = {
                            Text(
                                text = "The new collection name that you are going to create now, should be unique.",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }, modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                        )
                    }, containerColor = MaterialTheme.colorScheme.surface
                )
            }
        }

    }
}

object BtmSaveComposableContent {
    var indexedValue = 0
}