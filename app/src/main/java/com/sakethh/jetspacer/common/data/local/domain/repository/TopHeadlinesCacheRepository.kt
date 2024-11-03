package com.sakethh.jetspacer.common.data.local.domain.repository

import com.sakethh.jetspacer.common.data.local.domain.model.cache.TopHeadlinesCache

interface TopHeadlinesCacheRepository {
    suspend fun isEndReached(): Boolean
    suspend fun setEndReached(value: Boolean)
    suspend fun addANewRow(topHeadlinesCache: TopHeadlinesCache)
    suspend fun isTableEmpty(): Boolean
}