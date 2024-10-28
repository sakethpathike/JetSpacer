package com.sakethh.jetspacer.explore.marsGallery.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPagerApi::class, ExperimentalLayoutApi::class
)
@Composable
fun MarsGalleryScreen(navController: NavController) {
    val pagerState = rememberPagerState(0)
    val isSolTextFieldReadOnly = rememberSaveable {
        mutableStateOf(true)
    }
    val focusRequest = remember {
        FocusRequester()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Column {
                    Text("Mars Gallery", style = MaterialTheme.typography.titleSmall)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Curiosity",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.width(2.dp))
                        Icon(Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(100.dp)
                                )
                                .clickable {

                                })
                    }
                }
            }, navigationIcon = {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            })
        }) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(BottomAppBarDefaults.containerColor)
                    .animateContentSize()
                    .padding(bottom = if (WindowInsets.isImeVisible) it.calculateBottomPadding() * 4 else 0.dp)
            ) {
                TextField(keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Go
                ), keyboardActions = KeyboardActions(onGo = {
                    isSolTextFieldReadOnly.value = isSolTextFieldReadOnly.value.not()
                    focusRequest.freeFocus()
                }), readOnly = isSolTextFieldReadOnly.value, value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, top = 15.dp, end = 15.dp, bottom = 5.dp)
                        .focusRequester(focusRequest),
                    leadingIcon = {
                        Icon(Icons.Default.Search, null)
                    },
                    supportingText = {
                        Text(text = buildAnnotatedString {
                            append("value of ")
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                withLink(LinkAnnotation.Url("https://an.rsl.wustl.edu/help/Content/Using%20the%20Notebook/Concepts%20and%20deep%20dive/Time%20on%20Mars.htm#:~:text=a%20simple%20measure%20of%20time%20on%20mars%20is%20the%20sol%2C%20that%20is%2C%20a%20solar%20day.")) {
                                    append("Sol")
                                }
                            }
                            append(" should be >=0 and <=45")
                        }, style = MaterialTheme.typography.titleSmall)
                    })

                FilledTonalButton(
                    onClick = {
                        isSolTextFieldReadOnly.value = isSolTextFieldReadOnly.value.not()
                        if (isSolTextFieldReadOnly.value) focusRequest.freeFocus() else focusRequest.requestFocus()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
                ) {
                    Text(
                        if (isSolTextFieldReadOnly.value.not()) "Apply" else "Edit",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
            Column(
                Modifier.padding(it)
            ) {
                ScrollableTabRow(selectedTabIndex = pagerState.currentPage) {
                    (1..5).forEach {
                        Tab(
                            selected = listOf(true, false).random(),
                            onClick = {},
                            modifier = Modifier.padding(15.dp)
                        ) {
                            Text(text = it.toString(), style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
                HorizontalPager(count = 5, state = pagerState) {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Adaptive(150.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            Spacer(Modifier.height(500.dp))
                        }
                    }
                }
            }
        }
    }
}