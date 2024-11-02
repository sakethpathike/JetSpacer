package com.sakethh.jetspacer.headlines.domain.repository

import io.ktor.client.statement.HttpResponse


interface NewsDataRepository {
    suspend fun getTopHeadLines(pageSize: Int, page: Int): HttpResponse
}