package com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TempBar(
    val max: String = "",
    val min: String = "",
    @SerializedName("reference_marks")
    val referenceMarks: List<String> = emptyList(),
    @SerializedName("color_palette")
    val colorPalette: List<String> = emptyList(),  // Hex string (RRGGBB)
)
