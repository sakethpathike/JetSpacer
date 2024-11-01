package com.sakethh.jetspacer.news.domain.useCase

import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.news.data.repository.NewsDataImplementation
import com.sakethh.jetspacer.news.domain.model.NewsDTO
import com.sakethh.jetspacer.news.domain.repository.NewsDataRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class TopHeadlinesUseCase(
    private val newsDataRepository: NewsDataRepository = NewsDataImplementation()
) {
    operator fun invoke(pageSize: Int, page: Int): Flow<NetworkState<NewsDTO>> = flow {
        val httpResponse = newsDataRepository.getTopHeadLines(pageSize, page)
        if (httpResponse.status.isSuccess().not()) {
            emit(
                NetworkState.Failure(
                    exceptionMessage = "",
                    statusCode = httpResponse.status.value,
                    statusDescription = httpResponse.status.description
                )
            )
            return@flow
        }
        try {
            val dateFormatInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
            val dateFormatOutput = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            emit(NetworkState.Loading("loading"))
            val topHeadlines = httpResponse.body<NewsDTO>()
            emit(NetworkState.Success(NewsDTO(articles = topHeadlines.articles.map {
                it.copy(
                    publishedAt = dateFormatOutput.format(dateFormatInput.parse(it.publishedAt))
                )
            }, status = topHeadlines.status, totalResults = topHeadlines.totalResults)))
        } catch (e: Exception) {
            emit(
                NetworkState.Failure(
                    exceptionMessage = e.message.toString(),
                    statusCode = httpResponse.status.value,
                    statusDescription = httpResponse.status.description
                )
            )
        }
    }
}