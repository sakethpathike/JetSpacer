package com.sakethh.jetspacer

import android.app.Application
import com.sakethh.jetspacer.data.repository.PreferencesRepositoryImpl
import com.sakethh.jetspacer.domain.useCase.PreferencesUseCase
import com.sakethh.jetspacer.ui.screens.settings.SettingsScreenViewModel
import com.sakethh.jetspacer.ui.screens.settings.SettingsScreenViewModel.Companion.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HyleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.Default).launch {
            SettingsScreenViewModel(PreferencesUseCase(PreferencesRepositoryImpl(applicationContext.dataStore))).readAllSettingsValues()
        }
    }
}