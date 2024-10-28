package com.sakethh.jetspacer.explore.apodArchive.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sakethh.jetspacer.common.presentation.utils.customRememberSavable
import com.sakethh.jetspacer.common.utils.jetSpacerLog
import com.sakethh.jetspacer.home.presentation.state.apod.ModifiedAPODDTO
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun APODArchiveScreen(navController: NavController) {
    val apodArchiveScreenViewModel: APODArchiveScreenViewModel = viewModel()
    val apodArchiveState = apodArchiveScreenViewModel.apodArchiveState.value
    val context = LocalContext.current
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val lazyVerticalStaggeredGridState = rememberLazyStaggeredGridState()
    val isBtmSheetVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val btmSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isDateRangePickerDialogVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val selectedAPODData = customRememberSavable {
        mutableStateOf(
            ModifiedAPODDTO(
                copyright = "",
                date = "",
                explanation = "",
                hdUrl = "",
                mediaType = "",
                title = "",
                url = ""
            )
        )
    }
    val coroutineScope = rememberCoroutineScope()
    val isDataBasedOnCustomRangeSelector = rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            MediumTopAppBar(scrollBehavior = topAppBarScrollBehavior, title = {
                Text("APOD Archive", style = MaterialTheme.typography.titleSmall, fontSize = 16.sp)
            }, navigationIcon = {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            }, actions = {
                IconButton(onClick = {
                    isDateRangePickerDialogVisible.value = true
                }) {
                    Icon(Icons.Default.DateRange, null)
                }
            })
        }) {
        LazyVerticalStaggeredGrid(
            state = lazyVerticalStaggeredGridState,
            columns = StaggeredGridCells.Adaptive(150.dp),
            modifier = Modifier
                .padding(it)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
        ) {
            if (apodArchiveState.isLoading.not()) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Spacer(Modifier.height(10.dp))
                }
            }
            items(apodArchiveState.data) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(it.url)
                        .crossfade(true).build(),
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(5.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .border(
                            1.5.dp, LocalContentColor.current.copy(0.25f), RoundedCornerShape(15.dp)
                        )
                        .combinedClickable(onClick = {
                            selectedAPODData.value = it
                            isBtmSheetVisible.value = true
                            coroutineScope.launch {
                                btmSheetState.show()
                            }
                        }, onLongClick = {

                        }), contentDescription = null
                )
            }
            if (apodArchiveState.isLoading) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
            if (isDataBasedOnCustomRangeSelector.value && apodArchiveState.isLoading.not()) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Column {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        )
                        Text(
                            "That's all the data found based on the filter you applied.",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
                        )
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp, end = 15.dp, bottom = 15.dp), onClick = {
                                isDataBasedOnCustomRangeSelector.value = false
                                apodArchiveScreenViewModel.resetAPODArchiveStateAndReloadAgain()
                            }) {
                            Text("Clear the filter", style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
            }
        }
    }
    APODBtmSheet(
        modifiedAPODDTO = selectedAPODData.value,
        visible = isBtmSheetVisible,
        btmSheetState = btmSheetState
    )
    if (isDateRangePickerDialogVisible.value) {

        val dateRangePickerState =
            rememberDateRangePickerState(selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    if (apodArchiveScreenViewModel.apodStartDate.isBlank()) {
                        return false
                    }

                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                    val lastSelectableDate =
                        dateFormat.parse(apodArchiveScreenViewModel.apodStartDate)
                    return utcTimeMillis < lastSelectableDate.time
                }

                override fun isSelectableYear(year: Int): Boolean {
                    return year <= Calendar.getInstance().get(Calendar.YEAR)
                }
            })

        Box(
            Modifier
                .fillMaxSize()
                .background(DatePickerDefaults.colors().containerColor)
        ) {
            Column {
                Spacer(Modifier.height(50.dp))
                DateRangePicker(state = dateRangePickerState, title = {
                    Text(
                        "Select the date range",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 15.dp, top = 15.dp)
                    )
                }, headline = {
                    DateRangePickerDefaults.DateRangePickerHeadline(
                        selectedStartDateMillis = dateRangePickerState.selectedStartDateMillis,
                        selectedEndDateMillis = dateRangePickerState.selectedEndDateMillis,
                        displayMode = dateRangePickerState.displayMode,
                        modifier = Modifier.padding(start = 15.dp),
                        dateFormatter = object : DatePickerFormatter {
                            override fun formatDate(
                                dateMillis: Long?,
                                locale: CalendarLocale,
                                forContentDescription: Boolean
                            ): String? {
                                val dateFormat = if (forContentDescription) {
                                    SimpleDateFormat("EEEE, MMMM d, yyyy", locale)
                                } else {
                                    SimpleDateFormat("MMM d, yyyy", locale)
                                }

                                return dateMillis?.let { Date(it) }?.let { dateFormat.format(it) }
                            }

                            override fun formatMonthYear(
                                monthMillis: Long?,
                                locale: CalendarLocale
                            ): String? {
                                if (monthMillis == null) return null
                                val monthYearFormat = SimpleDateFormat("MMMM yyyy", locale)
                                return monthYearFormat.format(Date(monthMillis))
                            }
                        }
                    )
                })
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DatePickerDefaults.colors().containerColor)
                    .align(Alignment.BottomCenter)
            ) {
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp), onClick = {
                    isDateRangePickerDialogVisible.value = false
                    isDataBasedOnCustomRangeSelector.value = true
                    val endDate = Date(
                        dateRangePickerState.selectedStartDateMillis ?: 0
                    )
                    apodArchiveScreenViewModel.retrieveAPODDataBetweenSpecificDates(
                        apodStartDate = SimpleDateFormat("yyyy-MM-dd").format(
                            dateRangePickerState.selectedEndDateMillis ?: 0
                        ),
                        apodEndDate = SimpleDateFormat("yyyy-MM-dd").format(endDate),
                        erasePreviousData = true
                    )
                }) {
                    Text("Apply", style = MaterialTheme.typography.titleSmall)
                }
            }
        }
    }
    LaunchedEffect(lazyVerticalStaggeredGridState.canScrollForward) {
        if (isDataBasedOnCustomRangeSelector.value.not() && lazyVerticalStaggeredGridState.canScrollForward.not() && apodArchiveState.data.isNotEmpty() && !apodArchiveState.isLoading) {
            jetSpacerLog("triggering from launched effect")
            apodArchiveScreenViewModel.retrieveNextBatchOfAPODArchive()
        }
    }
}