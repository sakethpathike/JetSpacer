package com.sakethh.jetspacer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sakethh.jetspacer.data.dao.APODDao
import com.sakethh.jetspacer.data.dao.RoverDAO
import com.sakethh.jetspacer.data.dao.headline.TopHeadlineCacheDao
import com.sakethh.jetspacer.data.dao.headline.TopHeadlinesDao
import com.sakethh.jetspacer.data.typeConverter.CameraXTypeConverter
import com.sakethh.jetspacer.domain.model.APOD
import com.sakethh.jetspacer.domain.model.Headline
import com.sakethh.jetspacer.domain.model.cache.TopHeadlinesCache
import com.sakethh.jetspacer.domain.model.rover.Rover
import com.sakethh.jetspacer.domain.model.rover.RoverImage

@Database(
    entities = [Headline::class, APOD::class, RoverImage::class, Rover::class, TopHeadlinesCache::class],
    version = 1
)
@TypeConverters(CameraXTypeConverter::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract val topHeadlinesDao: TopHeadlinesDao
    abstract val topHeadlineCacheDao: TopHeadlineCacheDao
    abstract val apodDao: APODDao
    abstract val roverImagesDao: RoverDAO

    companion object {
        const val DATABASE_NAME = "JetSpacerLocalDatabase"
    }

}