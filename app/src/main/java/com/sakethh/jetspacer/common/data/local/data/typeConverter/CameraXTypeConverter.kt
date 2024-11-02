package com.sakethh.jetspacer.common.data.local.data.typeConverter

import androidx.room.TypeConverter
import com.sakethh.jetspacer.explore.marsGallery.domain.model.latest.CameraX
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CameraXTypeConverter {

    @TypeConverter
    fun listOfCameraXToString(cameraX: List<CameraX>): String {
        return Json.encodeToString(cameraX)
    }

    @TypeConverter
    fun stringToListOfCameraX(string: String): List<CameraX> {
        return Json.decodeFromString(string)
    }

}