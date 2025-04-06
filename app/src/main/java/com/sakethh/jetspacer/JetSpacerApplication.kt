package com.sakethh.jetspacer

import android.app.Application
import androidx.room.Room
import com.sakethh.jetspacer.common.data.local.LocalDatabase
import com.sakethh.jetspacer.home.settings.presentation.SettingsScreenViewModel.readAllSettingsValues
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class JetSpacerApplication : Application() {
    companion object {
        private var localDatabase: LocalDatabase? = null

        fun getLocalDb(): LocalDatabase? {
            return localDatabase
        }

    }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.Default).launch {
            awaitAll(async {
                this@JetSpacerApplication.readAllSettingsValues()
            }, async {
                localDatabase = Room.databaseBuilder(
                    applicationContext, LocalDatabase::class.java, LocalDatabase.DATABASE_NAME
                ).build()
            })
        }
    }
}