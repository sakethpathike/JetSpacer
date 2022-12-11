package com.sakethh.jetspacer.localDB

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*


class APOD_DB_DTO : RealmObject {
    var title: String = ""
    var datePublished: String = ""
    var description: String = ""
    var imageURL: String = ""
    var mediaType: String = ""
    var isBookMarked: Boolean = false
    var category: String = "APOD"
    var addedToLocalDBOn: String = ""

    @PrimaryKey
    var id: String = ""
}

class MarsRoversDBDTO : RealmObject {
    var imageURL: String = ""
    var capturedBy: String = ""
    var sol: String = ""
    var earthDate: String = ""
    var roverName: String = ""
    var roverStatus: String = ""
    var launchingDate: String = ""
    var landingDate: String = ""
    var isBookMarked: Boolean = false
    var category: String = "Rover"
    var addedToLocalDBOn: String = ""

    @PrimaryKey
    var id: String = ""
}

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
    val addedToLocalDBOn: MutableState<String> = mutableStateOf("")
)