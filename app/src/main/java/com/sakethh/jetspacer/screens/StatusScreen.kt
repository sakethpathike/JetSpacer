package com.sakethh.jetspacer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sakethh.jetspacer.ui.theme.AppTheme

@Composable
fun StatusScreen(title: String, description: String, status: Status) {
    val scrollState = rememberScrollState()
    val statusTitle =
        if (status == Status.NO_INTERNET) {
            "Network not detected\n(•ˋ _ ˊ•)"
        } else {
            title
        }
    val statusDescription =
        if (status == Status.NO_INTERNET) {
            "Connect to network and try again \uD83D\uDC4D"
        } else {
            description
        }
    AppTheme {
        Column(
            modifier = Modifier
                .background(androidx.compose.material3.MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Text(
                text = statusTitle,
                fontSize = 45.sp,
                style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                lineHeight = 47.sp,
                softWrap = true,
                modifier = Modifier.padding(top = 75.dp, start = 25.dp, end = 50.dp)
            )
            Text(
                text = statusDescription,
                fontSize = 25.sp,
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                lineHeight = 30.sp,
                softWrap = true,
                modifier = Modifier.padding(top = 15.dp, start = 25.dp, end = 20.dp)
            )
            if (status == Status.LOADING) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(start = 25.dp, top = 22.dp)
                        .size(40.dp),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                    strokeWidth = 4.dp
                )
            }
        }

    }
}

enum class Status {
    FOURO4InMarsScreen,
    LOADING,
    BOOKMARKS_EMPTY,
    NO_INTERNET
}