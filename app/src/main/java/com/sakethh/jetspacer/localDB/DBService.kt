package com.sakethh.jetspacer.localDB

import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow

interface DBService {
    suspend fun getBookMarkedAPODDBDATA(): Flow<ResultsChange<APOD_DB_DTO>>
    suspend fun addNewBookMarkToAPODDB(apodDbDto: APOD_DB_DTO)
    suspend fun deleteFromAPODDB(imageURL: String)
}