package com.sakethh.jetspacer.home.settings.domain.repository

import androidx.datastore.core.DataStore

interface SettingsDataRepository {

    suspend fun <T> changeSettingValue(
        preferenceKey: androidx.datastore.preferences.core.Preferences.Key<T>,
        dataStore: DataStore<androidx.datastore.preferences.core.Preferences>,
        newValue: T,
    )

    suspend fun <T> readASettingValue(
        preferenceKey: androidx.datastore.preferences.core.Preferences.Key<T>,
        dataStore: DataStore<androidx.datastore.preferences.core.Preferences>,
    ): T?

}