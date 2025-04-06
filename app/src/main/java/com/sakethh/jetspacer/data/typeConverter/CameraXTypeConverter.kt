package com.sakethh.jetspacer.data.typeConverter

import androidx.room.TypeConverter
import com.sakethh.jetspacer.domain.model.rover_latest_images.CameraX
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