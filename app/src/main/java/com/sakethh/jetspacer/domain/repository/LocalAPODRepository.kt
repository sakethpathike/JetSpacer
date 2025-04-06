package com.sakethh.jetspacer.domain.repository

import com.sakethh.jetspacer.domain.model.APOD
import kotlinx.coroutines.flow.Flow

interface LocalAPODRepository {
    suspend fun addANewAPOD(apod: APOD)

    suspend fun deleteAnAPOD(date: String)

    suspend fun doesThisDateExistsInTheSavedAPODs(date: String): Boolean

    fun getAllBookmarkedAPOD(): Flow<List<APOD>>
}