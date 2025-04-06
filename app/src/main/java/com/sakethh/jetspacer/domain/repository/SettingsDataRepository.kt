package com.sakethh.jetspacer.domain.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

interface SettingsDataRepository {

    suspend fun <T> changeSettingValue(
        preferenceKey: Preferences.Key<T>,
        dataStore: DataStore<Preferences>,
        newValue: T,
    )

    suspend fun <T> readASettingValue(
        preferenceKey: Preferences.Key<T>,
        dataStore: DataStore<Preferences>,
    ): T?

}