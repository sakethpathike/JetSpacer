package com.sakethh.jetspacer.domain.model.rover

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sakethh.jetspacer.domain.model.rover_latest_images.CameraX

@Entity
data class Rover(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cameras: List<CameraX>,
    val landingDate: String,
    val launchDate: String,
    val maxDate: String,
    val maxSol: Int,
    val name: String,
    val status: String,
    val totalImages: Int
)
