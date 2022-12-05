package com.sakethh.jetspacer.screens.space.rovers

import android.annotation.SuppressLint
import android.util.LayoutDirection
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DrawerValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.screens.bookMarks.BookMarksScreen
import com.sakethh.jetspacer.navigation.NavigationRoutes
import com.sakethh.jetspacer.screens.space.apod.APODScreen
import com.sakethh.jetspacer.ui.theme.AppTheme
import com.sakethh.jetspacer.R
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoversScreen(navController: NavController) {
    val navigationDrawerState =
        androidx.compose.material3.rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    BackHandler {
        if (navigationDrawerState.isOpen) {
            coroutineScope.launch {
                navigationDrawerState.close()
            }
        } else {
            navController.navigate(NavigationRoutes.SPACE_SCREEN) {
                popUpTo(0)
            }
        }
    }
    val roversScreenVM: RoversScreenVM = viewModel()
    val currentScreenIteration = remember { mutableStateOf(0) }
    val currentScreenName = remember { mutableStateOf("") }
    AppTheme {
        CompositionLocalProvider(LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Rtl) {
            ModalNavigationDrawer(
                gesturesEnabled = true,
                drawerContent = {
                    CompositionLocalProvider(LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Ltr) {
                        Column(
                            Modifier
                                .fillMaxWidth(0.75f)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Select\none of the\nRovers",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                fontSize = 40.sp,
                                modifier = Modifier.padding(start = 15.dp),
                                maxLines = 3,
                                lineHeight = 41.sp,
                                textAlign = TextAlign.Start
                            )
                            Divider(
                                thickness = 0.dp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(
                                    start = 25.dp,
                                    end = 25.dp,
                                    top = 30.dp,
                                    bottom = 50.dp
                                )
                            )
                            roversScreenVM.listForDrawerContent.forEachIndexed { index, roversScreen ->
                                Row(
                                    modifier = Modifier
                                        .height(75.dp)
                                        .fillMaxWidth()
                                        .clickable {
                                            coroutineScope.launch {
                                                navigationDrawerState.close()
                                            }
                                            currentScreenIteration.value = index
                                        }
                                        .wrapContentHeight()
                                ) {
                                    Box(
                                        contentAlignment = Alignment.CenterStart,
                                        modifier = Modifier
                                            .padding(start = 15.dp)
                                            .height(45.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Camera,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(end = 10.dp)
                                        )
                                    }
                                    Box(
                                        contentAlignment = Alignment.CenterStart,
                                        modifier = Modifier
                                            .height(45.dp)
                                    ) {
                                        Text(
                                            text = roversScreen.screenName,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            style = MaterialTheme.typography.headlineLarge,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 26.sp
                                        )
                                    }
                                }
                            }
                            Divider(
                                thickness = 0.dp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(
                                    start = 25.dp,
                                    end = 25.dp,
                                    top = 50.dp,
                                    bottom = 15.dp
                                )
                            )
                        }
                    }
                },
                drawerState = navigationDrawerState
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Ltr) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = currentScreenName.value,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 22.sp,
                                        style = MaterialTheme.typography.headlineLarge
                                    )
                                },
                                actions = {
                                    Icon(
                                        imageVector = Icons.Outlined.Menu,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(end = 15.dp)
                                            .clickable {
                                                coroutineScope.launch {
                                                    navigationDrawerState.open()
                                                }
                                            },
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                colors = TopAppBarDefaults.mediumTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                                )
                            )
                        }) {
                        RoversScreenVM.RoverScreenUtils.paddingValues.value = it
                        roversScreenVM.listForDrawerContent[currentScreenIteration.value].composable()
                        currentScreenName.value =
                            roversScreenVM.listForDrawerContent[currentScreenIteration.value].screenName
                    }
                }
            }
        }
    }
}