package com.realityexpander.complexcomposeui.ui.ptzCamera.components.elements

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TempSpotElement(
    modifier: Modifier = Modifier,
    measurementId: String = "2",
    numberColor: Color = Color.White,
    numberFontSize: TextUnit = 18.sp,
    tempStr: String = "100F",
    temperatureFontSize: TextUnit = 20.sp,
    temperatureOffset: Offset = Offset(35f, 20f),
    innerCircleColor: Color = Color(0xFF8B4513),
    outerCircleColor: Color = Color.White,
    outerCircleStrokeWidth: Float = 10f,
    crosshairColor: Color = Color.White,
    crosshairLengthRatio: Float = 0.7f // Length of crosshair lines relative to outer circle radius)
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .wrapContentSize()
            .offset((-temperatureFontSize.value / 2).dp, (-temperatureFontSize.value / 2).dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Calc center, inner circle, outer circle
            val outerRadius = size.minDimension / 2f
            val center = Offset(size.width / 2f, size.height / 2f)
            val innerRadius =
                outerRadius * 0.4f // Adjust as needed to match the image proportion
            // Calc crosshairs
            val crosshairLength = outerRadius * crosshairLengthRatio
            val strokeWidthPx =
                outerCircleStrokeWidth / 1.5f // Thinner lines for crosshairs

            fun drawCrosshairElement() {
                // Draw outer circle stroke
                drawCircle(
                    color = outerCircleColor,
                    radius = outerRadius,
                    style = Stroke(width = outerCircleStrokeWidth)
                )
                // Top crosshair
                drawLine(
                    color = crosshairColor,
                    start = Offset(center.x, center.y - outerRadius - crosshairLength),
                    end = Offset(
                        center.x,
                        center.y - outerRadius + 2 * strokeWidthPx
                    ), // Extend slightly into the circle
                    strokeWidth = strokeWidthPx,
                    cap = StrokeCap.Square
                )
                // Bottom crosshair
                drawLine(
                    color = crosshairColor,
                    start = Offset(center.x, center.y + outerRadius + crosshairLength),
                    end = Offset(center.x, center.y + outerRadius - 2 * strokeWidthPx),
                    strokeWidth = strokeWidthPx,
                    cap = StrokeCap.Square
                )
                // Left crosshair
                drawLine(
                    color = crosshairColor,
                    start = Offset(center.x - outerRadius - crosshairLength, center.y),
                    end = Offset(center.x - outerRadius + 2 * strokeWidthPx, center.y),
                    strokeWidth = strokeWidthPx,
                    cap = StrokeCap.Square
                )
                // Right crosshair
                drawLine(
                    color = crosshairColor,
                    start = Offset(center.x + outerRadius + crosshairLength, center.y),
                    end = Offset(center.x + outerRadius - 2 * strokeWidthPx, center.y),
                    strokeWidth = strokeWidthPx,
                    cap = StrokeCap.Square
                )

                // Draw inner solid circle
                drawCircle(
                    color = innerCircleColor,
                    radius = innerRadius * 2.5f,
                )
            }

            val shadowOffset = Offset(0.dp.toPx(), 0.dp.toPx()) // dx, dy
            val blurRadius = 4.dp.toPx()
            drawIntoCanvas { canvas ->
                // Create a Paint object for the shadow
                val shadowPaint = Paint().asFrameworkPaint().apply {
                    this.color =
                        android.graphics.Color.TRANSPARENT // Shadow color is set by setShadowLayer
                    // Set the shadow layer
                    setShadowLayer(
                        blurRadius,
                        shadowOffset.x,
                        shadowOffset.y,
                        Color(0xFF000000).toArgb() // Use Android Color for setShadowLayer
                    )
                }.apply {
                    strokeWidth = strokeWidthPx
                }

                // Center circle shadow
                canvas.drawCircle(
                    center = center,
                    radius = innerRadius * 3f,
                    shadowPaint.asComposePaint()
                )
                // Top crosshair shadow
                canvas.drawLine(
                    p1 = Offset(center.x, center.y - outerRadius - crosshairLength),
                    p2 = Offset(center.x, center.y - outerRadius + 2 * strokeWidthPx),
                    paint = shadowPaint.asComposePaint()
                )
                // Bottom crosshair shadow
                canvas.drawLine(
                    p1 = Offset(center.x, center.y + outerRadius + crosshairLength),
                    p2 = Offset(center.x, center.y + outerRadius - 2 * strokeWidthPx),
                    paint = shadowPaint.asComposePaint()
                )
                // Left crosshair shadow
                canvas.drawLine(
                    p1 = Offset(center.x - outerRadius - crosshairLength, center.y),
                    p2 = Offset(center.x - outerRadius + 2 * strokeWidthPx, center.y),
                    paint = shadowPaint.asComposePaint()
                )
                // Right crosshair shadow
                canvas.drawLine(
                    p1 = Offset(center.x + outerRadius + crosshairLength, center.y),
                    p2 = Offset(center.x + outerRadius - 2 * strokeWidthPx, center.y),
                    paint = shadowPaint.asComposePaint()
                )

                drawCrosshairElement()
            }
        }

        // Number (Measurement ID)
        Text(
            text = measurementId.toString(),
            color = numberColor,
            // based on font size and number length, compute the font size that gets 10% smaller for each digit
            fontSize = numberFontSize.times(1f - (measurementId.toString().length - 1) * 0.12f),
        )

        // Temperature String
        Box(
            modifier = Modifier
                .wrapContentSize(unbounded = true)
                .offset(x = temperatureOffset.x.dp, y = temperatureOffset.y.dp)
        ) {
            CamText(
                text = tempStr,
                color = Color.White,
                fontSize = temperatureFontSize,
                modifier = Modifier
            )
        }
    }
}

////////////////////////////////////////////////////////////
///////////////////// PREVIEWS /////////////////////////////
////////////////////////////////////////////////////////////

@Preview(
    device = "spec:width=330dp,height=650dp,dpi=240",
    showBackground = true
)
@Composable
fun PreviewPointTargetWithNumber() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFA500),
                        Color(0xFFFF4500),
                        Color(0xFF00008B)
                    ), // Orange, Red, Dark Blue gradient
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        // Default parameters
        TempSpotElement(
            measurementId = "2",
            modifier = Modifier.size(25.dp) // Must include size
        )

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(1f)
        ) {
            TempSpotElement(
                measurementId = "5",
                numberFontSize = 17.sp,
                tempStr = "850F",
                outerCircleStrokeWidth = 10f,
                modifier = Modifier.size(25.dp)
            )

            TempSpotElement(
                measurementId = "55",
                numberFontSize = 17.sp,
                tempStr = "850F",
                outerCircleStrokeWidth = 10f,
                modifier = Modifier.size(25.dp)
            )

            TempSpotElement(
                measurementId = "555",
                numberFontSize = 17.sp,
                tempStr = "850F",
                outerCircleStrokeWidth = 10f,
                modifier = Modifier.size(25.dp)
            )
        }

        TempSpotElement(
            measurementId = "2",
            numberFontSize = 30.sp,
            tempStr = "550F",
            temperatureFontSize = 22.sp,
            outerCircleStrokeWidth = 15f,
            temperatureOffset = Offset(50f, 40f),
            modifier = Modifier.size(50.dp)
        )

        TempSpotElement(
            measurementId = "22",
            numberFontSize = 30.sp,
            tempStr = "550F",
            temperatureFontSize = 22.sp,
            outerCircleStrokeWidth = 15f,
            temperatureOffset = Offset(50f, 40f),
            modifier = Modifier.size(50.dp)
        )

        TempSpotElement(
            measurementId = "222",
            numberFontSize = 30.sp,
            tempStr = "550F",
            temperatureFontSize = 22.sp,
            outerCircleStrokeWidth = 15f,
            temperatureOffset = Offset(50f, 40f),
            modifier = Modifier.size(50.dp)
        )
    }
}
