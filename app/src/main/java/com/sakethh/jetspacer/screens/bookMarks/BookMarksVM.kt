package com.sakethh.jetspacer.screens.bookMarks

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.sakethh.jetspacer.localDB.APOD_DB_DTO
import com.sakethh.jetspacer.localDB.DBImplementation
import com.sakethh.jetspacer.localDB.MarsRoversDBDTO
import com.sakethh.jetspacer.screens.bookMarks.screens.APODBookMarksScreen
import com.sakethh.jetspacer.screens.bookMarks.screens.MarsRoversBookMarksScreen
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookMarksVM(private val dbImplementation: DBImplementation = DBImplementation()) :
    ViewModel() {

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

    init {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler) {
            dbImplementation.getBookMarkedAPODDBDATA().collect {
                _bookMarksFromAPODDB.emit(it.list)
            }
        }
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler) {
            dbImplementation.getBookMarkedRoverDBDATA().collect {
                _bookMarksFromRoversDB.emit(it.list)
            }
        }

    }
}

data class BookMarksScreensData(
    val screenName: String,
    val screenComposable: @Composable (navController:NavController) -> Unit
)