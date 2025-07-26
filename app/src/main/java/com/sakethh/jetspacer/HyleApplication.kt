package com.sakethh.jetspacer

import android.app.Application
import androidx.room.Room
import com.sakethh.jetspacer.data.LocalDatabase
import com.sakethh.jetspacer.ui.screens.home.settings.SettingsScreenViewModel.readAllSettingsValues
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class HyleApplication : Application() {
    companion object {
        private lateinit var localDatabase: LocalDatabase

        fun getLocalDb(): LocalDatabase {
            return localDatabase
        }

    }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.Default).launch {
            awaitAll(async {
                this@HyleApplication.readAllSettingsValues()
            }, async {
                localDatabase = Room.databaseBuilder(
                    applicationContext, LocalDatabase::class.java, LocalDatabase.DATABASE_NAME
                ).build()
            })
        }
    }
}