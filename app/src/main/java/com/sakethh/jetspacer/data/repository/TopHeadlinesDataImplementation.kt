package com.sakethh.jetspacer.data.repository

import com.sakethh.jetspacer.JetSpacerApplication
import com.sakethh.jetspacer.common.Network
import com.sakethh.jetspacer.domain.model.Headline
import com.sakethh.jetspacer.domain.repository.TopHeadlinesDataRepository
import com.sakethh.jetspacer.ui.GlobalSettings
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class TopHeadlinesDataImplementation : TopHeadlinesDataRepository {
    override suspend fun getTopHeadLinesFromRemoteAPI(pageSize: Int, page: Int): HttpResponse {
        return Network.ktorClient.get("https://newsapi.org/v2/top-headlines?q=space&category=science&language=en&sortBy=popularity&pageSize=$pageSize&page=$page&apiKey=${GlobalSettings.newsApiAPIKey.value}")
    }

    override suspend fun addNewHeadlines(headlines: List<Headline>) {
        JetSpacerApplication.Companion.getLocalDb()?.topHeadlinesDao?.addNewHeadlines(headlines)
    }

    override suspend fun addANewHeadline(id: Long) {
        JetSpacerApplication.Companion.getLocalDb()?.topHeadlinesDao?.addANewHeadline(id)
    }

    override suspend fun deleteAHeadline(id: Long) {
        JetSpacerApplication.Companion.getLocalDb()?.topHeadlinesDao?.deleteAHeadline(id)
    }

    override fun getBookmarkedHeadlines(): Flow<List<Headline>> {
        return JetSpacerApplication.Companion.getLocalDb()?.topHeadlinesDao?.getBookmarkedHeadlines()
            ?: emptyFlow()
    }

    override suspend fun getTopHeadlinesFromLocalDB(): List<Headline> {
        return JetSpacerApplication.Companion.getLocalDb()?.topHeadlinesDao?.getAllHeadlines()
            ?: emptyList()
    }

    override suspend fun isLocalDataEmpty(): Boolean {
        return (JetSpacerApplication.Companion.getLocalDb()?.topHeadlinesDao?.isTableEmpty()
            ?: 0) < 1
    }

    override suspend fun isPageCached(pageNo: Int): Boolean {
        return JetSpacerApplication.Companion.getLocalDb()?.topHeadlinesDao?.isPageCached(pageNo)
            ?: false
    }

    override fun getTopHeadlinesUntilPageFromLocalDBAsFlow(pageNo: Int): Flow<List<Headline>> {
        return JetSpacerApplication.Companion.getLocalDb()?.topHeadlinesDao?.getTopHeadlinesUntilThisPageAsFlow(
            pageNo
        ) ?: emptyFlow()
    }

    override suspend fun clearCache() {
        JetSpacerApplication.Companion.getLocalDb()?.topHeadlinesDao?.clearCache()
    }
}