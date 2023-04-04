package com.sakethh.jetspacer.screens.bookMarks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.BookmarkRemove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.sakethh.jetspacer.localDB.*
import com.sakethh.jetspacer.screens.bookMarks.screens.APODBookMarksScreen
import com.sakethh.jetspacer.screens.bookMarks.screens.MarsRoversBookMarksScreen
import com.sakethh.jetspacer.screens.bookMarks.screens.NewsBookmarkScreen
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@Suppress("LocalVariableName")
class BookMarksVM() : ViewModel() {

    private val coroutineExceptionalHandler =
        CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }

    init {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler) {
            dbImplementation.localDBData().getBookMarkedAPODDBDATA().collect {
                _bookMarksFromAPODDB.emit(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler) {
            dbImplementation.localDBData().getBookMarkedRoverDBDATA().collect {
                _bookMarksFromRoversDB.emit(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler) {
            dbImplementation.localDBData().getBookMarkedNewsDATA().collect {
                _bookMarksFromNewsDB.emit(it)
            }
        }
    }

    val bookMarksScreensData = listOf(
        BookMarksScreensData(
            screenName = "APOD",
            screenComposable = { APODBookMarksScreen(navController = it) }),
        BookMarksScreensData(
            screenName = "Mars Rover",
            screenComposable = { MarsRoversBookMarksScreen(navController = it) }),
        BookMarksScreensData(
            screenName = "News",
            screenComposable = { NewsBookmarkScreen(navController = it) })
    )
    var imgURL = ""
    private val _bookMarksFromAPODDB = MutableStateFlow<List<APOD_DB_DTO>>(emptyList())
    val bookMarksFromAPODDB = _bookMarksFromAPODDB.asStateFlow()

    val bookMarkIcons = mutableStateOf<ImageVector>(Icons.Outlined.BookmarkAdd)
    val bookMarkText = mutableStateOf("")

    private val _bookMarksFromRoversDB = MutableStateFlow<List<MarsRoversDBDTO>>(emptyList())
    val bookMarksFromRoversDB = _bookMarksFromRoversDB.asStateFlow()

    private val _bookMarksFromNewsDB = MutableStateFlow<List<NewsDB>>(emptyList())
    val bookMarksFromNewsDB = _bookMarksFromNewsDB.asStateFlow()

    companion object {
        lateinit var dbImplementation: DBImplementation
    }


    suspend fun getApiKeys(): List<APIKeysDB> {
        return dbImplementation.localDBData().getAPIKeys()
    }

    suspend fun deleteDataFromAPODDB(imageURL: String): Boolean {
        dbImplementation.localDBData().deleteFromAPODDB(imageURL = imageURL)
        return dbImplementation.localDBData().doesThisExistsInAPODDB(imageURL = imageURL)
    }

    suspend fun deleteDataFromMARSDB(imageURL: String): Boolean {
        dbImplementation.localDBData().deleteFromRoverDB(imageURL = imageURL)
        return dbImplementation.localDBData()
            .doesThisExistsInRoversDB(imageURL = imageURL)
    }

    suspend fun deleteDataFromNewsDB(sourceURL: String): Boolean {
        dbImplementation.localDBData().deleteFromNewsDB(sourceURL = sourceURL)
        return dbImplementation.localDBData()
            .doesThisExistsInNewsDB(sourceURL = sourceURL)
    }

    suspend fun addDataToAPODDB(apodDbDto: APOD_DB_DTO): Boolean {
        return if (dbImplementation.localDBData()
                .doesThisExistsInAPODDB(imageURL = apodDbDto.imageURL)
        ) {
            false
        } else {
            dbImplementation.localDBData()
                .addNewBookMarkToAPODDB(apodDbDto = apodDbDto)
            dbImplementation.localDBData()
                .doesThisExistsInAPODDB(imageURL = apodDbDto.imageURL)
        }
    }


    suspend fun addDataToMarsDB(marsRoversDBDTO: MarsRoversDBDTO): Boolean {
        return if (dbImplementation.localDBData()
                .doesThisExistsInRoversDB(imageURL = marsRoversDBDTO.imageURL)
        ) {
            false
        } else {
            dbImplementation.localDBData()
                .addNewBookMarkToRoverDB(marsRoverDbDto = marsRoversDBDTO)
            dbImplementation.localDBData()
                .doesThisExistsInRoversDB(imageURL = marsRoversDBDTO.imageURL)
        }
    }

    suspend fun addDataToNewsDB(newsDB: NewsDB) {
        dbImplementation.localDBData().addNewBookMarkToNewsDB(newsDB = newsDB)
        doesThisExistsInNewsDBIconTxt(newsDB.sourceURL)
    }

    fun doesThisExistsInAPODIconTxt(imageURL: String) {
        var doesDataExistsInDB = false
        viewModelScope.launch {
            doesDataExistsInDB = dbImplementation.localDBData()
                .doesThisExistsInAPODDB(imageURL = imageURL)
        }.invokeOnCompletion {
            if (!doesDataExistsInDB) {
                bookMarkText.value = "Add to bookmarks"
                bookMarkIcons.value = Icons.Outlined.BookmarkAdd
            } else {
                bookMarkText.value = "Remove from bookmarks"
                bookMarkIcons.value = Icons.Outlined.BookmarkRemove
            }
        }
    }

    fun doesThisExistsInNewsDBIconTxt(sourceURL: String) {
        var doesDataExistsInDB = false
        viewModelScope.launch {
            doesDataExistsInDB = dbImplementation.localDBData()
                .doesThisExistsInNewsDB(sourceURL = sourceURL)
        }.invokeOnCompletion {
            if (!doesDataExistsInDB) {
                bookMarkText.value = "Add to bookmarks"
                bookMarkIcons.value = Icons.Outlined.BookmarkAdd
            } else {
                bookMarkText.value = "Remove from bookmarks"
                bookMarkIcons.value = Icons.Outlined.BookmarkRemove
            }
        }
    }

    fun doesThisExistsInRoverDBIconTxt(imageURL: String) {
        var doesDataExistsInDB = false
        viewModelScope.launch {
            doesDataExistsInDB = dbImplementation.localDBData()
                .doesThisExistsInRoversDB(imageURL = imageURL)
        }.invokeOnCompletion {
            if (!doesDataExistsInDB) {
                bookMarkText.value = "Add to bookmarks"
                bookMarkIcons.value = Icons.Outlined.BookmarkAdd
            } else {
                bookMarkText.value = "Remove from bookmarks"
                bookMarkIcons.value = Icons.Outlined.BookmarkRemove
            }
        }
    }
}


data class BookMarksScreensData(
    val screenName: String,
    val screenComposable: @Composable (navController: NavController) -> Unit,
)

enum class ApiType {
    NASA, NEWS
}