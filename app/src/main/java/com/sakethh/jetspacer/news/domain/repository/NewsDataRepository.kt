package com.sakethh.jetspacer.news.domain.repository

import com.sakethh.jetspacer.news.domain.model.NewsDTO


interface NewsDataRepository {
    suspend fun getTopHeadLines(pageSize: Int, page: Int): NewsDTO
}