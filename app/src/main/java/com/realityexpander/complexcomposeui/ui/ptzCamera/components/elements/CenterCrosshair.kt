package com.realityexpander.complexcomposeui.ui.ptzCamera.components.elements

import android.graphics.Matrix
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath

@Preview(
    widthDp = 200,
    heightDp = 200,
    showBackground = true,
    backgroundColor = 0x777777,
    name = "Crosshair + Rec Element"
)
@Composable
fun CenterCrosshair(
    modifier: Modifier = Modifier,
    recTextSize: TextUnit = 20.sp,
    recDotSize: Dp = 30.dp,
    recDotColor: Color = Color.Red,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {

        // Cross-hairs
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .requiredWidth(200.dp)
                .requiredHeight(200.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Top Left
                CenterCrosshairElement(modifier = Modifier.rotate(180f))
                CenterCrosshairElement(modifier = Modifier.rotate(270f))
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CenterCrosshairElement(modifier = Modifier.rotate(90f))
                CenterCrosshairElement(modifier = Modifier)
            }
        }

        // REC Element
        RecIndicatorElement(
            recDotSize = recDotSize,
            recDotColor = recDotColor,
            onClick = onClick,
            recTextSize = recTextSize,
            shouldFlashWholeIndicator = true
        )
    }
}


////////////////////////////////////////////////////////////
///////////////////// PREVIEWS /////////////////////////////
////////////////////////////////////////////////////////////

@Preview(
    widthDp = 50,
    heightDp = 50,
    showBackground = true,
    backgroundColor = 0x777777,
    name = "Center Crosshair Element"
)
@Composable
private fun CenterCrosshairElement(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    elementSize: Dp = 100.dp
) {
    Box(
        modifier = modifier
            .drawWithCache {
                val roundedPolygon = RoundedPolygon(
                    numVertices = 4,
                    radius = elementSize.toPx() / 4f,
                    centerX = size.width / 2,
                    centerY = size.height / 2,
                    perVertexRounding = listOf(
                        CornerRounding(
                            size.minDimension / 1f,
                            smoothing = 1f
                        ),
                        CornerRounding(
                            size.minDimension / 3f,
                            smoothing = 1f
                        ),
                        CornerRounding(
                            size.minDimension / 6f,
                            smoothing = 1f
                        ),
                        CornerRounding(
                            size.minDimension / 3f,
                            smoothing = 1f
                        )
                    )
                )

                // Squish vertically the roundedPolygonPath to make it smaller
                val squishFactor = .75f
                val squishedPath = roundedPolygon.toPath()
                squishedPath.transform(
                    Matrix().apply {
                        setScale(
                            .8f,
                            squishFactor,
                            size.width / 2,
                            size.height / 2
                        )
                    }
                )
                squishedPath.transform(
                    Matrix().apply {
                        setRotate(45f, size.width / 2, size.height / 2)
                    }
                )
                squishedPath.transform(
                    Matrix().apply {
                        setSkew(
                            .3f,
                            .3f,
                            size.width / 2,
                            size.height / 2
                        )
                    }
                )

                onDrawBehind {
                    // Fill
                    drawPath(
                        squishedPath.asComposePath(),
                        color = Color.Black.copy(alpha = 0.5f),
                        style = Fill
                    )
                    // Outline
                    drawPath(
                        squishedPath.asComposePath(),
                        color = Color.LightGray,
                        alpha = .5f,
                        style = Stroke(
                            width = 3f,
                            pathEffect = null,
                            cap = StrokeCap.Butt,
                            join = StrokeJoin.Miter,
                            miter = 1f,
                        )

                    )

                }
            }
            .clickable(
                onClick = onClick,
            )
            .size(100.dp),
    ) {
    }
}



