package com.sakethh.jetspacer.common.data.local.domain.model.rover

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoverImage(
    val cameraFullName: String,
    val cameraAbbreviation: String,
    val earthDate: String,
    val imgUrl: String,
    val roverId: Long,
    val roverName: String,
    val sol: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val isBookmarked: Boolean
)