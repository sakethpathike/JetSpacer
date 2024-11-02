package com.sakethh.jetspacer.common.data.local.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class APOD(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val copyright: String,
    val date: String,
    val explanation: String,
    val hdUrl: String,
    val mediaType: String,
    val title: String,
    val url: String,
    val isBookmarked: Boolean
)
