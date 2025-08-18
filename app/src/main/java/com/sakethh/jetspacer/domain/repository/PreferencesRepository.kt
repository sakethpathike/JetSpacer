package com.sakethh.jetspacer.domain.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

interface PreferencesRepository {

    val dataStore: DataStore<Preferences>

    suspend fun <T> changePreference(
        preferenceKey: Preferences.Key<T>,
        newValue: T,
    )

    suspend fun <T> readPreference(
        preferenceKey: Preferences.Key<T>,
    ): T?

}