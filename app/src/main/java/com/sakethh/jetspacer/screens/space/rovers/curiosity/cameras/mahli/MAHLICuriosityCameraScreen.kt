package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.mahli

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sakethh.jetspacer.screens.Status
import com.sakethh.jetspacer.screens.StatusScreen
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.CuriosityCamerasVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.ModifiedLazyVerticalGrid
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.RandomCuriosityCameraVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.SolTextField
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun MAHLICuriosityCameraScreen() {
    val curiosityCameraVM: CuriosityCamerasVM = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val solValue = rememberSaveable { mutableStateOf("0") }
    LaunchedEffect(key1 = true) {
        curiosityCameraVM.getMAHLIData(
            sol = solValue.value.toInt(),
            0
        )
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        SolTextField(solValue = solValue, onContinueClick = {
            curiosityCameraVM.isMAHLIDataLoaded.value = false
            coroutineScope.launch {
                curiosityCameraVM.getMAHLIData(solValue.value.toInt(), 0)
            }
        })
        if (!curiosityCameraVM.isMAHLIDataLoaded.value) {
            StatusScreen(
                title = "Wait a moment!",
                description = "fetching the images from this camera that were captured on sol ${solValue.value}",
                status = Status.LOADING
            )

        } else if (curiosityCameraVM.mahliDataFromAPI.value.isEmpty()) {
            StatusScreen(
                title = "4ooooFour",
                description = "No images were captured by this camera on sol ${solValue.value}. Change the sol value; it may give results.",
                status = Status.FOURO4InMarsScreen
            )
        } else {
            ModifiedLazyVerticalGrid(listData = curiosityCameraVM.mahliDataFromAPI.value) {

            }
        }

    }
}