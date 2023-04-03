package com.sakethh.jetspacer.localDB

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [NewsDB::class, APOD_DB_DTO::class, MarsRoversDBDTO::class, APIKeysDB::class, BookMarkScreenGridNames::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)],
    exportSchema = true
)
abstract class DBImplementation : RoomDatabase() {
    abstract fun localDBData(): DBService

    companion object {
        private val MigrationFrom1To2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `bookMarkScreen_GridNames` (\n" +
                        "  `name` TEXT NOT NULL PRIMARY KEY,\n" +
                        "  `imgUrlForGrid` TEXT\n" +
                        ")\n")
            }
        }
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
                    ).addMigrations(migrations = arrayOf(MigrationFrom1To2)).build()
                    dbInstance = roomDBInstance
                    return roomDBInstance
                }
        }
    }
}