package com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TempZone(
    val id: String,
    val resolution: ScreenResolution, // resolution of c3 video from vehicle
    @SerializedName("top_left_corner")
    val topLeftCorner: Position,
    @SerializedName("bottom_right_corner")
    val bottomRightCorner: Position,
    @SerializedName("min_temp")
    val minTemp: String,
    @SerializedName("max_temp")
    val maxTemp: String,
    @SerializedName("avg_temp")
    val avgTemp: String
)
