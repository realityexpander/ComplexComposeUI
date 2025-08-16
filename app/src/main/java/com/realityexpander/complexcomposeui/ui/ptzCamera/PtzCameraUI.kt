package com.realityexpander.complexcomposeui.ui.ptzCamera

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers.BackgroundImage
import com.realityexpander.ui.neuralSpotlightPTZ.components.uiLayers.DirectionalPad
import com.realityexpander.ui.neuralSpotlightPTZ.components.uiLayers.DirectionalPadDirection
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers.StatusGaugesAndControlBars
import com.realityexpander.ui.neuralSpotlightPTZ.components.uiLayers.TempSpotItem
import com.realityexpander.ui.neuralSpotlightPTZ.components.uiLayers.TempZoneItem
import com.realityexpander.ui.neuralSpotlightPTZ.components.uiLayers.ThermalOverlay
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers.ZoomAndTempControls
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.nsUiStateStream.GaugeBarItem
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.nsUiStateStream.PtzUiMode
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.nsUiStateStream.TempBar
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.nsUiStateStream.extractTempSpotItems
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.nsUiStateStream.extractTempZoneItems
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.nsUiStateStream.sampleNSUiStateStreamData
import com.realityexpander.complexcomposeui.ui.theme.LocalIsTablet
import com.realityexpander.complexcomposeui.ui.theme.LocalOnErrorMessage
import com.realityexpander.complexcomposeui.ui.theme.PtzCameraTheme

@Composable
fun PtzCamera(
    modifier: Modifier = Modifier,
    viewModel: PtzCameraViewModel = viewModel(),
    onErrorMessage: ((String) -> Unit)? = null,
    isTablet: Boolean = false
) {
    val uiMode by viewModel.ptzUiMode.collectAsState()
    val zoomPercentage by viewModel.zoomPercentage.collectAsState()
    val topLeftGauges by viewModel.topLeftGauges.collectAsState()
    val topRightGauges by viewModel.topRightGauges.collectAsState()
    val tempBar by viewModel.tempBar.collectAsState()
    val tempSpots by viewModel.tempSpots.collectAsState()
    val tempZones by viewModel.tempZones.collectAsState()
    val timeDate by viewModel.timeDate.collectAsState()

    PtzCameraUi(
        modifier = modifier,
        uiMode = uiMode,
        zoomPercentage = zoomPercentage,
        topLeftGauges = topLeftGauges,
        topRightGauges = topRightGauges,
        tempBar = tempBar,
        tempSpots = tempSpots,
        tempZones = tempZones,
        timeDate = timeDate,
        onUiModeToggleClicked = viewModel::onUiModeToggleClick,
        onSettingsClick = viewModel::onSettingsClick,
        onPhotosClick = viewModel::onPhotosClick,
        onDirectionClick = viewModel::onDirectionClick,
        onZoomIn = viewModel::onZoomIn,
        onZoomOut = viewModel::onZoomOut,
        onZoomSliderChange = viewModel::onZoomSliderChange,
        onTakePhotoClick = viewModel::onTakePhotoClick,
        onMoreSettingsClick = viewModel::onMoreSettingsClick,
        onErrorMessage = onErrorMessage?.let {
                onErrorMessage
            } ?: viewModel::onErrorMessage,
        isTablet = isTablet,
    )
}

@Composable
fun PtzCameraUi(
    modifier: Modifier = Modifier,
    uiMode: PtzUiMode = PtzUiMode.RGB,
    zoomPercentage: Float = 65f,
    topLeftGauges: List<GaugeBarItem> = emptyList(),
    topRightGauges: List<GaugeBarItem> = emptyList(),
    tempBar: TempBar = TempBar(),
    tempSpots: List<TempSpotItem> = emptyList(),
    tempZones: List<TempZoneItem> = emptyList(),
    timeDate: String = "12/31/25 12:55PM",
    onUiModeToggleClicked: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onPhotosClick: () -> Unit = {},
    onDirectionClick: (DirectionalPadDirection) -> Unit = {},
    onZoomIn: () -> Unit = {},
    onZoomOut: () -> Unit = {},
    onZoomSliderChange: (Float) -> Unit = {},
    onTakePhotoClick: () -> Unit = {},
    onMoreSettingsClick: () -> Unit = {},
    onErrorMessage: (String) -> Unit = {}, // shows preview background in @Previews
    isTablet: Boolean = false,
    isPreviewImageVisible: Boolean = true,
    isDebugInfoVisible: Boolean = false,
) {

    CompositionLocalProvider(
        LocalIsTablet provides isTablet,
        LocalOnErrorMessage provides onErrorMessage
    ) {
        PtzCameraTheme {
            Box(
                modifier = modifier
            ) {

                if (isPreviewImageVisible) {
                    BackgroundImage(uiMode)
                }

                if (uiMode is PtzUiMode.Thermal) {
                    ThermalOverlay(
                        modifier = Modifier.fillMaxSize(),
                        tempSpots = tempSpots,
                        tempZones = tempZones,
                    )
                }

                DirectionalPad(
                    modifier = Modifier.fillMaxSize(),
                    onDirectionClick = onDirectionClick
                )

                StatusGaugesAndControlBars(
                    topLeftGauges = topLeftGauges,
                    topRightGauges = topRightGauges,
                    uiModeDisplayName = uiMode.displayName,
                    timeDate = timeDate,
                    onUiModeToggleClick = onUiModeToggleClicked,
                    onSettingsClick = onSettingsClick,
                    onPhotosClick = onPhotosClick
                )

                ZoomAndTempControls(
                    uiMode,
                    zoomPercentage,
                    tempBar,
                    onZoomIn = onZoomIn,
                    onZoomOut = onZoomOut,
                    onZoomSliderChange = onZoomSliderChange,
                    onTakePhotoClick = onTakePhotoClick,
                    onMoreSettingsClick = onMoreSettingsClick
                )

                // LEAVE FOR REFERENCE
                if (isDebugInfoVisible) {
                    DisplayDebugInfo(isTablet)
                }
            }
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private fun DisplayDebugInfo(isTablet: Boolean) {
    Box(
        // Display screen metrics
        contentAlignment = Alignment.Center,
    ) {
        val windowSizeInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
        val screenSize = WindowSizeClass.calculateFromSize(
            DpSize(
                windowSizeInfo.windowSizeClass.minWidthDp.dp,
                windowSizeInfo.windowSizeClass.minHeightDp.dp
            )
        )
        val windowSizeClassString = windowSizeInfo.windowSizeClass.toString()

        Text(
            windowSizeClassString +
                    "\nTablet=" + isTablet +
                    "\nSize: " + screenSize.toString() +
                    "\npixelsWidth: " + LocalContext.current.resources.displayMetrics.widthPixels +
                    "\npixelsHeight: " + LocalContext.current.resources.displayMetrics.heightPixels +
                    "\npixelsDensity: " + LocalContext.current.resources.displayMetrics.density +
                    "\npixelsDpi: " + LocalContext.current.resources.displayMetrics.densityDpi ,
            color = Color.White,
            fontSize = 30.sp
        )
    }
}


////////////////////////////////////////////////////////////
///////////////////// PREVIEWS /////////////////////////////
////////////////////////////////////////////////////////////

private const val DEFAULT_SCREEN_WIDTH = 1280
private const val DEFAULT_SCREEN_HEIGHT = 800

@Preview(
//    device = "spec:width=${1080}dp,height=${480}dp,dpi=280",
//    device = "spec:width=${1280}dp,height=${800}dp,dpi=480", // expanded medium
//    device = "spec:width=1080px,height=2400px,dpi=320,orientation=landscape",
    device = "spec:width=1080px,height=2400px,dpi=480,orientation=landscape", // expanded compact
//    device = "spec:parent=10.1in WXGA (Tablet), orientation=landscape", // expanded medium
//    device = "spec:parent=desktop_small,orientation=landscape", // expanded medium
    showBackground = false,
    backgroundColor = 0xFF000000, showSystemUi = false,
)
@Composable
fun PTZ_Thermal_Phone(
) {
    PtzCameraUi(
        uiMode = PtzUiMode.Thermal,
        tempBar = sampleNSUiStateStreamData().msg.tempBar,
        zoomPercentage = 65f,
        topLeftGauges = sampleNSUiStateStreamData().msg.topLeftGaugeBar,
        topRightGauges = sampleNSUiStateStreamData().msg.topRightGaugeBar,
        tempSpots = sampleNSUiStateStreamData().extractTempSpotItems(),
        tempZones = sampleNSUiStateStreamData().extractTempZoneItems(),
        isPreviewImageVisible = true,
    )
}

@Preview(
//    device = "spec:width=${1080}dp,height=${480}dp,dpi=280",
//    device = "spec:width=${1280}dp,height=${800}dp,dpi=480", // expanded medium
//    device = "spec:width=1080px,height=2400px,dpi=320,orientation=landscape",
    device = "spec:width=1080px,height=2400px,dpi=480,orientation=landscape", // expanded compact
//    device = "spec:parent=10.1in WXGA (Tablet), orientation=landscape", // expanded medium
//    device = "spec:parent=desktop_small,orientation=landscape", // expanded medium
    showBackground = false,
    backgroundColor = 0xFF000000, showSystemUi = false,
)
@Composable
fun PTZ_RGB_Phone(
) {
    PtzCameraUi(
        uiMode = PtzUiMode.RGB,
        topLeftGauges = sampleNSUiStateStreamData().msg.topLeftGaugeBar,
        topRightGauges = sampleNSUiStateStreamData().msg.topRightGaugeBar,
        zoomPercentage = 65f,
        isPreviewImageVisible = true
    )
}

@Preview(
//    device = "spec:width=${SCREEN_WIDTH}dp,height=${SCREEN_HEIGHT}dp,dpi=480",
    device = "spec:parent=Nexus 10, orientation=landscape",
//    device = "spec:parent=desktop_small,orientation=landscape",
    showBackground = true,
    backgroundColor = 0x000000,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
fun PTZ_Thermal_Tablet() {
    PtzCameraUi(
        uiMode = PtzUiMode.Thermal,
        tempBar = sampleNSUiStateStreamData().msg.tempBar,
        zoomPercentage = 65f,
        topLeftGauges = sampleNSUiStateStreamData().msg.topLeftGaugeBar,
        topRightGauges = sampleNSUiStateStreamData().msg.topRightGaugeBar,
        isPreviewImageVisible = true,
        isTablet = true,
        tempSpots = sampleNSUiStateStreamData().extractTempSpotItems(),
        tempZones = sampleNSUiStateStreamData().extractTempZoneItems(),
    )
}

@Preview(
    device = "spec:width=${DEFAULT_SCREEN_WIDTH}dp,height=${DEFAULT_SCREEN_HEIGHT}dp,dpi=320",
    showBackground = true,
    backgroundColor = 0x000000,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
fun PTZ_RGB_Tablet() {
    PtzCameraUi(
        uiMode = PtzUiMode.RGB,
        topLeftGauges = sampleNSUiStateStreamData().msg.topLeftGaugeBar,
        topRightGauges = sampleNSUiStateStreamData().msg.topRightGaugeBar,
        zoomPercentage = 65f,
        isPreviewImageVisible = true,
        isTablet = true
    )
}
