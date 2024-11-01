package com.sakethh.jetspacer.news.domain.repository

import io.ktor.client.statement.HttpResponse


interface NewsDataRepository {
    suspend fun getTopHeadLines(pageSize: Int, page: Int): HttpResponse
}