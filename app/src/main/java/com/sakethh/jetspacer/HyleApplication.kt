package com.sakethh.jetspacer

import android.app.Application
import com.sakethh.jetspacer.data.repository.PreferencesRepositoryImpl
import com.sakethh.jetspacer.domain.useCase.PreferencesUseCase
import com.sakethh.jetspacer.ui.screens.settings.SettingsScreenViewModel
import com.sakethh.jetspacer.ui.screens.settings.SettingsScreenViewModel.Companion.dataStore
import com.sakethh.jetspacer.ui.utils.UIChannel
import com.sakethh.jetspacer.ui.utils.pushUIEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HyleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->

            // usually this should redirect to a new activity and show the log, but this is fine for us
            e.printStackTrace()
            e.message?.let {
                CoroutineScope(Dispatchers.Default).launch {
                    pushUIEvent(type = UIChannel.Type.ShowSnackbar(it))
                }
            }
        }
        CoroutineScope(Dispatchers.Default).launch {
            SettingsScreenViewModel(PreferencesUseCase(PreferencesRepositoryImpl(applicationContext.dataStore))).readAllSettingsValues()
        }
    }
}