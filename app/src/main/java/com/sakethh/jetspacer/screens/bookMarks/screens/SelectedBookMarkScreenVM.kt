package com.sakethh.jetspacer.screens.bookMarks.screens

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.localDB.BookMarkScreenGridNames
import com.sakethh.jetspacer.localDB.SavedDataType
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import com.sakethh.jetspacer.screens.home.data.remote.apod.dto.APOD_DTO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ApodMutableStateDTO(
    val apodURL: MutableState<String>,
    val apodHDURL: MutableState<String>,
    val apodTitle: MutableState<String>,
    val apodDate: MutableState<String>,
    val apodDescription: MutableState<String>,
    val apodMediaType: MutableState<String>,
)

class SelectedBookMarkScreenVM : ViewModel() {
    companion object {
        var selectedBookMarkIndex = 0
    }

    private val _selectedBookMarkData = MutableStateFlow(
        BookMarkScreenGridNames(
            "", "",
            emptyList()
        )
    )
    val selectedBookMarkData = _selectedBookMarkData.asStateFlow()

    val apodBtmSheetData = ApodMutableStateDTO(
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf("")
    ).copy()

    val selectedDataType = mutableStateOf(SavedDataType.APOD)

    init {
        viewModelScope.launch {
            BookMarksVM.dbImplementation.localDBData().getCustomBookMarkTopicData()
                .collect {
                    _selectedBookMarkData.emit(it[selectedBookMarkIndex])
                }
        }
    }
}
