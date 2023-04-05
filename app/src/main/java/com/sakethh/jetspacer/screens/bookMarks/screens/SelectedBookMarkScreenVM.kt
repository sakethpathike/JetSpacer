package com.sakethh.jetspacer.screens.bookMarks.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.localDB.BookMarkScreenGridNames
import com.sakethh.jetspacer.localDB.BookMarkType
import com.sakethh.jetspacer.localDB.SavedDataType
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class SelectedBookMarkMetaData(var name: String, var savedType: SavedDataType)

class SelectedBookMarkScreenVM : ViewModel() {
    val selectedBookMarkMetaData = SelectedBookMarkMetaData("", SavedDataType.ALL)
    private val _selectedBookMarkData = MutableStateFlow(emptyList<Any>())
    val selectedBookMarkData = _selectedBookMarkData.asStateFlow()

    init {
        viewModelScope.launch {
            when (selectedBookMarkMetaData.name) {
                "APOD" -> {
                    selectedBookMarkMetaData.savedType = SavedDataType.APOD
                    BookMarksVM.dbImplementation.localDBData().getBookMarkedAPODDBDATA().collect {
                        _selectedBookMarkData.emit(it)
                    }
                }
                "News" -> {
                    selectedBookMarkMetaData.savedType = SavedDataType.NEWS
                    BookMarksVM.dbImplementation.localDBData().getBookMarkedNewsDATA().collect {
                        _selectedBookMarkData.emit(it)
                    }
                }
                "Rovers" -> {
                    selectedBookMarkMetaData.savedType = SavedDataType.ROVERS
                    BookMarksVM.dbImplementation.localDBData().getBookMarkedRoverDBDATA().collect {
                        _selectedBookMarkData.emit(it)
                    }
                }
                else -> {
                    BookMarksVM.dbImplementation.localDBData().getCustomBookMarkTopicData()
                        .collect {
                            _selectedBookMarkData.emit(it)
                        }
                }
            }
        }
    }
}