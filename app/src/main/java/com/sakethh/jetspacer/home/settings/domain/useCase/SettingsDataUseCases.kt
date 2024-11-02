package com.sakethh.jetspacer.home.settings.domain.useCase

import androidx.datastore.core.DataStore
import com.sakethh.jetspacer.home.settings.data.repository.SettingsDataImplementation
import com.sakethh.jetspacer.home.settings.domain.repository.SettingsDataRepository

class SettingsDataUseCases(private val settingsDataRepository: SettingsDataRepository = SettingsDataImplementation()) {
    suspend fun <T> changeSettingValue(
        preferenceKey: androidx.datastore.preferences.core.Preferences.Key<T>,
        dataStore: DataStore<androidx.datastore.preferences.core.Preferences>,
        newValue: T,
    ) {
        settingsDataRepository.changeSettingValue(preferenceKey, dataStore, newValue)
    }

    suspend fun <T> readASettingValue(
        preferenceKey: androidx.datastore.preferences.core.Preferences.Key<T>,
        dataStore: DataStore<androidx.datastore.preferences.core.Preferences>,
    ): T? {
        return settingsDataRepository.readASettingValue(preferenceKey, dataStore)
    }
}