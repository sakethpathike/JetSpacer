package com.sakethh.jetspacer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sakethh.jetspacer.news.presentation.NewsScreen
import com.sakethh.jetspacer.ui.theme.JetSpacerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetSpacerTheme {
                NewsScreen()
            }
        }
    }
}