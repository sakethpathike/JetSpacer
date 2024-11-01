package com.sakethh.jetspacer.home.settings.presentation

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UIEvent
import com.sakethh.jetspacer.common.presentation.utils.uiEvent.UiChannel
import com.sakethh.jetspacer.home.settings.presentation.components.SettingsCredentialsItem
import com.sakethh.jetspacer.home.settings.presentation.components.SettingsItem
import com.sakethh.jetspacer.home.settings.presentation.components.SettingsSwitchItem
import com.sakethh.jetspacer.home.settings.presentation.utils.GlobalSettings

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        MediumTopAppBar(scrollBehavior = topAppBarScrollBehavior, title = {
            Column {
                Text(
                    text = "Settings", style = MaterialTheme.typography.titleSmall, fontSize = 18.sp
                )
            }
        }, navigationIcon = {
            IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        })
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
        ) {
            item {
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(15.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if (GlobalSettings.isDarkModeEnabled.value.not()) {
                item {
                    SettingsSwitchItem("Follow System Theme",
                        isEnabled = GlobalSettings.isThemingSetToDefault.value,
                        onClick = {
                            GlobalSettings.isThemingSetToDefault.value = it
                        })
                }
            }
            if (GlobalSettings.isThemingSetToDefault.value.not()) {
                item {
                    SettingsSwitchItem("Use Dark Theme",
                        isEnabled = GlobalSettings.isDarkModeEnabled.value,
                        onClick = {
                            GlobalSettings.isDarkModeEnabled.value = it
                        })
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                item {
                    SettingsSwitchItem("Use dynamic theming",
                        isEnabled = GlobalSettings.isDynamicThemingEnabled.value,
                        onClick = {
                            GlobalSettings.isDynamicThemingEnabled.value = it
                        })
                }
            }
            item {
                Text(
                    text = "Credentials",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(start = 15.dp, top = 15.dp, bottom = 7.5.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                SettingsCredentialsItem(
                    domain = "api.nasa.gov",
                    value = GlobalSettings.nasaAPIKey.value,
                    onResetDefault = {}) { }
            }
            item {
                SettingsCredentialsItem(
                    domain = "newsapi.org",
                    value = GlobalSettings.newsApiAPIKey.value,
                    onResetDefault = {}) { }
            }
            item {
                Text(
                    text = "Data",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(15.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                SettingsItem(
                    icon = Icons.Default.Storage,
                    title = "Clear local database",
                    onClick = {})
            }
            item {
                SettingsItem(
                    icon = Icons.Default.BrokenImage,
                    title = "Clear Image cache",
                    onClick = {
                        navController.context.imageLoader.memoryCache?.clear()
                        navController.context.imageLoader.diskCache?.clear()
                        UiChannel.pushUiEvent(
                            uiEvent = UIEvent.ShowSnackbar(errorMessage = "Cleared image cache"),
                            coroutineScope
                        )
                    })
            }
        }
    }
}