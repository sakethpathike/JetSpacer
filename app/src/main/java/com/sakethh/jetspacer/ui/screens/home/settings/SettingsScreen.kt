package com.sakethh.jetspacer.ui.screens.home.settings

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.sakethh.jetspacer.common.utils.Constants
import com.sakethh.jetspacer.domain.SettingType
import com.sakethh.jetspacer.ui.GlobalSettings
import com.sakethh.jetspacer.ui.LocalNavController
import com.sakethh.jetspacer.ui.screens.home.settings.SettingsScreenViewModel.dataStore
import com.sakethh.jetspacer.ui.screens.home.settings.components.SettingsCredentialsItem
import com.sakethh.jetspacer.ui.screens.home.settings.components.SettingsItem
import com.sakethh.jetspacer.ui.screens.home.settings.components.SettingsSwitchItem
import com.sakethh.jetspacer.ui.utils.uiEvent.UIEvent
import com.sakethh.jetspacer.ui.utils.uiEvent.UiChannel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun SettingsScreen() {
    val navController: NavController = LocalNavController.current
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
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
                Icon(Icons.Filled.ArrowBack, null)
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
                            SettingsScreenViewModel.changeSettingValue(
                                preferenceKey = booleanPreferencesKey(SettingType.SYSTEM_THEME.name),
                                dataStore = context.dataStore,
                                newValue = GlobalSettings.isThemingSetToDefault.value
                            )
                        })
                }
            }
            if (GlobalSettings.isThemingSetToDefault.value.not()) {
                item {
                    SettingsSwitchItem("Use Dark Theme",
                        isEnabled = GlobalSettings.isDarkModeEnabled.value,
                        onClick = {
                            GlobalSettings.isDarkModeEnabled.value = it
                            SettingsScreenViewModel.changeSettingValue(
                                preferenceKey = booleanPreferencesKey(SettingType.DARK_THEME.name),
                                dataStore = context.dataStore,
                                newValue = GlobalSettings.isDarkModeEnabled.value
                            )
                        })
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                item {
                    SettingsSwitchItem(
                        "Use Dynamic Theming",
                        isEnabled = GlobalSettings.isDynamicThemingEnabled.value,
                        onClick = {
                            GlobalSettings.isDynamicThemingEnabled.value = it
                            SettingsScreenViewModel.changeSettingValue(
                                preferenceKey = booleanPreferencesKey(SettingType.DYNAMIC_THEME.name),
                                dataStore = context.dataStore,
                                newValue = GlobalSettings.isDynamicThemingEnabled.value
                            )
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
                    onResetDefault = {
                        GlobalSettings.nasaAPIKey.value = Constants.NASA_API_KEY
                        SettingsScreenViewModel.changeSettingValue(
                            preferenceKey = stringPreferencesKey(SettingType.NASA_API_KEY.name),
                            dataStore = context.dataStore,
                            newValue = GlobalSettings.nasaAPIKey.value
                        )
                    }, onSaveClick = {
                        GlobalSettings.nasaAPIKey.value = it
                        SettingsScreenViewModel.changeSettingValue(
                            preferenceKey = stringPreferencesKey(SettingType.NASA_API_KEY.name),
                            dataStore = context.dataStore,
                            newValue = GlobalSettings.nasaAPIKey.value
                        )
                    })
            }
            item {
                SettingsCredentialsItem(
                    domain = "newsapi.org",
                    value = GlobalSettings.newsApiAPIKey.value,
                    onResetDefault = {
                        GlobalSettings.newsApiAPIKey.value = Constants.NEWS_API_KEY
                        SettingsScreenViewModel.changeSettingValue(
                            preferenceKey = stringPreferencesKey(SettingType.NEWS_API_KEY.name),
                            dataStore = context.dataStore,
                            newValue = GlobalSettings.newsApiAPIKey.value
                        )
                    }, onSaveClick = {
                        GlobalSettings.newsApiAPIKey.value = it
                        SettingsScreenViewModel.changeSettingValue(
                            preferenceKey = stringPreferencesKey(SettingType.NEWS_API_KEY.name),
                            dataStore = context.dataStore,
                            newValue = GlobalSettings.newsApiAPIKey.value
                        )
                    })
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
                    title = "Clear local database", onClick = {
                        SettingsScreenViewModel.clearEntireDatabase()
                    })
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
            item {
                Spacer(Modifier.height(150.dp))
            }
        }
    }
}