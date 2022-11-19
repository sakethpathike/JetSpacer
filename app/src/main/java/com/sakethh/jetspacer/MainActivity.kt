package com.sakethh.jetspacer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sakethh.jetspacer.screens.HTTPClient
import com.sakethh.jetspacer.screens.home.HomeScreen
import com.sakethh.jetspacer.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                HomeScreen()
            }
        }
    }
}