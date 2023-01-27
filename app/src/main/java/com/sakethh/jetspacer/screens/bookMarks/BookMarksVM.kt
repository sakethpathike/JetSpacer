package com.sakethh.jetspacer.screens.bookMarks

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.BookmarkRemove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.localDB.*
import com.sakethh.jetspacer.screens.bookMarks.screens.APODBookMarksScreen
import com.sakethh.jetspacer.screens.bookMarks.screens.MarsRoversBookMarksScreen
import com.sakethh.jetspacer.screens.bookMarks.screens.NewsBookmarkScreen
import com.sakethh.jetspacer.screens.home.HomeScreen
import com.sakethh.jetspacer.screens.home.HomeScreenViewModel
import com.sakethh.jetspacer.screens.news.NewsScreen
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@Suppress("LocalVariableName")
class BookMarksVM(application: Application) : AndroidViewModel(application) {
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

    private val coroutineExceptionalHandler =
        CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
    val dbImplementation: DBImplementation =
        DBImplementation.getLocalDB(application.applicationContext)

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
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler){
            dbImplementation.localDBData().getBookMarkedNewsDATA().collect{
                _bookMarksFromNewsDB.emit(it)
            }
        }
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
    suspend fun deleteDataFromNewsDB(imageURL: String): Boolean {
        dbImplementation.localDBData().deleteFromNewsDB(imageURL = imageURL)
        return dbImplementation.localDBData()
            .doesThisExistsInNewsDB(imageURL = imageURL)
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
    suspend fun addDataToNewsDB(newsDB: NewsDB): Boolean {
        return if (dbImplementation.localDBData()
                .doesThisExistsInNewsDB(imageURL = newsDB.imageURL)
        ) {
            false
        } else {
            dbImplementation.localDBData()
                .addNewBookMarkToNewsDB(newsDB = newsDB)
            dbImplementation.localDBData()
                .doesThisExistsInNewsDB(imageURL = newsDB.imageURL)
        }
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
    fun doesThisExistsInNewsDBIconTxt(imageURL: String) {
        var doesDataExistsInDB = false
        viewModelScope.launch {
            doesDataExistsInDB = dbImplementation.localDBData()
                .doesThisExistsInNewsDB(imageURL = imageURL)
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
    val screenComposable: @Composable (navController: NavController) -> Unit
)