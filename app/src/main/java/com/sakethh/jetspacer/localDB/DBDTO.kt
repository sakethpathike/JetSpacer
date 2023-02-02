package com.sakethh.jetspacer.localDB

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sakethh.jetspacer.Constants

@Entity(tableName = "apod_db")
data class APOD_DB_DTO(
    var title: String = "",
    var datePublished: String = "",
    var description: String = "",
    @PrimaryKey
    var imageURL: String = "",
    var hdImageURL: String = "",
    var mediaType: String = "",
    var isBookMarked: Boolean = false,
    var category: String = "APOD",
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
    var imageURL: String = "",
    var capturedBy: String = "",
    var sol: String = "",
    var earthDate: String = "",
    var roverName: String = "",
    var roverStatus: String = "",
    var launchingDate: String = "",
    var landingDate: String = "",
    var isBookMarked: Boolean = false,
    var category: String = "Rover",
    var addedToLocalDBOn: String = "",
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
    var title: String = "",
    var imageURL: String = "",
    @PrimaryKey
    var sourceURL: String = "",
    var sourceOfNews: String = "",
    var publishedTime: String = "",
)