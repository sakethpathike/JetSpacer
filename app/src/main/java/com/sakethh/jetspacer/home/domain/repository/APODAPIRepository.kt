package com.sakethh.jetspacer.home.domain.repository

import com.sakethh.jetspacer.home.domain.model.APODDTO

interface APODAPIRepository {
    suspend fun getAPODDataFromTheAPI(): APODDTO
}