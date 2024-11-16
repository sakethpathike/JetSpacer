package com.sakethh.jetspacer.common.data.local.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sakethh.jetspacer.common.data.local.domain.model.APOD
import kotlinx.coroutines.flow.Flow

@Dao
interface APODDao {

    @Insert
    suspend fun addANewAPOD(apod: APOD)

    @Query("DELETE FROM apod WHERE date = :date")
    suspend fun deleteAnAPOD(date: String)

    @Query("SELECT COUNT(*) > 0 FROM apod WHERE date = :date")
    suspend fun doesThisDateExistsInTheSavedAPODs(date: String): Boolean

    @Query("SELECT * FROM apod WHERE isBookmarked = 1")
    fun getAllBookmarkedAPOD(): Flow<List<APOD>>
}