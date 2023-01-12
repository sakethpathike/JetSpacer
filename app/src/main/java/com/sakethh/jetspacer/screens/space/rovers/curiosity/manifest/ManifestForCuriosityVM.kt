package com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ManifestForCuriosityVM(
    private val manifestForCuriosityImplementation: ManifestForCuriosityImplementation = ManifestForCuriosityImplementation(),
) :
    ViewModel() {
    val maxCuriositySol = mutableStateOf(1000)

    suspend fun maxCuriositySol() {
        maxCuriositySol.value = manifestForCuriosityImplementation.getCuriosityMaxSol()
    }
}