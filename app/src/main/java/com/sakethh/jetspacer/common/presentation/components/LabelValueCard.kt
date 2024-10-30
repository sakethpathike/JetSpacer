package com.sakethh.jetspacer.common.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LabelValueCard(
    title: String,
    value: String,
    innerPadding: Dp = 10.dp,
    outerPaddingValues: PaddingValues = PaddingValues(start = 15.dp, end = 15.dp),
    modifier: Modifier = Modifier
) {
    Column(
        Modifier
            .padding(outerPaddingValues)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primary.copy(0.25f))
            .padding(innerPadding)
            .then(modifier)
    ) {
        if (title.isNotBlank()) {
            Text(
                title,
                fontSize = 12.sp,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(Modifier.height(2.dp))
        }
        Text(
            value,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}