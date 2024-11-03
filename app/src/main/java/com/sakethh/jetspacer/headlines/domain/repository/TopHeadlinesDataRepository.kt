package com.sakethh.jetspacer.headlines.domain.repository

import com.sakethh.jetspacer.common.data.local.domain.model.Headline
import io.ktor.client.statement.HttpResponse


interface TopHeadlinesDataRepository {
    suspend fun getTopHeadLinesFromRemoteAPI(pageSize: Int, page: Int): HttpResponse

    suspend fun getTopHeadlinesFromLocalDB(): List<Headline>

    suspend fun isLocalDataEmpty(): Boolean

    suspend fun getTopHeadlinesOfThisPageFromLocalDB(pageNo: Int): List<Headline>
    suspend fun isPageCached(pageNo: Int): Boolean
    suspend fun addNewHeadlines(headlines: List<Headline>)
}