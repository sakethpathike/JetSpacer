package com.sakethh.jetspacer.explore.marsGallery.presentation.state

import com.sakethh.jetspacer.explore.marsGallery.domain.model.CameraAndSolSpecificDTO

data class MarsGalleryCameraSpecificState(
    val isLoading: Boolean,
    val error: Boolean,
    val data: CameraAndSolSpecificDTO,
    val reachedMaxPages: Boolean,
    val statusCode: Int,
    val statusDescription: String
)
