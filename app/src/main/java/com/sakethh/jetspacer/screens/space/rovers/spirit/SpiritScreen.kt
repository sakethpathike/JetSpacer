package com.sakethh.jetspacer.screens.space.rovers.spirit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun SpiritRoverScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "SpiritRoverScreen",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 22.sp,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}