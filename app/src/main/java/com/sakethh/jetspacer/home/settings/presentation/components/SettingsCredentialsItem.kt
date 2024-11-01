package com.sakethh.jetspacer.home.settings.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsCredentialsItem(
    domain: String,
    value: String,
    onResetDefault: () -> Unit,
    onSaveClick: (newValue: String) -> Unit
) {
    val textFieldReadOnly = rememberSaveable {
        mutableStateOf(true)
    }
    val textFieldValue = rememberSaveable(value) {
        mutableStateOf(value)
    }
    val textFieldFocusRequester = remember {
        FocusRequester()
    }
    Card(
        border = BorderStroke(
            1.dp, contentColorFor(MaterialTheme.colorScheme.surface)
        ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 15.dp)
    ) {
        Column(Modifier.padding(15.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier
                        .padding(
                            end = 15.dp
                        )
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(0.1f),
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(5.dp)
                        .horizontalScroll(rememberScrollState()),
                    text = domain,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp
                )
            }
        }
        TextField(
            label = {
                Text(
                    text = "",
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Start,
                    fontSize = 12.sp
                )
            },
            readOnly = textFieldReadOnly.value,
            value = textFieldValue.value,
            onValueChange = {
                textFieldValue.value = it
            },
            textStyle = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp)
                .fillMaxWidth()
                .focusRequester(textFieldFocusRequester),
        )
        Spacer(Modifier.height(15.dp))
        Column(modifier = Modifier.animateContentSize()) {
            if (!textFieldReadOnly.value) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp),
                    onClick = {
                        onSaveClick(textFieldValue.value)
                        textFieldReadOnly.value = true
                    }
                ) {
                    Text("Save", style = MaterialTheme.typography.titleSmall)
                    textFieldFocusRequester.freeFocus()
                }
            } else {
                FilledTonalButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp),
                    onClick = {
                        onResetDefault()
                    }
                ) {
                    Text("Reset to default", style = MaterialTheme.typography.titleSmall)
                }
                Spacer(Modifier.height(5.dp))
                Button(
                    onClick = {
                        textFieldReadOnly.value = false
                        textFieldFocusRequester.requestFocus()
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp)
                ) {
                    Text("Edit", style = MaterialTheme.typography.titleSmall)
                }
            }
        }
        Spacer(Modifier.height(15.dp))
    }
}