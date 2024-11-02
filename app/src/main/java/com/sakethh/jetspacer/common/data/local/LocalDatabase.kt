package com.sakethh.jetspacer.common.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sakethh.jetspacer.common.data.local.data.typeConverter.CameraXTypeConverter
import com.sakethh.jetspacer.common.data.local.domain.model.APOD
import com.sakethh.jetspacer.common.data.local.domain.model.Headline
import com.sakethh.jetspacer.common.data.local.domain.model.rover.Rover
import com.sakethh.jetspacer.common.data.local.domain.model.rover.RoverImage

@Database(entities = [Headline::class, APOD::class, RoverImage::class, Rover::class], version = 1)
@TypeConverters(CameraXTypeConverter::class)
abstract class LocalDatabase : RoomDatabase() {


    companion object {
        const val DATABASE_NAME = "JetSpacerLocalDatabase"
    }

}