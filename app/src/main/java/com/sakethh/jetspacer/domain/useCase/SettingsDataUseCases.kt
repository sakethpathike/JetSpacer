package com.sakethh.jetspacer.domain.useCase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.sakethh.jetspacer.data.repository.SettingsDataImplementation
import com.sakethh.jetspacer.domain.repository.SettingsDataRepository

class SettingsDataUseCases(private val settingsDataRepository: SettingsDataRepository = SettingsDataImplementation()) {
    suspend fun <T> changeSettingValue(
        preferenceKey: Preferences.Key<T>,
        dataStore: DataStore<Preferences>,
        newValue: T,
    ) {
        settingsDataRepository.changeSettingValue(preferenceKey, dataStore, newValue)
    }

    suspend fun <T> readASettingValue(
        preferenceKey: Preferences.Key<T>,
        dataStore: DataStore<Preferences>,
    ): T? {
        return settingsDataRepository.readASettingValue(preferenceKey, dataStore)
    }
}