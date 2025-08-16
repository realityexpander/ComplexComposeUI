package com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.TempSpotElement
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.TempZoneElement
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.extractTempSpotItems
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.extractTempZoneItems
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.samplePtzUiStateStreamData

data class TempSpotItem(
    val measurementId: String = "",
    val tempStr: String = "",
    val percentOffset: PercentOffset = PercentOffset()
)

data class TempZoneItem(
    val measurementId: String = "",
    val minTempStr: String = "",
    val maxTempStr: String = "",
    val avgTempStr: String = "",
    val percentOffset: PercentOffset = PercentOffset(),
    val percentWidth: Float = 0f,
    val percentHeight: Float = 0f
)

data class PercentOffset(
    val xOffset: Float = 0f,
    val yOffset: Float = 0f,
)

// Temp Measurement layer: TempSpots and TempZones
@Preview(showBackground = true,
    backgroundColor = 0xFF1A1A1A,
    device = "spec:width=580dp,height=500dp,dpi=220",
    showSystemUi = false
)
@Composable
fun ThermalOverlayPreview(
    tempSpots: List<TempSpotItem> = samplePtzUiStateStreamData().extractTempSpotItems(),
    tempZones: List<TempZoneItem> = samplePtzUiStateStreamData().extractTempZoneItems(),
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ThermalOverlay(
            modifier = Modifier.fillMaxSize(),
            tempSpots = tempSpots,
            tempZones = tempZones,
        )
    }
}

@Composable
fun ThermalOverlay(
    modifier: Modifier = Modifier,
    tempSpots: List<TempSpotItem> = samplePtzUiStateStreamData().extractTempSpotItems(),
    tempZones: List<TempZoneItem> = samplePtzUiStateStreamData().extractTempZoneItems(),
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp
    val density = LocalDensity.current.density
    var boxActualHeightDp by remember { mutableStateOf(screenHeightDp) }
    var boxActualWidthDp by remember { mutableStateOf(screenWidthDp) }

    Box(
        modifier = modifier
            .onSizeChanged { intSize ->
                boxActualHeightDp = with(density) { intSize.height.dp / density }
                boxActualWidthDp = with(density) { intSize.width.dp / density}
            }
    ) {
            for (tempSpot in tempSpots) {
                TempSpotElement(
                    measurementId = tempSpot.measurementId,
                    tempStr = tempSpot.tempStr,
                    modifier = Modifier
                        .size(25.dp)
                        .offset(
                            x = (tempSpot.percentOffset.xOffset * (boxActualWidthDp )
                                    - density.dp), // Centered on the spot
                            y = (tempSpot.percentOffset.yOffset * (boxActualHeightDp )
                                    - density.dp)  // Centered on the spot
                        )
                )
            }

            for (tempZone in tempZones) {
                TempZoneElement(
                    modifier = Modifier
                        .width(tempZone.percentWidth * (boxActualWidthDp))
                        .height(tempZone.percentHeight * (boxActualHeightDp ))
                        .offset(
                            x = (tempZone.percentOffset.xOffset * (boxActualWidthDp )),
                            y = tempZone.percentOffset.yOffset * (boxActualHeightDp ),
                        ),
                    rectColor = Color.White,
                    measurementId = tempZone.measurementId,
                    minTempStr = tempZone.minTempStr,
                    maxTempStr = tempZone.maxTempStr,
                    avgTempStr = tempZone.avgTempStr
                )
            }
        }
}
