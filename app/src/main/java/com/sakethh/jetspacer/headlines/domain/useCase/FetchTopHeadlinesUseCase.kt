package com.sakethh.jetspacer.headlines.domain.useCase

import com.sakethh.jetspacer.common.data.local.data.repository.TopHeadlineCacheImplementation
import com.sakethh.jetspacer.common.data.local.domain.model.Headline
import com.sakethh.jetspacer.common.data.local.domain.model.cache.TopHeadlinesCache
import com.sakethh.jetspacer.common.data.local.domain.repository.TopHeadlinesCacheRepository
import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.common.utils.logger
import com.sakethh.jetspacer.headlines.data.repository.TopHeadlinesDataImplementation
import com.sakethh.jetspacer.headlines.domain.model.Article
import com.sakethh.jetspacer.headlines.domain.model.NewsDTO
import com.sakethh.jetspacer.headlines.domain.model.Source
import com.sakethh.jetspacer.headlines.domain.repository.TopHeadlinesDataRepository
import com.sakethh.jetspacer.home.settings.domain.useCase.SettingsDataUseCases
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class FetchTopHeadlinesUseCase(
    private val topHeadlinesRepository: TopHeadlinesDataRepository = TopHeadlinesDataImplementation(),
    private val settingsDataUseCases: SettingsDataUseCases = SettingsDataUseCases(),
    private val topHeadlinesCacheRepository: TopHeadlinesCacheRepository = TopHeadlineCacheImplementation()
) {
    operator fun invoke(pageSize: Int, pageNo: Int): Flow<NetworkState<NewsDTO>> = flow {
        emit(NetworkState.Loading())
        if (topHeadlinesRepository.isPageCached(pageNo)) {
            logger("cache exists")
            emit(
                NetworkState.Success(
                    NewsDTO(articles = topHeadlinesRepository.getTopHeadlinesOfThisPageFromLocalDB(
                        pageNo = pageNo
                    )
                        .map {
                            Article(
                                author = it.author,
                                content = it.content,
                                description = it.description,
                                publishedAt = it.publishedAt,
                                source = Source(
                                    id = "", name = it.sourceName
                                ),
                                title = it.title,
                                url = it.url,
                                urlToImage = it.imageUrl
                            )
                        })
                )
            )
            return@flow
        }

        if (topHeadlinesCacheRepository.isEndReached().not()) {
            logger("making request as cache doesn't exist")
            val httpResponse = topHeadlinesRepository.getTopHeadLinesFromRemoteAPI(pageSize, pageNo)
            if (httpResponse.status.isSuccess().not()) {
                emit(
                    NetworkState.Failure(
                        exceptionMessage = "Network request failed.",
                        statusCode = httpResponse.status.value,
                        statusDescription = httpResponse.status.description
                    )
                )
                return@flow
            }
            try {
                val dateFormatInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
                val dateFormatOutput = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                val topHeadlines = httpResponse.body<NewsDTO>()
                val articles = topHeadlines.articles.map {
                    it.copy(
                        publishedAt = dateFormatOutput.format(dateFormatInput.parse(it.publishedAt))
                    )
                }
                if (articles.isEmpty()) {
                    if (topHeadlinesCacheRepository.isTableEmpty().not()) {
                        logger("adding a new row and marking as end reached")
                        topHeadlinesCacheRepository.addANewRow(TopHeadlinesCache(isEndReached = true))
                    } else {
                        logger("marking as end reached")
                        topHeadlinesCacheRepository.setEndReached(true)
                    }
                } else {
                    topHeadlinesRepository.addNewHeadlines(articles.map {
                        Headline(
                            author = it.author,
                            content = it.content,
                            description = it.description,
                            publishedAt = it.publishedAt,
                            sourceName = it.source.name,
                            title = it.title,
                            url = it.url,
                            imageUrl = it.urlToImage,
                            isBookmarked = false,
                            page = pageNo
                        )
                    })
                }
                emit(
                    NetworkState.Success(
                        NewsDTO(
                            articles = topHeadlinesRepository.getTopHeadlinesOfThisPageFromLocalDB(
                                pageNo
                            )
                                .map {
                                    Article(
                                        author = it.author,
                                        content = it.content,
                                        description = it.description,
                                        publishedAt = it.publishedAt,
                                        source = Source(
                                            id = "", name = it.sourceName
                                        ),
                                        title = it.title,
                                        url = it.url,
                                        urlToImage = it.imageUrl
                                    )
                                },
                            status = topHeadlines.status,
                            totalResults = topHeadlines.totalResults
                        )
                    )
                )
                logger("returned local db data")
            } catch (e: Exception) {
                emit(
                    NetworkState.Failure(
                        exceptionMessage = e.message.toString(),
                        statusCode = httpResponse.status.value,
                        statusDescription = httpResponse.status.description
                    )
                )
            }
        } else {
            emit(NetworkState.Success(NewsDTO(emptyList())))
            logger("end has been reached making no request")
        }
    }
}