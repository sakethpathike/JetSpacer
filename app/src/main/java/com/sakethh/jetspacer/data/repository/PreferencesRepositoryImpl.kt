package com.sakethh.jetspacer.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.sakethh.jetspacer.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.first

class PreferencesRepositoryImpl(override val dataStore: DataStore<Preferences>) : PreferencesRepository {

    override suspend fun <T> changePreference(
        preferenceKey: Preferences.Key<T>, newValue: T
    ) {
        dataStore.edit {
            it[preferenceKey] = newValue
        }
    }

    override suspend fun <T> readPreference(
        preferenceKey: Preferences.Key<T>
    ): T? {
        return dataStore.data.first()[preferenceKey]
    }
}