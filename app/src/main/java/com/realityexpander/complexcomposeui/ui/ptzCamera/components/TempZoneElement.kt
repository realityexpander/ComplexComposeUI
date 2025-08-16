package com.realityexpander.complexcomposeui.ui.ptzCamera.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TempZoneElement(
    modifier: Modifier = Modifier.size(100.dp),
    measurementId: String = "2",
    minTempStr: String = "",
    maxTempStr: String,
    avgTempStr: String,
    rectColor: Color = MaterialTheme.colorScheme.onPrimary,
    temperatureFontSize: TextUnit = 20.sp,
    onClick: () -> Unit = {},
) {
    // Rectangle with handles
    fun DrawScope.drawElement(
        elementColor: Color = Color.White,
        xOffset: Float = 0f,
        yOffset: Float = 0f,
        textColor: Color = Color.Black,
    ) {
        drawRect(
            topLeft = Offset(xOffset, yOffset),
            color = elementColor,
            size = this.size,
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Draw handles in each corner
        val handleSize = 10.dp.toPx()
        val halfHandleSize = handleSize / 2f
        drawRect(
            color = elementColor,
            topLeft = Offset(
                0f - halfHandleSize + xOffset,
                0f - halfHandleSize + yOffset),
            size = Size(handleSize, handleSize)
        )
        drawRect(
            color = elementColor,
            topLeft = Offset(
                size.width - handleSize + halfHandleSize + xOffset,
                0f - halfHandleSize + yOffset),
            size = Size(handleSize, handleSize)
        )
        drawRect(
            color = elementColor,
            topLeft = Offset(
                0f - halfHandleSize + xOffset,
                size.height - handleSize + halfHandleSize + yOffset
            ),
            size = Size(handleSize, handleSize)
        )
        drawRect(
            color = elementColor,
            topLeft = Offset(
                size.width - handleSize + halfHandleSize + xOffset,
                size.height - handleSize + halfHandleSize + yOffset
            ),
            size = Size(handleSize, handleSize)
        )

        // Draw White circle with text number
        drawCircle(
            color = elementColor,
            radius = 15.dp.toPx(),
            center = Offset(size.width / 2 + xOffset
                , 0f + yOffset)
        )
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                measurementId,
                size.width / 2,
                0f + halfHandleSize + 4f, // Adjust for text baseline
                Paint().apply {
                    color = textColor.toArgb()
                    textSize = 20.dp.toPx()
                    textAlign = Paint.Align.CENTER
                }
            )
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            // Shadow
            drawElement(
                Color.Black.copy(alpha = 0.5f),
                1.5f,
                1.5f
            )
            // Element
            drawElement(
                rectColor,
            )
        }
    }

    // Temperature text
    Box(
        modifier = modifier
            .offset(y = 15.dp)
            .wrapContentSize(Alignment.TopCenter, unbounded = true),
        contentAlignment = Alignment.TopEnd,
    ) {

        val temperatureStr =
            "Min: $minTempStr " +
            "Max: $maxTempStr " +
            "Avg: $avgTempStr"

        CamText(
            temperatureStr,
            fontSize = temperatureFontSize,
        )

    }

    // Put invisible touch targets on each corner
    val touchSize = 30.dp
    val halfTouchSize = (touchSize.value / 2f).dp
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        val showTouchTargets = false // For debugging

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Top Left
                Box(
                    modifier = Modifier
                        .offset(x = -halfTouchSize, y = -halfTouchSize)
                        .size(touchSize)
                        .background(if (showTouchTargets) Color.Red else Color.Transparent)
                        .clickable(onClick = { /* Handle top left click */ })
                )

                // Top Right
                Box(
                    modifier = Modifier
                        .offset(x = halfTouchSize, y = -halfTouchSize)
                        .size(touchSize)
                        .background(if (showTouchTargets) Color.Red else Color.Transparent)
                        .clickable(onClick = { /* Handle top right click */ })
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Bottom Left
                Box(
                    modifier = Modifier
                        .offset(x = -halfTouchSize, y = halfTouchSize)
                        .size(touchSize)
                        .background(if (showTouchTargets) Color.Red else Color.Transparent)
                        .clickable(onClick = { /* Handle bottom left click */ })
                )

                // Bottom Right
                Box(
                    modifier = Modifier
                        .size(touchSize)
                        .offset(x = halfTouchSize, y = halfTouchSize)
                        .background(if (showTouchTargets) Color.Red else Color.Transparent)
                        .clickable(onClick = { /* Handle bottom right click */ })
                )
            }
        }
    }
}

@Preview(
    device = "spec:width=450dp,height=350dp,dpi=240",
    backgroundColor = 0x777777,
    showBackground = true
)
@Composable
fun TemperatureRectanglePreview(
    rectColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .fillMaxSize()
    ) {
        TempZoneElement(
            modifier = Modifier
                .size(100.dp)
                .offset(x = 360.dp, y = 50.dp),
            measurementId = "1",
            minTempStr = "52.2F",
            maxTempStr = "75.3F",
            avgTempStr = "63.0F",
            rectColor = rectColor,
        )

        TempZoneElement(
            modifier = Modifier
                .width(150.dp)
                .height(130.dp)
                .offset(x = 100.dp, y = 200.dp),
            minTempStr = "52.2F",
            maxTempStr = "75.3F",
            avgTempStr = "63.0F",
            rectColor = rectColor,
        )
    }
}
