package com.sakethh.jetspacer.screens.bookMarks

import android.app.Application
import android.content.Context
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sakethh.jetspacer.localDB.APOD_DB_DTO
import com.sakethh.jetspacer.localDB.DBImplementation
import com.sakethh.jetspacer.localDB.DBService
import com.sakethh.jetspacer.localDB.MarsRoversDBDTO
import com.sakethh.jetspacer.screens.bookMarks.screens.APODBookMarksScreen
import com.sakethh.jetspacer.screens.bookMarks.screens.MarsRoversBookMarksScreen
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookMarksVM(application: Application) : AndroidViewModel(application) {
    val bookMarksScreensData = listOf(
        BookMarksScreensData(
            screenName = "APOD",
            screenComposable = { APODBookMarksScreen(navController = it) }),
        BookMarksScreensData(
            screenName = "Mars Rover",
            screenComposable = { MarsRoversBookMarksScreen(navController = it) })
    )

    private val _bookMarksFromAPODDB = MutableStateFlow<List<APOD_DB_DTO>>(emptyList())
    val bookMarksFromAPODDB = _bookMarksFromAPODDB.asStateFlow()

    private val _bookMarksFromRoversDB = MutableStateFlow<List<MarsRoversDBDTO>>(emptyList())
    val bookMarksFromRoversDB = _bookMarksFromRoversDB.asStateFlow()

    private val coroutineExceptionalHandler =
        CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
    private val dbImplementation: DBImplementation =
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
    }

    suspend fun deleteDataFromAPODDB(imageURL: String): Boolean {
        dbImplementation.localDBData().deleteFromAPODDB(imageURL = imageURL)
        return dbImplementation.localDBData()
            .doesThisExistsInAPODDB(imageURL = imageURL)
    }

    suspend fun deleteDataFromMARSDB(imageURL: String): Boolean {
        dbImplementation.localDBData().deleteFromRoverDB(imageURL = imageURL)
        return dbImplementation.localDBData()
            .doesThisExistsInRoversDB(imageURL = imageURL)
    }

    suspend fun addDataToAPODDB(apodDbDto: APOD_DB_DTO): Boolean {
        return if (dbImplementation.localDBData()
                .doesThisExistsInAPODDB(imageURL = apodDbDto.imageURL)
        ) {
            false
        } else {
            dbImplementation.localDBData().addNewBookMarkToAPODDB(apodDbDto = apodDbDto)
            dbImplementation.localDBData()
                .doesThisExistsInAPODDB(imageURL = apodDbDto.imageURL)
        }
    }

    suspend fun addDataToMarsDB(marsRoversDBDTO: MarsRoversDBDTO): Boolean {
        return if (dbImplementation.localDBData()
                .doesThisExistsInRoversDB(
                    imageURL = marsRoversDBDTO.imageURL
                )
        ) {
            false
        } else {
            dbImplementation.localDBData().addNewBookMarkToRoverDB(marsRoverDbDto = marsRoversDBDTO)
            dbImplementation.localDBData().doesThisExistsInRoversDB(imageURL = marsRoversDBDTO.imageURL)
        }
    }
}

data class BookMarksScreensData(
    val screenName: String,
    val screenComposable: @Composable (navController: NavController) -> Unit
)