package com.sakethh.jetspacer.common.data.local.data.dao.headline

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sakethh.jetspacer.common.data.local.domain.model.cache.TopHeadlinesCache

@Dao
interface TopHeadlineCacheDao {
    @Query("UPDATE topheadlinescache SET isEndReached = :value")
    suspend fun setEndReached(value: Boolean)

    @Query("SELECT isEndReached FROM topheadlinescache LIMIT 1")
    suspend fun isEndReached(): Boolean

    @Insert
    suspend fun addANewRow(topHeadlinesCache: TopHeadlinesCache)

    @Query("SELECT EXISTS(SELECT isEndReached FROM topheadlinescache)")
    suspend fun isTableEmpty(): Boolean
}