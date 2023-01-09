package com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.RandomCuriosityCameraVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.manifest.dto.RoverManifestDTO
import kotlinx.coroutines.*

class ManifestVM(
    private val manifestImplementation: ManifestImplementation = ManifestImplementation(),
) :
    ViewModel() {
    val maxCuriositySol = mutableStateOf(1000)

    suspend fun maxCuriositySol() {
        maxCuriositySol.value = manifestImplementation.getCuriosityMaxSol()
    }
}