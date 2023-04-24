package com.sakethh.jetspacer.localDB

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sakethh.jetspacer.Constants
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Database(
    entities = [NewsDB::class, APOD_DB_DTO::class, MarsRoversDBDTO::class, APIKeysDB::class, BookMarkScreenGridNames::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 1, to = 2), AutoMigration(from = 2, to = 3)],
    exportSchema = true
)
@TypeConverters(
    BookMarkDataConverterForListOfCustomBookMarks::class,
    BookMarkDataConverterForCustomBookMarks::class
)
abstract class DBImplementation : RoomDatabase() {
    abstract fun localDBData(): DBService
    

    companion object {

        private val MigrationFrom1To2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `bookMarkScreen_GridNames` (\n" +
                            "    `name` TEXT NOT NULL,\n" +
                            "    `imgUrlForGrid` TEXT NOT NULL,\n" +
                            "    `data` TEXT NOT NULL,\n" +
                            "    PRIMARY KEY(`name`)\n" +
                            ");\n"
                )
            }
        }

        private val MigrationFrom2To3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE apiKeys ADD COLUMN currentIPGeoLocationAPIKey TEXT NOT NULL DEFAULT '${Constants.IP_GEOLOCATION_APIKEY}'")
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
                    ).addMigrations(
                        migrations = arrayOf(MigrationFrom1To2, MigrationFrom2To3)
                    )
                        .build()
                    dbInstance = roomDBInstance
                    return roomDBInstance
                }
        }
    }
}

class BookMarkDataConverterForListOfCustomBookMarks {
    private val json = Json { encodeDefaults = true }

    @TypeConverter
    fun convertToString(value: List<CustomBookMarkData>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun convertToList(value: String): List<CustomBookMarkData> {
        return json.decodeFromString(value)
    }
}
class BookMarkDataConverterForCustomBookMarks {
    private val json = Json { encodeDefaults = true }

    @TypeConverter
    fun convertToString(value: CustomBookMarkData): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun convertToList(value: String): CustomBookMarkData {
        return json.decodeFromString(value)
    }
}