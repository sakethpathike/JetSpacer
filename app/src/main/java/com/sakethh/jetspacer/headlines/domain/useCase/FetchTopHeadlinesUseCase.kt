package com.sakethh.jetspacer.headlines.domain.useCase

import com.sakethh.jetspacer.common.network.NetworkState
import com.sakethh.jetspacer.headlines.data.repository.NewsDataImplementation
import com.sakethh.jetspacer.headlines.domain.model.NewsDTO
import com.sakethh.jetspacer.headlines.domain.repository.NewsDataRepository
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class FetchTopHeadlinesUseCase(
    private val newsDataRepository: NewsDataRepository = NewsDataImplementation()
) {
    operator fun invoke(pageSize: Int, page: Int): Flow<NetworkState<NewsDTO>> = flow {
        emit(NetworkState.Loading())
        val httpResponse = newsDataRepository.getTopHeadLines(pageSize, page)
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