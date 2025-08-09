package com.sakethh.jetspacer.domain.repository

import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.Headline
import com.sakethh.jetspacer.domain.model.article.NewsDTO
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow

interface TopHeadlinesDataRepository {

    // for cached:
    suspend fun getTopHeadLinesFromRemoteAPI(pageSize: Int, page: Int): Flow<Response<NewsDTO>>

    suspend fun addNewHeadlines(headlines: List<Headline>)


    // for bookmarked:

    suspend fun addANewHeadline(id: Long)
    suspend fun deleteAHeadline(id: Long)
}