package com.sakethh.jetspacer.ui.screens.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.core.common.utils.Constants
import com.sakethh.jetspacer.domain.SettingType
import com.sakethh.jetspacer.domain.useCase.PreferencesUseCase
import com.sakethh.jetspacer.ui.AppPreferences
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class SettingsScreenViewModel(private val preferencesUseCase: PreferencesUseCase) : ViewModel() {

    companion object {
        val Context.dataStore by preferencesDataStore(Constants.DATA_STORE_NAME)
    }

    fun <T> changeSettingValue(
        preferenceKey: Preferences.Key<T>,
        newValue: T,
    ) {
        viewModelScope.launch {
            preferencesUseCase.changeSettingValue(preferenceKey, newValue)
        }
    }

    fun readAllSettingsValues() {
        viewModelScope.launch {
            awaitAll(
                async {
                    AppPreferences.isThemingSetToDefault.value =
                        preferencesUseCase.readASettingValue(
                            preferenceKey = booleanPreferencesKey(SettingType.SYSTEM_THEME.name),
                        ) ?: AppPreferences.isThemingSetToDefault.value
                },
                async {
                    AppPreferences.isDarkModeEnabled.value = preferencesUseCase.readASettingValue(
                        preferenceKey = booleanPreferencesKey(SettingType.DARK_THEME.name),
                    ) ?: AppPreferences.isDarkModeEnabled.value
                },
                async {
                    AppPreferences.isDynamicThemingEnabled.value =
                        preferencesUseCase.readASettingValue(
                            preferenceKey = booleanPreferencesKey(SettingType.DYNAMIC_THEME.name),
                        ) ?: AppPreferences.isDynamicThemingEnabled.value
                },
                async {
                    AppPreferences.nasaAPIKey.value = preferencesUseCase.readASettingValue(
                        preferenceKey = stringPreferencesKey(SettingType.NASA_API_KEY.name),
                    ) ?: AppPreferences.nasaAPIKey.value
                },
                async {
                    AppPreferences.newsApiAPIKey.value = preferencesUseCase.readASettingValue(
                        preferenceKey = stringPreferencesKey(SettingType.NEWS_API_KEY.name),
                    ) ?: AppPreferences.newsApiAPIKey.value
                },
            )
        }
    }
}