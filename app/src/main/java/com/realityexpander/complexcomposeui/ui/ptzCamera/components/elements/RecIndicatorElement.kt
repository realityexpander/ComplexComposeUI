package com.realityexpander.complexcomposeui.ui.ptzCamera.components.elements

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecIndicatorElement(
    modifier: Modifier = Modifier,
    recDotSize: Dp = 30.dp,
    recDotColor: Color = Color.Red,
    onClick: () -> Unit = {},
    recTextSize: TextUnit = 30.sp,
    isRecording: Boolean = true,
    shouldFlashWholeIndicator: Boolean = false
) {
    // Animate the red recording circle to fade in and out once per second
    var isRecCircleVisible by remember { mutableStateOf(false) }
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isRecCircleVisible) 0f else 1f,
        animationSpec = if (isRecording)
            infiniteRepeatable(
                animation = tween(durationMillis = 500),
                repeatMode = RepeatMode.Reverse
            )
        else
            tween(durationMillis = 0),
        label = "alpha"
    )

    LaunchedEffect(isRecording) {
        when {
            isRecording -> isRecCircleVisible = !isRecCircleVisible
            else -> isRecCircleVisible = false
        }
    }

        Box(
            modifier = modifier
                .wrapContentWidth(unbounded = true)
                .then(
                    if(shouldFlashWholeIndicator) {
                        Modifier
                            .graphicsLayer { alpha = if (isRecording) animatedAlpha else 0f }
                        } else Modifier
                    )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.drawWithCache {
                    onDrawBehind {
                        // Outline style
                        val drawStyle = Stroke(
                            width = 5f,
                            pathEffect = null,
                            cap = StrokeCap.Butt,
                            join = StrokeJoin.Miter,
                            miter = 10f,
                        )

                        // Draw the outline of the REC indicator
                        drawRoundRect(
                            color = Color.White,
                            cornerRadius = CornerRadius(size.minDimension / 4f),
                            style = drawStyle
                        )
                    }
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,

                ) {
                    // Recording Circle
                    Box(
                        modifier = Modifier
                            .size(recDotSize)
                            .graphicsLayer { alpha = if (isRecording) animatedAlpha else 0f }
                            .drawWithCache {
                                onDrawBehind {
                                    drawCircle(
                                        color = recDotColor,
                                        radius = size.minDimension / 4f,
                                        center = center
                                    )
                                }
                            }
                            .clickable(onClick = onClick),
                    )

                    Text(
                        "REC  ",
                        fontSize = recTextSize,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
}

////////////////////////////////////////////////////////////
///////////////////// PREVIEWS /////////////////////////////
////////////////////////////////////////////////////////////

@Preview(
    device = "spec:width=480px,height=840px,dpi=440",
    showBackground = true,
    backgroundColor = 0x777777,
    name = "Rec Indicator Element"
)
@Composable
private fun RecIndicatorElementPreview() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        RecIndicatorElement()
        Spacer(modifier = Modifier.height(40.dp))

        RecIndicatorElement(
            modifier = Modifier.rotate(
                -90f
            ),
            isRecording = false
        )
        Spacer(modifier = Modifier.height(40.dp))

        RecIndicatorElement(
            modifier = Modifier
                .width(100.dp)
                ,
            isRecording = true,
            recTextSize = 13.sp,
            recDotSize = 12.dp,
        )
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Preview(
    device = "spec:width=480px,height=540px,dpi=240",
    showBackground = true,
    backgroundColor = 0xFF000000,
    name = "Rec Indicator Interactive Preview"
)
@Composable
private fun InteractivePreview() { // Run from gutter icon
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        var isRecording1 by remember { mutableStateOf(true) }

        Button(
            onClick = { isRecording1 = !isRecording1}
        ) {
            Text(
                text = "isRecording1: $isRecording1",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        RecIndicatorElement(isRecording = isRecording1)

        Spacer(modifier = Modifier.height(40.dp))

        var isRecording2 by remember { mutableStateOf(false) }
        Button(
            onClick = { isRecording2 = !isRecording2}
        ) {
            Text(
                text = "isRecording2: $isRecording2",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        RecIndicatorElement(isRecording = isRecording2)
    }
}
