package com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class UiMode(
    val id: String,
    @SerializedName("display_name")
    val displayName: String,
)
