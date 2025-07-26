package com.sakethh.jetspacer.data.repository

import com.sakethh.jetspacer.HyleApplication
import com.sakethh.jetspacer.domain.model.APOD
import com.sakethh.jetspacer.domain.repository.LocalAPODRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class LocalAPODImplementation : LocalAPODRepository {
    override suspend fun addANewAPOD(apod: APOD) {
        HyleApplication.getLocalDb()?.apodDao?.addANewAPOD(apod)
    }

    override suspend fun deleteAnAPOD(date: String) {
        HyleApplication.getLocalDb()?.apodDao?.deleteAnAPOD(date)
    }

    override suspend fun doesThisDateExistsInTheSavedAPODs(date: String): Boolean {
        return HyleApplication.getLocalDb()?.apodDao?.doesThisDateExistsInTheSavedAPODs(date)
            ?: false
    }

    override fun getAllBookmarkedAPOD(): Flow<List<APOD>> {
        return HyleApplication.getLocalDb()?.apodDao?.getAllBookmarkedAPOD() ?: emptyFlow()
    }
}