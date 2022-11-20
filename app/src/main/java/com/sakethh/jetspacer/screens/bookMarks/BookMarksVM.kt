package com.sakethh.jetspacer.screens.bookMarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.localDB.APOD_DB_DTO
import com.sakethh.jetspacer.localDB.DBImplementation
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookMarksVM(private val dbImplementation: DBImplementation = DBImplementation()) :
    ViewModel() {
    private val _bookMarks = MutableStateFlow<List<APOD_DB_DTO>>(emptyList())
    val bookMarks = _bookMarks.asStateFlow()
    private val coroutineExceptionalHandler =
        CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }

    init {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionalHandler) {
            dbImplementation.getBookMarkedAPODDBDATA().collect {
                _bookMarks.emit(it.list)
            }
        }
    }
}