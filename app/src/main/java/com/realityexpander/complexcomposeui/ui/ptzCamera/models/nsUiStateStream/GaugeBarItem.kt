package com.realityexpander.complexcomposeui.ui.ptzCamera.models.nsUiStateStream

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GaugeBarItem(
    val id: String,
    @SerializedName("display_name")
    val displayName: String,
    val value: String
)
