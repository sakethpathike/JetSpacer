package com.sakethh.jetspacer.domain.useCase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.sakethh.jetspacer.domain.repository.PreferencesRepository

class PreferencesUseCase(private val preferencesRepository: PreferencesRepository) {
    suspend fun <T> changeSettingValue(
        preferenceKey: Preferences.Key<T>,
        newValue: T,
    ) {
        preferencesRepository.changePreference(preferenceKey, newValue)
    }

    suspend fun <T> readASettingValue(
        preferenceKey: Preferences.Key<T>,
    ): T? {
        return preferencesRepository.readPreference(preferenceKey)
    }
}