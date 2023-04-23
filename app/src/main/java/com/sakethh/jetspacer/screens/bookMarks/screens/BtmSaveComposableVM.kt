package com.sakethh.jetspacer.screens.bookMarks.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import com.sakethh.jetspacer.localDB.APOD_DB_DTO
import com.sakethh.jetspacer.localDB.CustomBookMarkData
import com.sakethh.jetspacer.localDB.MarsRoversDBDTO
import com.sakethh.jetspacer.localDB.SavedDataType
import com.sakethh.jetspacer.screens.bookMarks.BookMarksVM

class BtmSaveComposableVM : ViewModel() {

    suspend fun addNewDataInTheExistingTable(
        nameOfTheTable: String,
        newData: CustomBookMarkData,
    ): Boolean {
        val imageURL: String = when (newData.dataType) {
            SavedDataType.APOD -> {
                newData.data as APOD_DB_DTO
                newData.data.imageURL
            }

            SavedDataType.ROVERS -> {
                newData.data as MarsRoversDBDTO
                newData.data.imageURL
            }

            else -> {
                ""
            }
        }

        val dataClassName: String = when (newData.dataType) {
            SavedDataType.APOD -> {
                "APOD_DB_DTO"
            }

            SavedDataType.ROVERS -> {
                "MarsRoversDBDTO"
            }

            else -> {
                ""
            }
        }

        return if (true) {
            BookMarksVM.dbImplementation.localDBData()
                .addDataInAnExistingBookmarkTopic(tableName = nameOfTheTable, newData = newData)
            Log.d("data check",newData.data.toString())
            true
        } else {
            Log.d("data check","not added")
            false
        }
    }

    suspend fun doesThisTableExists(tableName:String):Boolean{
        return BookMarksVM.dbImplementation.localDBData().doesThisTableExistsInCustomBookMarkDB(name = tableName)
    }
}