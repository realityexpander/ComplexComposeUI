package com.realityexpander.complexcomposeui.ui.ptzCamera

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers.BackgroundImage
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers.StatusGaugesAndControlBars
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers.ZoomAndTempControls
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.GaugeBarItem
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.PtzUiMode
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.TempBar
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.extractTempSpotItems
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.extractTempZoneItems
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.samplePtzUiStateStreamData
import com.realityexpander.complexcomposeui.ui.theme.LocalIsTablet
import com.realityexpander.complexcomposeui.ui.theme.LocalOnErrorMessage
import com.realityexpander.complexcomposeui.ui.theme.PtzCameraTheme
import com.realityexpander.complexcomposeui.ui.util.DisplayDebugInfo
import com.realityexpander.complexcomposeui.ui.util.LockScreenOrientation
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers.DirectionalPad
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers.DirectionalPadDirection
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers.TempSpotItem
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers.TempZoneItem
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers.ThermalOverlay

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
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

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
        tempBar = samplePtzUiStateStreamData().msg.tempBar,
        zoomPercentage = 65f,
        topLeftGauges = samplePtzUiStateStreamData().msg.topLeftGaugeBar,
        topRightGauges = samplePtzUiStateStreamData().msg.topRightGaugeBar,
        tempSpots = samplePtzUiStateStreamData().extractTempSpotItems(),
        tempZones = samplePtzUiStateStreamData().extractTempZoneItems(),
        isPreviewImageVisible = true,
    )
}

@Preview(
    device = "spec:width=1080px,height=2400px,dpi=480,orientation=landscape", // expanded compact
    showBackground = false,
    backgroundColor = 0xFF000000, showSystemUi = false,
)
@Composable
fun PTZ_RGB_Phone(
) {
    PtzCameraUi(
        uiMode = PtzUiMode.RGB,
        topLeftGauges = samplePtzUiStateStreamData().msg.topLeftGaugeBar,
        topRightGauges = samplePtzUiStateStreamData().msg.topRightGaugeBar,
        zoomPercentage = 65f,
        isPreviewImageVisible = true
    )
}

@Preview(
    device = "spec:parent=Nexus 10, orientation=landscape",
    showBackground = true,
    backgroundColor = 0x000000,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
fun PTZ_Thermal_Tablet() {
    PtzCameraUi(
        uiMode = PtzUiMode.Thermal,
        tempBar = samplePtzUiStateStreamData().msg.tempBar,
        zoomPercentage = 65f,
        topLeftGauges = samplePtzUiStateStreamData().msg.topLeftGaugeBar,
        topRightGauges = samplePtzUiStateStreamData().msg.topRightGaugeBar,
        isPreviewImageVisible = true,
        isTablet = true,
        tempSpots = samplePtzUiStateStreamData().extractTempSpotItems(),
        tempZones = samplePtzUiStateStreamData().extractTempZoneItems(),
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
        topLeftGauges = samplePtzUiStateStreamData().msg.topLeftGaugeBar,
        topRightGauges = samplePtzUiStateStreamData().msg.topRightGaugeBar,
        zoomPercentage = 65f,
        isPreviewImageVisible = true,
        isTablet = true
    )
}
