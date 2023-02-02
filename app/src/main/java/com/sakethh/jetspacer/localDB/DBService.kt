package com.sakethh.jetspacer.localDB

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
    @Query("SELECT * FROM newsDB ORDER BY imageURL ASC")
    fun getBookMarkedNewsDATA(): Flow<List<NewsDB>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNewBookMarkToRoverDB(marsRoverDbDto: MarsRoversDBDTO)

    @Query("DELETE from marsRovers_db WHERE imageURL = :imageURL")
    suspend fun deleteFromRoverDB(imageURL: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNewBookMarkToNewsDB(newsDB: NewsDB)

    @Query("DELETE from newsDB WHERE sourceURL = :sourceURL")
    suspend fun deleteFromNewsDB(sourceURL: String)

    @Query("SELECT EXISTS(SELECT * FROM apod_db WHERE imageURL = :imageURL)")
    suspend fun doesThisExistsInAPODDB(imageURL: String):Boolean
    @Query("SELECT EXISTS(SELECT * FROM marsRovers_db WHERE imageURL = :imageURL)")
    suspend fun doesThisExistsInRoversDB(imageURL: String):Boolean
    @Query("SELECT EXISTS(SELECT * FROM newsDB WHERE sourceURL = :sourceURL)")
    suspend fun doesThisExistsInNewsDB(sourceURL: String):Boolean

    @Query("DELETE FROM marsRovers_db")
    suspend fun deleteAllDataFromMarsDB()
    @Query("DELETE FROM apod_db")
    suspend fun deleteAllDataFromAPODDB()
    @Query("DELETE FROM newsDB")
    suspend fun deleteAllDataFromNewsDB()

    @Query("SELECT * FROM apiKeys")
    suspend fun getAPIKeys():List<APIKeysDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAPIKeys(apiKeysDB: APIKeysDB)

    @Query("SELECT EXISTS(SELECT * FROM apiKeys WHERE currentNASAAPIKey = :apiKey)")
    suspend fun didThisNasaApiKeyGotUpdated(apiKey: String):Boolean
    @Query("SELECT EXISTS(SELECT * FROM apiKeys WHERE currentNewsAPIKey = :apiKey)")
    suspend fun didThisNewsApiKeyGotUpdated(apiKey: String):Boolean
}