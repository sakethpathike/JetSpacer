package com.sakethh.jetspacer.domain.repository

import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.headlines.HeadlinesDTO
import kotlinx.coroutines.flow.Flow

interface HeadlinesRepository {
    suspend fun getTopHeadLines(pageSize: Int, page: Int): Flow<Response<HeadlinesDTO>>
}