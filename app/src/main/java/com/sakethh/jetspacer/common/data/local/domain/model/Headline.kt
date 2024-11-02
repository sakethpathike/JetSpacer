package com.sakethh.jetspacer.common.data.local.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Headline(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val sourceName: String,
    val title: String,
    val url: String,
    val imageUrl: String,
    val isBookmarked: Boolean
)
