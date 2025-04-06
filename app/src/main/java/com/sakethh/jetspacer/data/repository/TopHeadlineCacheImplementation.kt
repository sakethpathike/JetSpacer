package com.sakethh.jetspacer.data.repository

import com.sakethh.jetspacer.JetSpacerApplication
import com.sakethh.jetspacer.domain.model.cache.TopHeadlinesCache
import com.sakethh.jetspacer.domain.repository.TopHeadlinesCacheRepository

class TopHeadlineCacheImplementation : TopHeadlinesCacheRepository {
    override suspend fun isEndReached(): Boolean {
        return try {
            JetSpacerApplication.getLocalDb()?.topHeadlineCacheDao?.isEndReached() ?: false
        } catch (_: Exception) {
            false
        }
    }

    override suspend fun setEndReached(value: Boolean) {
        JetSpacerApplication.getLocalDb()?.topHeadlineCacheDao?.setEndReached(value)
    }

    override suspend fun addANewRow(topHeadlinesCache: TopHeadlinesCache) {
        JetSpacerApplication.getLocalDb()?.topHeadlineCacheDao?.addANewRow(topHeadlinesCache)
    }

    override suspend fun isTableEmpty(): Boolean {
        return JetSpacerApplication.getLocalDb()?.topHeadlineCacheDao?.isTableEmpty() ?: true
    }
}