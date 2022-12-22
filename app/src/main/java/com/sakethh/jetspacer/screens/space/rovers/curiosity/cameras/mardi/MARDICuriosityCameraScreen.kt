package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.mardi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.CuriosityCamerasVM
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.ModifiedLazyVerticalGrid
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.RandomCuriosityCameraVM
import com.sakethh.jetspacer.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun MARDICuriosityCameraScreen() {
    val curiosityCameraVM: CuriosityCamerasVM = viewModel()
    val randomCuriosityCameraVM: RandomCuriosityCameraVM = viewModel()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = coroutineScope) {
        coroutineScope.launch {
            randomCuriosityCameraVM.enteredSol.value?.let {
                curiosityCameraVM.getMARDIData(
                    page = 0,
                    sol = it.toInt()
                )
            }
        }
    }
    ModifiedLazyVerticalGrid(listData = curiosityCameraVM.mardiDataFromAPI.value) {

    }
}