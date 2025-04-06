package com.sakethh.jetspacer.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.sakethh.jetspacer.domain.repository.SettingsDataRepository
import kotlinx.coroutines.flow.first

class SettingsDataImplementation : SettingsDataRepository {
    override suspend fun <T> changeSettingValue(
        preferenceKey: Preferences.Key<T>, dataStore: DataStore<Preferences>, newValue: T
    ) {
        dataStore.edit {
            it[preferenceKey] = newValue
        }
    }

    override suspend fun <T> readASettingValue(
        preferenceKey: Preferences.Key<T>, dataStore: DataStore<Preferences>
    ): T? {
        return dataStore.data.first()[preferenceKey]
    }
}