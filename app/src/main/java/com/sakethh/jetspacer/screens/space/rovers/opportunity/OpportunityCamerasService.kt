package com.sakethh.jetspacer.screens.space.rovers.opportunity

import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo
import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.RandomCameraDTO


interface OpportunityCamerasService {
    suspend fun getRandomCamerasData(sol: Int,page:Int):RandomCameraDTO
    suspend fun getFHAZData(sol: Int,page:Int):List<Photo>
    suspend fun getRHAZData(sol: Int,page:Int):List<Photo>
    suspend fun getNAVCAMData(sol: Int,page:Int):List<Photo>
    suspend fun getPANCAMData(sol: Int,page:Int):List<Photo>
    suspend fun getMINITESData(sol: Int,page:Int):List<Photo>
}