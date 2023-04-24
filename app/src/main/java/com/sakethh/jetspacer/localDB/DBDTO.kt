package com.sakethh.jetspacer.localDB

import androidx.room.*
import com.sakethh.jetspacer.Constants
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
sealed class BookMarkType

@Entity(tableName = "apod_db")
@Serializable
data class APOD_DB_DTO(
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "datePublished")
    var datePublished: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @PrimaryKey
    @ColumnInfo(name = "imageURL")
    var imageURL: String = "",
    @ColumnInfo(name = "hdImageURL")
    var hdImageURL: String = "",
    @ColumnInfo(name = "mediaType")
    var mediaType: String = "",
    @ColumnInfo(name = "isBookMarked")
    var isBookMarked: Boolean = false,
    @ColumnInfo(name = "category")
    var category: String = "APOD",
    @ColumnInfo(name = "addedToLocalDBOn")
    var addedToLocalDBOn: String = "",
):BookMarkType()

@Entity(tableName = "apiKeys")
@Serializable
data class APIKeysDB(
    var currentNASAAPIKey: String = "",
    var currentNewsAPIKey: String = "",
    @ColumnInfo(defaultValue = Constants.IP_GEOLOCATION_APIKEY)
    var currentIPGeoLocationAPIKey: String = "",
    @PrimaryKey
    var id: String = "apiKey",
):BookMarkType()

@Entity(tableName = "marsRovers_db")
@Serializable
data class MarsRoversDBDTO(
    @PrimaryKey
    @ColumnInfo(name = "imageURL")
    var imageURL: String = "",
    @ColumnInfo(name = "capturedBy")
    var capturedBy: String = "",
    @ColumnInfo(name = "sol")
    var sol: String = "",
    @ColumnInfo(name = "earthDate")
    var earthDate: String = "",
    @ColumnInfo(name = "roverName")
    var roverName: String = "",
    @ColumnInfo(name = "roverStatus")
    var roverStatus: String = "",
    @ColumnInfo(name = "launchingDate")
    var launchingDate: String = "",
    @ColumnInfo(name = "landingDate")
    var landingDate: String = "",
    @ColumnInfo(name = "isBookMarked")
    var isBookMarked: Boolean = false,
    @ColumnInfo(name = "category")
    var category: String = "Rover",
    @ColumnInfo(name = "addedToLocalDBOn")
    var addedToLocalDBOn: String = "",
):BookMarkType()

@Serializable
data class CustomBookMarkData(val dataType: SavedDataType,val data:BookMarkType)
@Entity(tableName = "bookMarkScreen_GridNames")
@TypeConverters(BookMarkDataConverterForListOfCustomBookMarks::class,BookMarkDataConverterForCustomBookMarks::class)
@Serializable
data class BookMarkScreenGridNames( // for custom bookmark topic
    @PrimaryKey
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "imgUrlForGrid")
    var imgUrlForGrid: String,
    @ColumnInfo(name = "data")
    @TypeConverters(BookMarkDataConverterForListOfCustomBookMarks::class)
    var data: List<CustomBookMarkData>
)
@Serializable
enum class SavedDataType {
    APOD, ROVERS, NEWS, ALL, NONE
}

@Entity(tableName = "newsDB")
@Serializable
data class NewsDB(
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "imageURL")
    var imageURL: String = "",
    @PrimaryKey
    @ColumnInfo(name = "sourceURL")
    var sourceURL: String = "",
    @ColumnInfo(name = "sourceOfNews")
    var sourceOfNews: String = "",
    @ColumnInfo(name = "publishedTime")
    var publishedTime: String = "",
):BookMarkType()