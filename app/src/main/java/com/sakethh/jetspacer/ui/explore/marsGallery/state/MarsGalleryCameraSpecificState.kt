package com.sakethh.jetspacer.ui.explore.marsGallery.state

import com.sakethh.jetspacer.domain.model.CameraAndSolSpecificDTO

data class MarsGalleryCameraSpecificState(
    val isLoading: Boolean,
    val error: Boolean,
    val data: CameraAndSolSpecificDTO,
    val reachedMaxPages: Boolean,
    val statusCode: Int,
    val statusDescription: String
)
