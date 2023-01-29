package com.sakethh.jetspacer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.createDataStore
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sakethh.jetspacer.navigation.BottomNavigationComposable
import com.sakethh.jetspacer.navigation.MainNavigation
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.settings.readInAppBrowserSetting
import com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest.ManifestForCuriosityVM
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(
        ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class,
        ExperimentalMaterialApi::class
    )
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStore = createDataStore("settingsPreferences")
        lifecycleScope.launchWhenCreated {
            readInAppBrowserSetting(dataStore)
            ManifestForCuriosityVM().maxCuriositySol()
        }
        GlobalScope.launch {
            HomeScreenViewModel.Network.isConnectedToInternet()
        }
        setContent {
            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()
            val isConnectedToInternet =
                HomeScreenViewModel.Network.connectedToInternet.collectAsState()
            val bottomSheetState = rememberBottomSheetScaffoldState()
            if (!isConnectedToInternet.value || !HomeScreenViewModel.Network.isConnectionSucceed.value) {
                coroutineScope.launch {
                    bottomSheetState.bottomSheetState.expand()
                }
            } else {
                coroutineScope.launch {
                    bottomSheetState.bottomSheetState.collapse()
                }
            }
            AppTheme {
                val systemColor= rememberSystemUiController()
                systemColor.setStatusBarColor(MaterialTheme.colorScheme.surface)
                systemColor.setNavigationBarColor(MaterialTheme.colorScheme.primary)
                Scaffold(
                    bottomBar = {
                        BottomNavigationComposable(
                            navController = navController
                        )
                    }
                ) {
                    BottomSheetScaffold(
                        scaffoldState = bottomSheetState,
                        sheetPeekHeight = 0.dp,
                        sheetGesturesEnabled = false,
                        drawerBackgroundColor= Color.Transparent,
                        drawerContentColor= Color.Transparent,
                        drawerScrimColor= Color.Transparent,
                        backgroundColor= Color.Transparent,
                        sheetBackgroundColor = Color.Transparent,
                        sheetContent = {
                            Snackbar(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .padding(bottom = 80.dp, start = 20.dp, end = 20.dp)
                                    .wrapContentHeight()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(5.dp)
                            ) {
                                Text(
                                    text = "Network not detected, check your network settings. Once you are connected to a network, swipe down to refresh the data\n",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    softWrap = true,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start,
                                    lineHeight = 18.sp
                                )
                            }
                        }) {
                        MainNavigation(
                            navController = navController,
                        dataStore = dataStore
                        )
                    }
                }
            }
        }
    }
}
