package com.sakethh.jetspacer.localDB

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Database(
    entities = [NewsDB::class, APOD_DB_DTO::class, MarsRoversDBDTO::class, APIKeysDB::class, BookMarkScreenGridNames::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)],
    exportSchema = true
)
@TypeConverters(
    BookMarkTypeConverterForCustomBookMarks::class,
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
                            "    `savedDataType` TEXT NOT NULL,\n" +
                            "    PRIMARY KEY(`name`)\n" +
                            ");\n"
                )
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
                        migrations = arrayOf(MigrationFrom1To2)
                    )
                        .build()
                    dbInstance = roomDBInstance
                    return roomDBInstance
                }
        }
    }
}

class BookMarkTypeConverterForCustomBookMarks {

    @TypeConverter
    fun convertToString(value: SavedDataType): String {
        return value.name
    }

    @TypeConverter
    fun retrieveConvertedData(value: String): SavedDataType {
        return enumValueOf(value)
    }
}

class BookMarkDataConverterForCustomBookMarks {
    private val json = Json { encodeDefaults = true }

    @TypeConverter
    fun convertToString(value: List<BookMarkType>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun convertToList(value: String): List<BookMarkType> {
        return json.decodeFromString(value)
    }
}