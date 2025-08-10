package com.sakethh.jetspacer.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

fun Modifier.iconModifier(colorScheme: ColorScheme, onClick: () -> Unit): Modifier {
    return this
        .padding(start = 5.dp, end = 5.dp)
        .clip(RoundedCornerShape(15.dp))
        .clickable {
            onClick()
        }
        .background(colorScheme.primary.copy(0.1f))
        .padding(10.dp)
        .size(20.dp)
}