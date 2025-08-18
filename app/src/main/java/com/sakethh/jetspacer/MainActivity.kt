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
import com.sakethh.jetspacer.ui.navigation.HyleBottomNavBar
import com.sakethh.jetspacer.ui.navigation.HyleNavHost
import com.sakethh.jetspacer.ui.theme.HyleTheme
import com.sakethh.jetspacer.ui.utils.UIChannel
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
                        HyleBottomNavBar()
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
                UIChannel.readChannel.collectLatest {
                    when (it) {
                        UIChannel.Type.Nothing -> Unit
                        is UIChannel.Type.ShowSnackbar -> {
                            snackBarHostState.currentSnackbarData?.dismiss()
                            snackBarHostState.showSnackbar(message = it.errorMessage)
                        }
                    }
                }
            }
        }
    }
}

