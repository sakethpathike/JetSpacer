package com.sakethh.jetspacer.localDB

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DBService {
    @Query("SELECT * FROM apod_db ORDER BY imageURL ASC")
    fun getBookMarkedAPODDBDATA(): Flow<List<APOD_DB_DTO>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNewBookMarkToAPODDB(apodDbDto: APOD_DB_DTO)

    @Query("DELETE from apod_db WHERE imageURL = :imageURL")
    suspend fun deleteFromAPODDB(imageURL: String)

    @Query("SELECT * FROM marsRovers_db ORDER BY imageURL ASC")
    fun getBookMarkedRoverDBDATA(): Flow<List<MarsRoversDBDTO>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNewBookMarkToRoverDB(marsRoverDbDto: MarsRoversDBDTO)

    @Query("DELETE from marsRovers_db WHERE imageURL = :imageURL")
    suspend fun deleteFromRoverDB(imageURL: String)

    @Query("SELECT EXISTS(SELECT * FROM apod_db WHERE imageURL = :imageURL)")
    suspend fun doesThisExistsInAPODDB(imageURL: String):Boolean
    @Query("SELECT EXISTS(SELECT * FROM marsRovers_db WHERE imageURL = :imageURL)")
    suspend fun doesThisExistsInRoversDB(imageURL: String):Boolean
}