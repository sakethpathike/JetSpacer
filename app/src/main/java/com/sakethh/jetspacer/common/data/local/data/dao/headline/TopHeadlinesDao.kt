package com.sakethh.jetspacer.common.data.local.data.dao.headline

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sakethh.jetspacer.common.data.local.domain.model.Headline
import kotlinx.coroutines.flow.Flow

@Dao
interface TopHeadlinesDao {

    @Query("UPDATE headline SET isBookmarked = 1 WHERE id = :id")
    suspend fun addANewHeadline(id: Long)

    @Insert
    suspend fun addNewHeadlines(headlines: List<Headline>)

    @Query("UPDATE headline SET isBookmarked = 0 WHERE id = :id")
    suspend fun deleteAHeadline(id: Long)

    @Query("SELECT * FROM headline")
    suspend fun getAllHeadlines(): List<Headline>

    @Query("SELECT COUNT(*) FROM headline")
    fun isTableEmpty(): Int

    @Query("SELECT * FROM headline WHERE isBookmarked = 1")
    fun getBookmarkedHeadlines(): Flow<List<Headline>>

    @Query("SELECT EXISTS(SELECT 1 FROM headline WHERE page = :pageNo)")
    suspend fun isPageCached(pageNo: Int): Boolean

    @Query("SELECT * FROM headline WHERE page = :pageNo")
    suspend fun getTopHeadlinesOfThisPage(pageNo: Int): List<Headline>

    @Query("SELECT * FROM headline WHERE page = :pageNo")
    fun getTopHeadlinesOfThisPageAsFlow(pageNo: Int): Flow<List<Headline>>
}