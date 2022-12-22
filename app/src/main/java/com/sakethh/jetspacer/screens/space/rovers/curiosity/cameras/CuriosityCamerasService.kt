package com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras

import com.sakethh.jetspacer.screens.space.rovers.curiosity.cameras.random.remote.data.dto.Photo

interface CuriosityCamerasService {
    suspend fun getFHAZData(sol: Int,page:Int):List<Photo>
    suspend fun getRHAZData(sol: Int,page:Int):List<Photo>
    suspend fun getMASTData(sol: Int,page:Int):List<Photo>
    suspend fun getCHEMCAMData(sol: Int,page:Int):List<Photo>
    suspend fun getMAHLIData(sol: Int,page:Int):List<Photo>
    suspend fun getMARDIData(sol: Int,page:Int):List<Photo>
    suspend fun getNAVCAMData(sol: Int,page:Int):List<Photo>
}