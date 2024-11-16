package com.sakethh.jetspacer.common.data.local.data.repository

import com.sakethh.jetspacer.JetSpacerApplication
import com.sakethh.jetspacer.common.data.local.domain.model.APOD
import com.sakethh.jetspacer.common.data.local.domain.repository.LocalAPODRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class LocalAPODImplementation : LocalAPODRepository {
    override suspend fun addANewAPOD(apod: APOD) {
        JetSpacerApplication.getLocalDb()?.apodDao?.addANewAPOD(apod)
    }

    override suspend fun deleteAnAPOD(date: String) {
        JetSpacerApplication.getLocalDb()?.apodDao?.deleteAnAPOD(date)
    }

    override suspend fun doesThisDateExistsInTheSavedAPODs(date: String): Boolean {
        return JetSpacerApplication.getLocalDb()?.apodDao?.doesThisDateExistsInTheSavedAPODs(date)
            ?: false
    }

    override fun getAllBookmarkedAPOD(): Flow<List<APOD>> {
        return JetSpacerApplication.getLocalDb()?.apodDao?.getAllBookmarkedAPOD() ?: emptyFlow()
    }
}