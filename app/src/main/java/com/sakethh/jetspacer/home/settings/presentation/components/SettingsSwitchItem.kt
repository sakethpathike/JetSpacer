package com.sakethh.jetspacer.home.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsSwitchItem(settingTitle: String, onClick: (Boolean) -> Unit, isEnabled: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(!isEnabled)
            }
            .padding(top = 7.5.dp, bottom = 7.5.dp, start = 15.dp, end = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = settingTitle,
            modifier = Modifier.fillMaxWidth(0.75f),
            style = MaterialTheme.typography.titleSmall,
            fontSize = 16.sp
        )
        Switch(checked = isEnabled, onCheckedChange = {
            onClick(it)
        })
    }
}