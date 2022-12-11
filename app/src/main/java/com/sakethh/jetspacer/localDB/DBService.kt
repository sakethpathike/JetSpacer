package com.sakethh.jetspacer.localDB

import android.content.Context
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow

interface DBService {
    suspend fun getBookMarkedAPODDBDATA(): Flow<ResultsChange<APOD_DB_DTO>>
    suspend fun addNewBookMarkToAPODDB(apodDbDto: APOD_DB_DTO)
    suspend fun deleteFromAPODDB(imageURL: String)

    suspend fun getBookMarkedRoverDBDATA(): Flow<ResultsChange<MarsRoversDBDTO>>
    suspend fun addNewBookMarkToRoverDB(marsRoverDbDto: MarsRoversDBDTO)
    suspend fun deleteFromRoverDB(imageURL: String)
}