package com.sakethh.jetspacer.localDB

import android.content.Context
import android.widget.Toast
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForAPODDB
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel.BookMarkUtils.isAlertDialogEnabledForRoversDB
import io.ktor.http.auth.HttpAuthHeader.Parameters.Realm
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [NewsDB::class,APOD_DB_DTO::class, MarsRoversDBDTO::class],
    version = 1,
    exportSchema = false
)
abstract class DBImplementation : RoomDatabase() {
    abstract fun localDBData(): DBService

    companion object {
        @Volatile
        private var dbInstance: DBImplementation? = null
        fun getLocalDB(context: Context): DBImplementation {
            val instance = dbInstance
            return instance
                ?: synchronized(this) {
                    val roomDBInstance = Room.databaseBuilder(
                        context.applicationContext,
                        DBImplementation::class.java,
                        "bookmarks_db"
                    ).build()
                    dbInstance = roomDBInstance
                    return roomDBInstance
                }
        }
    }
}