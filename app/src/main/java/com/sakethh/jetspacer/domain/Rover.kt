package com.sakethh.jetspacer.domain

import kotlinx.serialization.Serializable

@Serializable
data class Camera(val abbreviation: String, val name: String)

@Serializable
enum class Rover(val cameras: List<Camera>) {
    Curiosity(
        listOf(
            Camera("NAVCAM", "Navigation Camera"),
            Camera("MARDI", "Mars Descent Imager"),
            Camera("MAHLI", "Mars Hand Lens Imager"),
            Camera("CHEMCAM", "Chemistry and Camera Complex"),
            Camera("MAST", "Mast Camera"),
            Camera("RHAZ", "Rear Hazard Avoidance Camera"),
            Camera("FHAZ", "Front Hazard Avoidance Camera")
        )
    ),
    Opportunity(
        listOf(
            Camera("RHAZ", "Rear Hazard Avoidance Camera"),
            Camera("FHAZ", "Front Hazard Avoidance Camera"),
            Camera("NAVCAM", "Navigation Camera"),
            Camera("PANCAM", "Panoramic Camera"),
            Camera("MINITES", "Miniature Thermal Emission Spectrometer (Mini-TES)")
        )
    ),
    Spirit(
        listOf(
            Camera("RHAZ", "Rear Hazard Avoidance Camera"),
            Camera("FHAZ", "Front Hazard Avoidance Camera"),
            Camera("NAVCAM", "Navigation Camera"),
            Camera("PANCAM", "Panoramic Camera"),
            Camera("MINITES", "Miniature Thermal Emission Spectrometer (Mini-TES)")
        )
    )
    /*
    Perseverance was available if I remember correct, but looks like the API doesn't return camera names like it used to or something's missing in the current impl, anyway this is fine
    Perseverance(listOf())
    * */
}