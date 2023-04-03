package com.sakethh.jetspacer.localDB

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "apod_db")
data class APOD_DB_DTO(
    @ColumnInfo(name="title")
    var title: String = "",
    @ColumnInfo(name="datePublished")
    var datePublished: String = "",
    @ColumnInfo(name="description")
    var description: String = "",
    @PrimaryKey
    @ColumnInfo(name="imageURL")
    var imageURL: String = "",
    @ColumnInfo(name="hdImageURL")
    var hdImageURL: String = "",
    @ColumnInfo(name="mediaType")
    var mediaType: String = "",
    @ColumnInfo(name="isBookMarked")
    var isBookMarked: Boolean = false,
    @ColumnInfo(name="category")
    var category: String = "APOD",
    @ColumnInfo(name="addedToLocalDBOn")
    var addedToLocalDBOn: String = "",
)
@Entity(tableName = "apiKeys")
data class APIKeysDB(
    var currentNASAAPIKey: String = "",
    var currentNewsAPIKey: String = "",
    @PrimaryKey
    var id: String = "apiKey"
)
@Entity(tableName = "marsRovers_db")
data class MarsRoversDBDTO(
    @PrimaryKey
    @ColumnInfo(name="imageURL")
    var imageURL: String = "",
    @ColumnInfo(name="capturedBy")
    var capturedBy: String = "",
    @ColumnInfo(name="sol")
    var sol: String = "",
    @ColumnInfo(name="earthDate")
    var earthDate: String = "",
    @ColumnInfo(name="roverName")
    var roverName: String = "",
    @ColumnInfo(name="roverStatus")
    var roverStatus: String = "",
    @ColumnInfo(name="launchingDate")
    var launchingDate: String = "",
    @ColumnInfo(name="landingDate")
    var landingDate: String = "",
    @ColumnInfo(name="isBookMarked")
    var isBookMarked: Boolean = false,
    @ColumnInfo(name="category")
    var category: String = "Rover",
    @ColumnInfo(name="addedToLocalDBOn")
    var addedToLocalDBOn: String = "",
)

@Entity(tableName = "bookMarkScreen_GridNames")
data class BookMarkScreenGridNames(
    @PrimaryKey
    @ColumnInfo(name="name")
    var name: String = "",
    @ColumnInfo(name="imgUrlForGrid")
    var imgUrlForGrid: String? = "",
)

data class MarsRoversDB(
    val imageURL: MutableState<String> = mutableStateOf(""),
    val capturedBy: MutableState<String> = mutableStateOf(""),
    val sol: MutableState<String> = mutableStateOf(""),
    val earthDate: MutableState<String> = mutableStateOf(""),
    val roverName: MutableState<String> = mutableStateOf(""),
    val roverStatus: MutableState<String> = mutableStateOf(""),
    val launchingDate: MutableState<String> = mutableStateOf(""),
    val landingDate: MutableState<String> = mutableStateOf(""),
    val category: MutableState<String> = mutableStateOf(""),
    val addedToLocalDBOn: MutableState<String> = mutableStateOf(""),
)

@Entity(tableName = "newsDB")
data class NewsDB(
    @ColumnInfo(name="title")
    var title: String = "",
    @PrimaryKey
    @ColumnInfo(name="imageURL")
    var imageURL: String = "",
    @ColumnInfo(name="sourceURL")
    var sourceURL: String = "",
    @ColumnInfo(name="sourceOfNews")
    var sourceOfNews: String = "",
    @ColumnInfo(name="publishedTime")
    var publishedTime: String = "",
)