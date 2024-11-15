package com.sakethh.jetspacer.headlines.domain.useCase

import com.sakethh.jetspacer.common.data.local.data.repository.TopHeadlineCacheImplementation
import com.sakethh.jetspacer.common.data.local.domain.model.Headline
import com.sakethh.jetspacer.common.data.local.domain.model.cache.TopHeadlinesCache
import com.sakethh.jetspacer.common.data.local.domain.repository.TopHeadlinesCacheRepository
import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.common.utils.logger
import com.sakethh.jetspacer.headlines.data.repository.TopHeadlinesDataImplementation
import com.sakethh.jetspacer.headlines.domain.model.NewsDTO
import com.sakethh.jetspacer.headlines.domain.repository.TopHeadlinesDataRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class FetchRemoteTopHeadlinesUseCase(
    private val topHeadlinesRepository: TopHeadlinesDataRepository = TopHeadlinesDataImplementation(),
    private val topHeadlinesCacheRepository: TopHeadlinesCacheRepository = TopHeadlineCacheImplementation()
) {
    operator fun invoke(pageSize: Int, pageNo: Int): Flow<NetworkState<List<Headline>>> = flow {
        emit(NetworkState.Loading())
        if (topHeadlinesRepository.isPageCached(pageNo)) {
            logger("cache exists")
            topHeadlinesRepository.getTopHeadlinesUntilPageFromLocalDBAsFlow(
                pageNo = pageNo
            ).collect {
                emit(NetworkState.Success(it))
            }
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
                topHeadlinesRepository.getTopHeadlinesUntilPageFromLocalDBAsFlow(
                    pageNo = pageNo
                ).collect {
                    emit(NetworkState.Success(it))
                }
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
            topHeadlinesRepository.getTopHeadlinesUntilPageFromLocalDBAsFlow(
                pageNo = pageNo
            ).collect {
                emit(
                    NetworkState.Success(
                        it + listOf(
                            Headline(
                                id = -1,
                                author = "orci",
                                content = "te",
                                description = "aliquet",
                                publishedAt = "neglegentur",
                                sourceName = "Mathew Day",
                                title = "convenire",
                                url = "https://search.yahoo.com/search?p=urna",
                                imageUrl = "https://www.google.com/#q=aenean",
                                isBookmarked = false,
                                page = 9943
                            )
                        )
                    )
                )
            }
            logger("end has been reached making no request")
        }
    }
}