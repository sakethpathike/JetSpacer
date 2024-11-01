package com.sakethh.jetspacer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.sakethh.jetspacer.common.presentation.navigation.BottomNavigationBar
import com.sakethh.jetspacer.common.presentation.navigation.MainNavigation
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UIEvent
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UiChannel
import com.sakethh.jetspacer.common.theme.JetSpacerTheme
import com.sakethh.jetspacer.home.settings.presentation.SettingsScreenViewModel.readAllSettingsValues
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            readAllSettingsValues()
        }
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val snackBarHostState = remember {
                SnackbarHostState()
            }
            JetSpacerTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                    BottomNavigationBar(navController)
                }, snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState)
                }) {
                    Surface {
                        MainNavigation(navController)
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

