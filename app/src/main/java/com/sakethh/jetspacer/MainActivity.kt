package com.sakethh.jetspacer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sakethh.jetspacer.common.presentation.navigation.BottomNavigationBar
import com.sakethh.jetspacer.common.presentation.navigation.JetSpacerNavigation
import com.sakethh.jetspacer.common.presentation.navigation.MainNavigation
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UIEvent
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UiChannel
import com.sakethh.jetspacer.common.theme.JetSpacerTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val currentBackStackEntryState = navController.currentBackStackEntryAsState()
            val snackBarHostState = remember {
                SnackbarHostState()
            }
            val systemUiController = rememberSystemUiController()
            JetSpacerTheme {
                val rootBtmNavColor = BottomAppBarDefaults.containerColor
                val materialThemeColorScheme = MaterialTheme.colorScheme

                CompositionLocalProvider(LocalNavController provides navController) {
                    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                        BottomNavigationBar()
                    }, snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState)
                    }) {
                        Surface {
                            MainNavigation()
                        }
                    }

                    LaunchedEffect(currentBackStackEntryState.value) {
                        if (listOf(
                                JetSpacerNavigation.Root.Latest,
                                JetSpacerNavigation.Root.Explore,
                                JetSpacerNavigation.Root.Headlines,
                                JetSpacerNavigation.Root.Collections
                            ).any {
                                currentBackStackEntryState.value?.destination?.hasRoute(it::class) == true
                            }
                        ) {
                            systemUiController.setNavigationBarColor(rootBtmNavColor)
                        } else {
                            systemUiController.setNavigationBarColor(materialThemeColorScheme.surface)
                        }
                    }
                }
            }
            LaunchedEffect(Unit) {
                UiChannel.uiEvent.collectLatest {
                    when (it) {
                        UIEvent.Nothing -> Unit
                        is UIEvent.ShowSnackbar -> {
                            snackBarHostState.currentSnackbarData?.dismiss()
                            snackBarHostState.showSnackbar(message = it.errorMessage)
                        }
                    }
                }
            }
        }
    }
}

