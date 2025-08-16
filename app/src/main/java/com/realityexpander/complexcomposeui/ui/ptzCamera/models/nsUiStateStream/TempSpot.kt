package com.realityexpander.complexcomposeui.ui.ptzCamera.models.nsUiStateStream

import androidx.annotation.Keep

@Keep
data class TempSpot(
    val id: String,
    val resolution: ScreenResolution,
    val position: Position,
    val temp: String
)
