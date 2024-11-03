package com.sakethh.jetspacer

import android.app.Application
import androidx.room.Room
import com.sakethh.jetspacer.common.data.local.LocalDatabase

class JetSpacerApplication : Application() {
    companion object {
        private var localDatabase: LocalDatabase? = null

        fun getLocalDb(): LocalDatabase? {
            return localDatabase
        }

    }

    override fun onCreate() {
        super.onCreate()
        localDatabase = Room.databaseBuilder(
            applicationContext, LocalDatabase::class.java, LocalDatabase.DATABASE_NAME
        ).build()
    }
}