package com.sakethh.jetspacer.ui.screens.settings

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import coil3.imageLoader
import com.sakethh.jetspacer.core.common.utils.Constants
import com.sakethh.jetspacer.data.repository.PreferencesRepositoryImpl
import com.sakethh.jetspacer.domain.SettingType
import com.sakethh.jetspacer.domain.useCase.PreferencesUseCase
import com.sakethh.jetspacer.ui.AppPreferences
import com.sakethh.jetspacer.ui.LocalNavController
import com.sakethh.jetspacer.ui.screens.settings.SettingsScreenViewModel.Companion.dataStore
import com.sakethh.jetspacer.ui.screens.settings.components.SettingsCredentialsItem
import com.sakethh.jetspacer.ui.screens.settings.components.SettingsItem
import com.sakethh.jetspacer.ui.screens.settings.components.SettingsSwitchItem
import com.sakethh.jetspacer.ui.utils.UIChannel
import com.sakethh.jetspacer.ui.utils.pushUIEvent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val navController: NavController = LocalNavController.current
    val topAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val settingsScreenViewModel: SettingsScreenViewModel = viewModel(factory = viewModelFactory {
        initializer {
            SettingsScreenViewModel(PreferencesUseCase(PreferencesRepositoryImpl(context.dataStore)))
        }
    })
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
            if (AppPreferences.isDarkModeEnabled.value.not()) {
                item {
                    SettingsSwitchItem(
                        "Follow System Theme",
                        isEnabled = AppPreferences.isThemingSetToDefault.value,
                        onClick = {
                            AppPreferences.isThemingSetToDefault.value = it
                            settingsScreenViewModel.changeSettingValue(
                                preferenceKey = booleanPreferencesKey(SettingType.SYSTEM_THEME.name),
                                newValue = AppPreferences.isThemingSetToDefault.value
                            )
                        })
                }
            }
            if (AppPreferences.isThemingSetToDefault.value.not()) {
                item {
                    SettingsSwitchItem(
                        "Use Dark Theme",
                        isEnabled = AppPreferences.isDarkModeEnabled.value,
                        onClick = {
                            AppPreferences.isDarkModeEnabled.value = it
                            settingsScreenViewModel.changeSettingValue(
                                preferenceKey = booleanPreferencesKey(SettingType.DARK_THEME.name),
                                newValue = AppPreferences.isDarkModeEnabled.value
                            )
                        })
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                item {
                    SettingsSwitchItem(
                        "Use Dynamic Theming",
                        isEnabled = AppPreferences.isDynamicThemingEnabled.value,
                        onClick = {
                            AppPreferences.isDynamicThemingEnabled.value = it
                            settingsScreenViewModel.changeSettingValue(
                                preferenceKey = booleanPreferencesKey(SettingType.DYNAMIC_THEME.name),
                                newValue = AppPreferences.isDynamicThemingEnabled.value
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
                    value = AppPreferences.nasaAPIKey.value,
                    onResetDefault = {
                        AppPreferences.nasaAPIKey.value = Constants.NASA_API_KEY
                        settingsScreenViewModel.changeSettingValue(
                            preferenceKey = stringPreferencesKey(SettingType.NASA_API_KEY.name),
                            newValue = AppPreferences.nasaAPIKey.value
                        )
                    },
                    onSaveClick = {
                        AppPreferences.nasaAPIKey.value = it
                        settingsScreenViewModel.changeSettingValue(
                            preferenceKey = stringPreferencesKey(SettingType.NASA_API_KEY.name),
                            newValue = AppPreferences.nasaAPIKey.value
                        )
                    })
            }
            item {
                SettingsCredentialsItem(
                    domain = "newsapi.org",
                    value = AppPreferences.newsApiAPIKey.value,
                    onResetDefault = {
                        AppPreferences.newsApiAPIKey.value = Constants.NEWS_API_KEY
                        settingsScreenViewModel.changeSettingValue(
                            preferenceKey = stringPreferencesKey(SettingType.NEWS_API_KEY.name),
                            newValue = AppPreferences.newsApiAPIKey.value
                        )
                    },
                    onSaveClick = {
                        AppPreferences.newsApiAPIKey.value = it
                        settingsScreenViewModel.changeSettingValue(
                            preferenceKey = stringPreferencesKey(SettingType.NEWS_API_KEY.name),
                            newValue = AppPreferences.newsApiAPIKey.value
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
                    icon = Icons.Default.BrokenImage, title = "Clear Image cache", onClick = {
                        navController.context.imageLoader.memoryCache?.clear()
                        navController.context.imageLoader.diskCache?.clear()
                        coroutineScope.launch {
                            pushUIEvent(UIChannel.Type.ShowSnackbar(errorMessage = "Cleared image cache"))
                        }
                    })
            }
            item {
                Spacer(Modifier.height(150.dp))
            }
        }
    }
}