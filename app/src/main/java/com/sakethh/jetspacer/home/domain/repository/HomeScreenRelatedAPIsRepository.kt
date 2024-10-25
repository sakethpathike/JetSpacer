package com.sakethh.jetspacer.home.domain.repository

import com.sakethh.jetspacer.home.domain.model.APODDTO
import com.sakethh.jetspacer.home.domain.model.epic.all.AllEPICDTOItem
import com.sakethh.jetspacer.home.domain.model.epic.specific.EPICSpecificDTO

interface HomeScreenRelatedAPIsRepository {
    suspend fun getAPODDataFromTheAPI(): APODDTO

    suspend fun getEpicDataForASpecificDate(date: String): List<EPICSpecificDTO>

    suspend fun getAllEpicDataDates(): List<AllEPICDTOItem>
}