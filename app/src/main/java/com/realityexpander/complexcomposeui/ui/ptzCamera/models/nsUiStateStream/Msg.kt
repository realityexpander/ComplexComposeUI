package com.realityexpander.complexcomposeui.ui.ptzCamera.models.nsUiStateStream

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Msg(
    @SerializedName("time_date")
    val timeDate: String,
    @SerializedName("zoom_percentage")
    val zoomPercentage: String,
    @SerializedName("ui_title")
    val uiTitle: String,
    @SerializedName("pan_angle")
    val panAngle: String,
    @SerializedName("tilt_angle")
    val tiltAngle: String,
    @SerializedName("video_recording")
    val videoRecording: Boolean,
    @SerializedName("tracking_mode")
    val trackingMode: String,
    @SerializedName("ui_mode")
    val uiMode: UiMode = UiMode("", ""),
    @SerializedName("available_ui_modes")
    val availableUiModes: List<UiMode> = emptyList(),
    val zoom: Zoom = Zoom("", "", ""), // used in UI?
    @SerializedName("active_tool_modes")
    val activeToolModes: List<ActiveToolMode> = emptyList(),
    @SerializedName("temp_bar")
    val tempBar: TempBar = TempBar("", ""),
    @SerializedName("temp_spots")
    val tempSpots: List<TempSpot> = emptyList(),
    @SerializedName("temp_zones")
    val tempZones: List<TempZone> = emptyList(),
    @SerializedName("top_left_gauge_bar")
    val topLeftGaugeBar: List<GaugeBarItem> = emptyList(),
    @SerializedName("top_right_gauge_bar")
    val topRightGaugeBar: List<GaugeBarItem> = emptyList(),
)
