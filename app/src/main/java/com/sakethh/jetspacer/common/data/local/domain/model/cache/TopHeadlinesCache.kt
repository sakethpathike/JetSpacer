package com.sakethh.jetspacer.common.data.local.domain.model.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TopHeadlinesCache(
    @PrimaryKey
    val isEndReached: Boolean
)
