package com.sakethh.jetspacer.ui.screens.home.settings

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.JetSpacerApplication
import com.sakethh.jetspacer.common.utils.Constants
import com.sakethh.jetspacer.domain.SettingType
import com.sakethh.jetspacer.domain.useCase.SettingsDataUseCases
import com.sakethh.jetspacer.ui.GlobalSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

object SettingsScreenViewModel : ViewModel() {
    private val settingsDataUseCases: SettingsDataUseCases = SettingsDataUseCases()
    val Context.dataStore by preferencesDataStore(Constants.DATA_STORE_NAME)

    fun clearEntireDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            JetSpacerApplication.getLocalDb()?.clearAllTables()
        }
    }

    fun <T> changeSettingValue(
        preferenceKey: Preferences.Key<T>,
        dataStore: DataStore<Preferences>,
        newValue: T,
    ) {
        viewModelScope.launch {
            settingsDataUseCases.changeSettingValue(preferenceKey, dataStore, newValue)
        }
    }

    fun Application.readAllSettingsValues() {
        viewModelScope.launch {
            awaitAll(
                async {
                    GlobalSettings.isThemingSetToDefault.value =
                        settingsDataUseCases.readASettingValue(
                            preferenceKey = booleanPreferencesKey(SettingType.SYSTEM_THEME.name),
                            dataStore = this@readAllSettingsValues.dataStore
                        ) ?: GlobalSettings.isThemingSetToDefault.value
                },
                async {
                    GlobalSettings.isDarkModeEnabled.value =
                        settingsDataUseCases.readASettingValue(
                            preferenceKey = booleanPreferencesKey(SettingType.DARK_THEME.name),
                            dataStore = this@readAllSettingsValues.dataStore
                        ) ?: GlobalSettings.isDarkModeEnabled.value
                },
                async {
                    GlobalSettings.isDynamicThemingEnabled.value =
                        settingsDataUseCases.readASettingValue(
                            preferenceKey = booleanPreferencesKey(SettingType.DYNAMIC_THEME.name),
                            dataStore = this@readAllSettingsValues.dataStore
                        ) ?: GlobalSettings.isDynamicThemingEnabled.value
                },
                async {
                    GlobalSettings.nasaAPIKey.value =
                        settingsDataUseCases.readASettingValue(
                            preferenceKey = stringPreferencesKey(SettingType.NASA_API_KEY.name),
                            dataStore = this@readAllSettingsValues.dataStore
                        ) ?: GlobalSettings.nasaAPIKey.value
                },
                async {
                    GlobalSettings.newsApiAPIKey.value =
                        settingsDataUseCases.readASettingValue(
                            preferenceKey = stringPreferencesKey(SettingType.NEWS_API_KEY.name),
                            dataStore = this@readAllSettingsValues.dataStore
                        ) ?: GlobalSettings.newsApiAPIKey.value
                },
            )
        }
    }
}