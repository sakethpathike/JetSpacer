package com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest

interface ManifestService {
    suspend fun getCuriosityMaxSol(): Int
}