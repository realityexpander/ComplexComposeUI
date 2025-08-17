package com.realityexpander.complexcomposeui.ui.ptzCamera.components.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.GaugeBarItem
import com.realityexpander.complexcomposeui.ui.theme.PtzCameraTheme

@Composable
fun GaugeBarElement(
    topLeftGaugeBarItem: GaugeBarItem
) {
    CamText(
        topLeftGaugeBarItem.displayName + ": ",
        color = MaterialTheme.colorScheme.onPrimary,
    )
    CamText(
        topLeftGaugeBarItem.value,
        color = MaterialTheme.colorScheme.tertiary,
    )
    Spacer(modifier = Modifier.width(8.dp))
}

@Composable
fun GaugeBarClusterElement(
    gaugeBar: List<GaugeBarItem>
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier

    ) {
        // Show first 3 items gauge_bar
        Row {
            for (firstRowGaugeBarItem in gaugeBar.take(3)) {
                GaugeBarElement(
                    firstRowGaugeBarItem,
                )
            }
        }

        // Second row next 3 items
        if (gaugeBar.size > 3) {
            Row {
                // Show next 3 items (if any)
                for (i in 3 until gaugeBar.size) {
                    GaugeBarElement(
                        gaugeBar[i],
                    )
                }
            }
        }
    }
}

@Composable
fun GaugeRowElement(
    modifier: Modifier = Modifier,
    gaugeBar: List<GaugeBarItem>,
    gaugeClusterWidthFraction: Float = 0.5f
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
    ) {
        PtzOutlinedSurface(
            modifier = Modifier.fillMaxWidth(gaugeClusterWidthFraction),
            outlineColor = Color.Transparent,
            padding = Modifier,
            content = {
                GaugeBarClusterElement(gaugeBar)
            })
    }
}

////////////////////////////////////////////////////////////
///////////////////// PREVIEWS /////////////////////////////
////////////////////////////////////////////////////////////

@Preview(showBackground = true, backgroundColor = 0xFF2F2F2F,
    device = "spec:width=800dp,height=500dp,dpi=320,orientation=portrait"
)
@Composable
fun GaugeRowPreview(){
    PtzCameraTheme {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            GaugeRowElement(
                gaugeBar = listOf(
                    GaugeBarItem("1", "RGB", "on"),
                    GaugeBarItem("2", "Thermal", "off"),
                    GaugeBarItem("3", "Distance", "100m"),
                    GaugeBarItem("4", "FOV", "60°")
                ),
                gaugeClusterWidthFraction = 0.75f
            )
        }
    }
}
