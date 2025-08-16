package com.realityexpander.complexcomposeui.ui.ptzCamera.components

import android.annotation.SuppressLint
import android.graphics.Matrix
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import com.realityexpander.complexcomposeui.R

@SuppressLint("UnusedBoxWithConstraintsScope") // for spurious warning in this version of compose
@Preview(
    widthDp = 200,
    heightDp = 200,
    showBackground = true,
    backgroundColor = 0x000000,
    name = "Directional Button"
)
@Composable
fun DirectionalButton(
    modifier: Modifier = Modifier,
    angleDegree: Float = -90f,
    onClick: () -> Unit = {},
    outlineColor: Color = colorResource(R.color.primaryColor),
    buttonSize: Float = 80f,
    roundingButtonSize : Float = 15f,
) {
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .wrapContentSize(),
    ) {
        Box(
            modifier = modifier
                .drawWithCache {
                    val roundedPolygon = RoundedPolygon(
                        numVertices = 3,
                        radius = buttonSize,
                        centerX = size.width / 2f,
                        centerY = size.height / 2f,
                        rounding = CornerRounding(
                            roundingButtonSize,
                            smoothing = 0.1f
                        )
                    )

                    // Squish vertically the roundedPolygonPath to make it smaller
                    val squishFactor = 2.4f
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
                            setRotate(angleDegree, size.width / 2, size.height / 2)
                        }
                    )

                    onDrawBehind {
                        // Fill
                        drawPath(
                            squishedPath.asComposePath(),
                            color = Color.Black.copy(alpha = 0.7f),
                            style = Fill
                        )
                        // Outline
                        drawPath(
                            squishedPath.asComposePath(),
                            color = outlineColor,
                            alpha = 1f,
                            style = Stroke(
                                width = 6f,
                                pathEffect = null,
                                cap = StrokeCap.Butt,
                                join = StrokeJoin.Miter,
                                miter = 20f,
                            )

                        )

                    }
                }
                .clickable(
                    onClick = onClick,
                )
                .size(100.dp),
        )
    }


}



