package com.sakethh.jetspacer.screens.bookMarks.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.localDB.BookMarkScreenGridNames
import com.sakethh.jetspacer.localDB.SavedDataType
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SelectedBookMarkScreenVM : ViewModel() {
    companion object{
        var selectedBookMarkIndex=0
    }
    private val _selectedBookMarkData = MutableStateFlow(BookMarkScreenGridNames("","",
        emptyList()
    ))
    val selectedBookMarkData = _selectedBookMarkData.asStateFlow()


    init {
        viewModelScope.launch {
            BookMarksVM.dbImplementation.localDBData().getCustomBookMarkTopicData()
                .collect {
                    _selectedBookMarkData.emit(it[selectedBookMarkIndex])
                }
        }
    }
}
