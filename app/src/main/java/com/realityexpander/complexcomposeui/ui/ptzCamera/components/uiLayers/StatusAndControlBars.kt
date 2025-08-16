package com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.CamText
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.GaugeRowElement
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.IconComponent
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.PtzOutlinedButton
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.PtzOutlinedSurface
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.GaugeBarItem
import com.realityexpander.complexcomposeui.ui.theme.AppDimens
import com.realityexpander.complexcomposeui.ui.theme.AppDimens.fontScale
import com.realityexpander.complexcomposeui.ui.theme.LocalIsTablet
import com.realityexpander.complexcomposeui.ui.theme.PtzCameraTheme

@Composable
fun StatusGaugesAndControlBars(
    topLeftGauges: List<GaugeBarItem>,
    topRightGauges: List<GaugeBarItem>,
    uiModeDisplayName: String = "RGB",
    timeDate: String = "12:34 12/25/25",
    onUiModeToggleClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onPhotosClick: () -> Unit = {}
) {
    val isTablet = LocalIsTablet.current

    Box(
        modifier = Modifier
            .absolutePadding(10.dp, 10.dp, 10.dp,
                if(!isTablet) 4.dp else 10.dp
            )
    ) {

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            // Top Row : Device Status (msg): Temperature, Distance, FOV, Camera Information
            TopRowGaugeBars(
                topLeftGauges,
                topRightGauges,
            )

            // Bottom Row: Button for RGB/Thermal, Settings, Photos
            BottomRowControls(
                uiModeDisplayName = uiModeDisplayName,
                timeDate = timeDate,
                onUiModeToggleClick = onUiModeToggleClick,
                onSettingsClick = onSettingsClick,
                onPhotosClick = onPhotosClick
            )
        }
    }
}

@Composable
fun ColumnScope.TopRowGaugeBars(
    topLeftGauges: List<GaugeBarItem> = emptyList(),
    topRightGauges: List<GaugeBarItem> = emptyList(),
) {
    val isTablet = LocalIsTablet.current

    @Suppress("KotlinConstantConditions") // remove spurious warning on isPhone Offset
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth(1f)
            .align(Alignment.CenterHorizontally)
            .then(
                if (!isTablet)
                    Modifier
                        .offset(
                            0.dp,
                            if (!isTablet) (-5).dp else 0.dp
                        )
                else
                    Modifier
            )
    ) {
        val gaugeClusterWidthFraction = if (isTablet) .62f else .75f

        // Top Left Gauges (Temp/Distance/FOV)
        GaugeRowElement(
            modifier = Modifier.fillMaxWidth(.5f),
            topLeftGauges,
            gaugeClusterWidthFraction = gaugeClusterWidthFraction
        )

        // Top Right Gauges (Zoom/Exposure/ExposureTime)
        GaugeRowElement(
            Modifier.fillMaxWidth(1f),
            topRightGauges,
            gaugeClusterWidthFraction = gaugeClusterWidthFraction
        )
    }
}

@Composable
fun BottomRowControls(
    uiModeDisplayName: String = "RGB",
    timeDate: String = "12:34 12/25/25",
    onUiModeToggleClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onPhotosClick: () -> Unit = {}
) {
    val isTablet = LocalIsTablet.current

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .fillMaxWidth(1f)
            .heightIn(0.dp, if (!isTablet) 30.dp else 70.dp)
    ) {
        // Bottom Left Mode: Button for RGB/Thermal
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight()
        ) {
            // Mode:RGB/Thermal
            PtzOutlinedButton(
                modifier = Modifier
                    .fillMaxHeight(),
                onClick = onUiModeToggleClick,
                content = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentWidth(unbounded = true)
                    ) {
                        CamText(
                            "MODE  ",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = MaterialTheme.typography.displaySmall.fontSize,
                            fontScale = fontScale,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth(.4f)
                        )

                        CamText(
                            uiModeDisplayName,
                            fontSize = MaterialTheme.typography.displayMedium.fontSize,
                            fontScale = fontScale,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth(1f),
                        )
                    }
                },
            )
        }

        // Bottom Right: Settings / Photos / Date&Time
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly, // SpaceAround,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(),
        ) {
            // Settings / Photos / Time & Date
            PtzOutlinedSurface(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(.62f)
                ,
                outlineColor = Color.Transparent,
                padding = Modifier
                    .padding(horizontal = 10.dp),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                    ) {
                        // Gear Icon Button
                        IconComponent(
                            icon = Icons.Default.Settings,
                            contentDescription = "App Settings",
                            onClick = onSettingsClick
                        )
                        Spacer(modifier = Modifier.size(10.dp))

                        // Photo Icon Button
                        IconComponent(
                            icon = Icons.Filled.Photo,
                            contentDescription = "Photos/Videos",
                            onClick = onPhotosClick
                        )
                        Spacer(modifier = Modifier.size(10.dp))

                        // Date/Time
                        CamText(
                            timeDate,
                            fontSize = MaterialTheme.typography.displayMedium.fontSize,
                            fontScale = AppDimens.fontScale,
                        )
                    }
                },
            )
        }
    }
}

@Preview(
    device = "spec:width=500dp,height=891dp,dpi=320", showBackground = true,
    backgroundColor = 0xFF222222
)
@Composable
fun TopRowGaugeBarsPreview_Tablet(
    topLeftGauges: List<GaugeBarItem> = listOf(GaugeBarItem("1", "Temp", "100°F")),
    topRightGauges: List<GaugeBarItem> = listOf(GaugeBarItem("1", "FOV", "120°")),
) {
    CompositionLocalProvider(LocalIsTablet provides true) {
        PtzCameraTheme {
            Column {
                TopRowGaugeBars(topLeftGauges, topRightGauges)
            }
        }
    }
}

@Preview(
    device = "spec:width=500dp,height=591dp,dpi=320", showBackground = true,
    backgroundColor = 0xFF222222
)
@Composable
fun TopRowGaugeBarsPreview_Phone(
    topLeftGauges: List<GaugeBarItem> = listOf(GaugeBarItem("1", "Temp", "100°F")),
    topRightGauges: List<GaugeBarItem> = listOf(GaugeBarItem("1", "FOV", "120°")),
) {
    CompositionLocalProvider(LocalIsTablet provides false) {
        PtzCameraTheme {
            Column {
                TopRowGaugeBars(topLeftGauges, topRightGauges)
            }
        }
    }
}

@Preview(
    device = "spec:width=800dp,height=291dp,dpi=320", showBackground = true,
    backgroundColor = 0xFF222222
)
@Composable
fun BottomRowControlsPreview_Tablet() {
    CompositionLocalProvider(LocalIsTablet provides true) {
        PtzCameraTheme {
            BottomRowControls(
                uiModeDisplayName = "RGB",
                timeDate = "12:34 12/25/25",
            )
        }
    }
}

@Preview(
    device = "spec:width=800dp,height=591dp,dpi=320", showBackground = true,
    backgroundColor = 0xFF222222
)
@Composable
fun BottomRowControlsPreview_Phone() {
    CompositionLocalProvider(LocalIsTablet provides false) {
        PtzCameraTheme {
            BottomRowControls(
                uiModeDisplayName = "RGB",
                timeDate = "12:34 12/25/25",
            )
        }
    }
}
