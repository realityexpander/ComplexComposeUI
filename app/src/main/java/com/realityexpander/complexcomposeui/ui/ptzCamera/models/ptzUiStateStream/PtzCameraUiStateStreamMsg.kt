@file:Suppress("PropertyName")

package com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzUiStateStream

import androidx.annotation.Keep
import com.realityexpander.ui.neuralSpotlightPTZ.components.uiLayers.PercentOffset
import com.realityexpander.ui.neuralSpotlightPTZ.components.uiLayers.TempSpotItem
import com.realityexpander.ui.neuralSpotlightPTZ.components.uiLayers.TempZoneItem
import com.google.gson.annotations.SerializedName
import timber.log.Timber

@Keep
data class PtzCameraUiStateStreamMsg(
    val hash: String,
    val timestamp: String,
    val direction: String,
    val channel: String,
    @SerializedName("node_name")
    val nodeName: String,
    val command: String,
    val msg: Msg = Msg(
        "",
        "",
        "",
        "",
        "",
        false,
        "",
        UiMode(
            "",
            ""
        )
    ),
)

sealed class PtzUiMode(
    val id: String = "",
    val displayName: String = "",
) {
    object RGB : PtzUiMode("rgb_camera", "RGB")
    object Thermal : PtzUiMode("thermal_camera", "THERMAL")

    companion object Companion {
        fun fromIdStr(id: String): PtzUiMode {
            // Get all the sealed subclasses of NSUiMode
            val modes = PtzUiMode::class.sealedSubclasses.mapNotNull { it.objectInstance }

            return modes.find { it.id == id } ?: run {
                Timber.e("Unknown UI Mode ID: $id. Defaulting to RGB.")
                RGB // Or throw an exception / return a specific Error state
            }
        }
    }
}

fun PtzCameraUiStateStreamMsg.extractTempZoneItems(): List<TempZoneItem> =
    msg.tempZones.map { tempZone ->
        TempZoneItem(
            measurementId = tempZone.id,
            minTempStr = tempZone.minTemp,
            maxTempStr = tempZone.maxTemp,
            avgTempStr = tempZone.avgTemp,
            percentOffset = PercentOffset(
                tempZone.topLeftCorner.x / tempZone.resolution.width.toFloat(),
                tempZone.topLeftCorner.y / tempZone.resolution.height.toFloat()
            ),
            percentWidth = (tempZone.bottomRightCorner.x - tempZone.topLeftCorner.x) / tempZone.resolution.width.toFloat(),
            percentHeight = (tempZone.bottomRightCorner.y - tempZone.topLeftCorner.y) / tempZone.resolution.height.toFloat()
        )
    }

fun PtzCameraUiStateStreamMsg.extractTempSpotItems(): List<TempSpotItem> =
    msg.tempSpots.map { tempSpot ->
        TempSpotItem(
            measurementId = tempSpot.id,
            tempStr = tempSpot.temp,
            percentOffset = PercentOffset(
                tempSpot.position.x / tempSpot.resolution.width.toFloat(),
                tempSpot.position.y / tempSpot.resolution.height.toFloat()
            )
        )
    }

fun sampleNSUiStateStreamData(): PtzCameraUiStateStreamMsg {
    return PtzCameraUiStateStreamMsg(
        hash = "test_hash",
        timestamp = "1729618583301",
        direction = "ns_to_c3",
        channel = "data_stream",
        nodeName = "neural_spotlight",
        command = "ui_state_stream",
        msg = Msg(
            timeDate = "5/1/2025 12:15PM",
            uiTitle = "TASK RECORDING MODE",
            zoomPercentage = "65",
            panAngle = "0",
            tiltAngle = "0", //
            videoRecording = true,
            trackingMode = "precise_hold",
//            uiMode = UiMode(
//                id = "rgb_camera",
//                displayName = "RGB",
//            ),
            uiMode = UiMode(
                id = "thermal_camera",
                displayName = "THERMAL",
            ),
            availableUiModes = listOf(
                UiMode(
                    id = "rgb_camera",
                    displayName = "RGB",
                ),
                UiMode(
                    id = "b&w_camera",
                    displayName = "B&W",
                ),
                UiMode(
                    id = "thermal_camera",
                    displayName = "Thermal",
                ),
            ),
            zoom = Zoom(
                activeZoom = "18x",
                max = "18x",
                min = "1x",
            ),
            activeToolModes = listOf(
                ActiveToolMode(
                    id = "point_sampling_mode",
                    displayName = "Point Sampling Mode",
                ),
            ),
            tempBar = TempBar(
                max = "1025",
                min = "54",
                referenceMarks = listOf("1025F", "850F", "575F", "260F", "54F"),
                colorPalette = listOf("FFFFFF", "FDF6DD", "F57F17", "E29b21", "64af30", "1761c7",  "1474d4", "000000")
            ),
            tempSpots = listOf(
                TempSpot(
                    id = "1",
                    resolution = ScreenResolution(1080, 720), // resolution of c3 video area
                    position = Position(240, 360), // horizontal, vertical pixel
                    temp = "52.0F"
                ),
                TempSpot(
                    id = "2",
                    resolution = ScreenResolution(1080, 720), // resolution of c3 video area
                    position = Position(540, 150), // horizontal, vertical pixel
                    temp = "78.3F"
                ),
                TempSpot(
                    id = "3",
                    resolution = ScreenResolution(1080, 720), // resolution of c3 video area
                    position = Position(540, 540), // horizontal, vertical pixel
                    temp = "98.3F"
                ),
            ),
            tempZones = listOf(
                TempZone(
                    id = "1",
                    resolution = ScreenResolution(1080, 720),
                    topLeftCorner = Position(540, 360), // CENTER OF IMAGE
//                    topLeftCorner = Position(440, 420),
                    bottomRightCorner = Position(900, 700),
                    minTemp = "52.0F",
                    maxTemp = "75.0F",
                    avgTemp = "63.5F"
                ),
                TempZone(
                    id = "2",
                    resolution = ScreenResolution(1080, 720),
                    topLeftCorner = Position(100, 100),
//                    topLeftCorner = Position(440, 420),
                    bottomRightCorner = Position(540, 360),
                    minTemp = "52.0F",
                    maxTemp = "75.0F",
                    avgTemp = "63.5F"
                ),
            ),
            topLeftGaugeBar = listOf(
                GaugeBarItem(
                    id = "center_temp",
                    displayName = "Temp",
                    value = "30c"
                ),
                GaugeBarItem(
                    id = "center_distance",
                    displayName = "Dist",
                    value = "10m"
                ),
                GaugeBarItem(
                    id = "field_of_view",
                    displayName = "FOV",
                    value = "50m"
                ),
// LEAVE FOR REFERENCE
//                GaugeBarItem(
//                    id = "center_temp2",
//                    displayName = "Temp2",
//                    value = "30c"
//                ),
//                GaugeBarItem(
//                    id = "center_distance2",
//                    display_name = "Dist2",
//                    value = "10m"
//                ),
//                GaugeBarItem(
//                    id = "field_of_view2",
//                    displayName = "FOV2",
//                    value = "50m"
//                )
            ),
            topRightGaugeBar = listOf(
                GaugeBarItem(
                    id = "rgb_zoom",
                    displayName = "Zoom",
                    value = "18x"
                ),
                GaugeBarItem(
                    id = "exposure_mode",
                    displayName = "Exp",
                    value = "Auto"
                ),
                GaugeBarItem(
                    id = "exposure_time",
                    displayName = "ExpTime",
                    value = "1/8s"
                ),
                GaugeBarItem(
                    id = "rgb_zoom",
                    displayName = "Zoom2",
                    value = "18x"
                ),
                GaugeBarItem(
                    id = "exposure_mode",
                    displayName = "Exp2",
                    value = "Auto"
                ),
                GaugeBarItem(
                    id = "exposure_time",
                    displayName = "ExpTime2",
                    value = "1/8s"
                )
            ),
        )
    )
}

