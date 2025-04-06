package com.sakethh.jetspacer.domain.repository

import com.sakethh.jetspacer.domain.model.cache.TopHeadlinesCache

interface TopHeadlinesCacheRepository {
    suspend fun isEndReached(): Boolean
    suspend fun setEndReached(value: Boolean)
    suspend fun addANewRow(topHeadlinesCache: TopHeadlinesCache)
    suspend fun isTableEmpty(): Boolean
}