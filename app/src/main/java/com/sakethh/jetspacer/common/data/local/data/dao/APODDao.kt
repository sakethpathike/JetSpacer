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

    @Query("SELECT 1 FROM apod WHERE isCurrentAPOD = 1")
    suspend fun getCurrentAPOD(): APOD

    @Query("UPDATE apod SET isCurrentAPOD  = 0")
    suspend fun unMarkCurrentAPOD()

    @Query("DELETE FROM apod WHERE id = :id")
    suspend fun deleteAnAPOD(id: Long)

    @Query("SELECT COUNT(*) FROM apod where date = :date")
    suspend fun doesThisDateExistsInTheSavedAPODs(date: String): Int

    @Query("SELECT * FROM apod")
    fun getAllAPOData(): Flow<List<APOD>>

    @Query("SELECT * FROM headline WHERE isBookmarked = 1")
    fun getAllBookmarkedAPOD(): Flow<List<APOD>>
}