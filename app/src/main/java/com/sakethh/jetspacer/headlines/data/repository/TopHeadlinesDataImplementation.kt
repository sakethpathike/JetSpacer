package com.sakethh.jetspacer.headlines.data.repository

import com.sakethh.jetspacer.JetSpacerApplication
import com.sakethh.jetspacer.common.data.local.domain.model.Headline
import com.sakethh.jetspacer.common.network.HTTPClient
import com.sakethh.jetspacer.headlines.domain.repository.TopHeadlinesDataRepository
import com.sakethh.jetspacer.home.settings.presentation.utils.GlobalSettings
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class TopHeadlinesDataImplementation : TopHeadlinesDataRepository {
    override suspend fun getTopHeadLinesFromRemoteAPI(pageSize: Int, page: Int): HttpResponse {
        return HTTPClient.ktorClient.get("https://newsapi.org/v2/top-headlines?q=space&category=science&language=en&sortBy=popularity&pageSize=$pageSize&page=$page&apiKey=${GlobalSettings.newsApiAPIKey.value}")
    }

    override suspend fun addNewHeadlines(headlines: List<Headline>) {
        JetSpacerApplication.getLocalDb()?.topHeadlinesDao?.addNewHeadlines(headlines)
    }

    override suspend fun getTopHeadlinesFromLocalDB(): List<Headline> {
        return JetSpacerApplication.getLocalDb()?.topHeadlinesDao?.getAllHeadlines() ?: emptyList()
    }

    override suspend fun isLocalDataEmpty(): Boolean {
        return (JetSpacerApplication.getLocalDb()?.topHeadlinesDao?.isTableEmpty() ?: 0) < 1
    }

    override suspend fun isPageCached(pageNo: Int): Boolean {
        return JetSpacerApplication.getLocalDb()?.topHeadlinesDao?.isPageCached(pageNo) ?: false
    }

    override suspend fun getTopHeadlinesOfThisPageFromLocalDB(pageNo: Int): List<Headline> {
        return JetSpacerApplication.getLocalDb()?.topHeadlinesDao?.getTopHeadlinesOfThisPage(pageNo)
            ?: emptyList()
    }
}