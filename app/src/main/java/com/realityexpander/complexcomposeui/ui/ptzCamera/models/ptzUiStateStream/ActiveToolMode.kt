package com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzUiStateStream

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ActiveToolMode(
    val id: String,
    @SerializedName("display_name")
    val displayName: String,
)
