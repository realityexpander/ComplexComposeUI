package com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Zoom(
    @SerializedName("active_zoom")
    val activeZoom: String,
    val max: String,
    val min: String
)
