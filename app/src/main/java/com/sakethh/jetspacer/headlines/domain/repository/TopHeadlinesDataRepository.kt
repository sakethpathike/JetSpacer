package com.sakethh.jetspacer.headlines.domain.repository

import com.sakethh.jetspacer.common.data.local.domain.model.Headline
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow


interface TopHeadlinesDataRepository {

    // for cached:
    suspend fun getTopHeadLinesFromRemoteAPI(pageSize: Int, page: Int): HttpResponse

    suspend fun getTopHeadlinesFromLocalDB(): List<Headline>

    suspend fun isLocalDataEmpty(): Boolean

    fun getTopHeadlinesUntilPageFromLocalDBAsFlow(pageNo: Int): Flow<List<Headline>>
    suspend fun isPageCached(pageNo: Int): Boolean
    suspend fun addNewHeadlines(headlines: List<Headline>)


    // for bookmarked:

    suspend fun addANewHeadline(id: Long)
    suspend fun deleteAHeadline(id: Long)
    fun getBookmarkedHeadlines(): Flow<List<Headline>>
}