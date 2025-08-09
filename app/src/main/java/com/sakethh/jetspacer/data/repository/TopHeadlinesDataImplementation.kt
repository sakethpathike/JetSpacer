package com.sakethh.jetspacer.data.repository

import com.sakethh.jetspacer.HyleApplication
import com.sakethh.jetspacer.common.Network
import com.sakethh.jetspacer.data.dao.headline.TopHeadlinesDao
import com.sakethh.jetspacer.domain.Response
import com.sakethh.jetspacer.domain.model.Headline
import com.sakethh.jetspacer.domain.model.article.NewsDTO
import com.sakethh.jetspacer.domain.repository.TopHeadlinesDataRepository
import com.sakethh.jetspacer.ui.GlobalSettings
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow

class TopHeadlinesDataImplementation(
    private val httpClient: HttpClient = Network.ktorClient,
    private val topHeadlinesDao: TopHeadlinesDao = HyleApplication.getLocalDb().topHeadlinesDao
) : TopHeadlinesDataRepository {

    override suspend fun getTopHeadLinesFromRemoteAPI(pageSize: Int, page: Int):  Flow<Response<NewsDTO>> {
        return extractBodyFlow {
            httpClient.get("https://newsapi.org/v2/top-headlines?q=space&category=science&language=en&sortBy=popularity&pageSize=$pageSize&page=$page&apiKey=${GlobalSettings.newsApiAPIKey.value}")
        }
    }

    override suspend fun addNewHeadlines(headlines: List<Headline>) {
        topHeadlinesDao.addNewHeadlines(headlines)
    }

    override suspend fun addANewHeadline(id: Long) {
        topHeadlinesDao.addANewHeadline(id)
    }

    override suspend fun deleteAHeadline(id: Long) {
        topHeadlinesDao.deleteAHeadline(id)
    }
}