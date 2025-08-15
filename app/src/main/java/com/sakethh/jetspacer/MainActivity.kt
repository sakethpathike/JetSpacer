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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.sakethh.jetspacer.ui.LocalNavController
import com.sakethh.jetspacer.ui.navigation.BottomNavigationBar
import com.sakethh.jetspacer.ui.navigation.HyleNavHost
import com.sakethh.jetspacer.ui.theme.HyleTheme
import com.sakethh.jetspacer.ui.utils.uiEvent.UIEvent
import com.sakethh.jetspacer.ui.utils.uiEvent.UiChannel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val snackBarHostState = remember {
                SnackbarHostState()
            }
            HyleTheme {
                CompositionLocalProvider(LocalNavController provides navController) {
                    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
                        BottomNavigationBar()
                    }, snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState)
                    }) {
                        Surface {
                            HyleNavHost()
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

