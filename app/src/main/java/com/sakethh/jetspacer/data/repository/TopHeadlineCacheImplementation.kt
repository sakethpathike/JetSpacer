package com.sakethh.jetspacer.data.repository

import com.sakethh.jetspacer.HyleApplication
import com.sakethh.jetspacer.domain.model.cache.TopHeadlinesCache
import com.sakethh.jetspacer.domain.repository.TopHeadlinesCacheRepository

class TopHeadlineCacheImplementation : TopHeadlinesCacheRepository {
    override suspend fun isEndReached(): Boolean {
        return try {
            HyleApplication.getLocalDb()?.topHeadlineCacheDao?.isEndReached() ?: false
        } catch (_: Exception) {
            false
        }
    }

    override suspend fun setEndReached(value: Boolean) {
        HyleApplication.getLocalDb()?.topHeadlineCacheDao?.setEndReached(value)
    }

    override suspend fun addANewRow(topHeadlinesCache: TopHeadlinesCache) {
        HyleApplication.getLocalDb()?.topHeadlineCacheDao?.addANewRow(topHeadlinesCache)
    }

    override suspend fun isTableEmpty(): Boolean {
        return HyleApplication.getLocalDb()?.topHeadlineCacheDao?.isTableEmpty() ?: true
    }
}