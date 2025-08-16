package com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.toColorInt
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.CamText
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.PtzOutlinedSurface
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.RecIndicatorElement
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.PtzUiMode
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.TempBar
import com.realityexpander.complexcomposeui.ui.theme.AppDimens.fontScale
import com.realityexpander.complexcomposeui.ui.theme.LocalIsTablet
import com.realityexpander.complexcomposeui.ui.theme.LocalOnErrorMessage

@Composable
fun ZoomAndTempControls(
    uiMode: PtzUiMode,
    zoomPercentage: Float,
    tempBar: TempBar,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onZoomSliderChange: (Float) -> Unit,
    onTakePhotoClick: () -> Unit,
    onMoreSettingsClick: () -> Unit
) {
    val isTablet = LocalIsTablet.current
    val onErrorMessage: (String) -> Unit = LocalOnErrorMessage.current

    Box(
        modifier = Modifier
            .absolutePadding(10.dp, 10.dp, 10.dp, 10.dp)
    ) {
        val insetHorizontallyToCenterSliderFraction = 0.945f

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth(insetHorizontallyToCenterSliderFraction) // inset horizontally to center the slider
                .fillMaxHeight(1f)
        ) {
            val rightControlGroupSizeFactor = .8f
            val fromBottomToBottomOfSlider = 160.dp

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight(rightControlGroupSizeFactor)
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxHeight(.95f)
                ) {
                    val (zoomSliderAndThermScale, tempScaleText) = createRefs()
                    val gridLine = createGuidelineFromBottom(
                        fromBottomToBottomOfSlider
                    )

                    // Thermal Scale Reference Marks
                    if (uiMode is PtzUiMode.Thermal) {
                        Box(
                            modifier = Modifier
                                .constrainAs(tempScaleText) {
                                    top.linkTo(zoomSliderAndThermScale.top)
                                    bottom.linkTo(gridLine)
                                    height = Dimension.fillToConstraints
                                },
                        ) {
                            val refMarks: List<String> = if (isTablet)
                                tempBar.referenceMarks
                            else {
                                if(tempBar.referenceMarks.isEmpty()) listOf<String>()
                                if(tempBar.referenceMarks.size < 3) listOf<String>()

                                // On phone: Take first, middle and last marks
                                val middleMark =
                                    tempBar.referenceMarks.size / 2
                                val lastMark =
                                    tempBar.referenceMarks.size - 1

                                listOf(
                                    tempBar.referenceMarks[0],
                                    tempBar.referenceMarks[middleMark],
                                    tempBar.referenceMarks[lastMark]
                                )
                            }

                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .offset(x = (-7).dp)
                                    .fillMaxHeight()
                                    .sizeIn(maxWidth = 0.dp) // dont include this column size when laying out the the parent component
                            ) {
                                if (refMarks.isNotEmpty()) {
                                    for (referenceMark in refMarks) {
                                        CamText(
                                            referenceMark,
                                            color = Color.White,
                                            modifier = Modifier
                                                .wrapContentWidth(
                                                    Alignment.End,
                                                    unbounded = true
                                                ),
                                            textAlign = TextAlign.Right,
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Settings, Zoom Controls, Temp Gradient
                    PtzOutlinedSurface(
                        modifier = Modifier
                            .constrainAs(zoomSliderAndThermScale) {},
                        outlineColor = Color.Transparent,
                        padding = Modifier.padding(0.dp),
                        content = {
                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxHeight(1f)  // height of zoom slider
                                    .width(if (isTablet) 45.dp else 35.dp)
                            ) {
                                // Zoom Slider & Temperature Gradient
                                Column(
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .weight(
                                            1f,
                                            false
                                        ) // weight of the slider/therm-indicator
                                ) {
                                    when (uiMode) {

                                        // Draw Zoom Buttons & Maybe Slider
                                        is PtzUiMode.RGB -> {
                                            // Zoom in button (+)
                                            Text(
                                                "+",
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                fontFamily = FontFamily.SansSerif,
                                                fontSize = if (isTablet) MaterialTheme.typography.displayMedium.fontSize
                                                else MaterialTheme.typography.displayMedium.fontSize.times(
                                                    1.3f
                                                ),
                                                modifier = Modifier
                                                    .clickable(
                                                        onClick = onZoomIn,
                                                        indication = LocalIndication.current,
                                                        role = Role.Button,
                                                        interactionSource = remember { MutableInteractionSource() },
                                                        enabled = true,
                                                        onClickLabel = "Zoom in"
                                                    )
                                                    .minimumInteractiveComponentSize()
                                            )
                                            Spacer(modifier = Modifier.height(1.dp))

                                            // Zoom Slider
                                            if (isTablet) {
                                                Slider(
                                                    modifier = Modifier
                                                        .graphicsLayer {
                                                            rotationZ = 270f
                                                            transformOrigin =
                                                                TransformOrigin(0f, 0f)
                                                        }
                                                        .layout { measurable, constraints ->
                                                            val placeable =
                                                                measurable.measure(
                                                                    Constraints(
                                                                        minWidth = constraints.minHeight,
                                                                        maxWidth = constraints.maxHeight,
                                                                        minHeight = constraints.minWidth,
                                                                        maxHeight = constraints.maxWidth,
                                                                    )
                                                                )
                                                            layout(
                                                                placeable.height,
                                                                placeable.width
                                                            ) {
                                                                placeable.place(
                                                                    -placeable.width,
                                                                    0
                                                                )
                                                            }
                                                        }
                                                        .weight(2f),
                                                    value = zoomPercentage,
                                                    onValueChange = onZoomSliderChange,
                                                    valueRange = 0f..100f,
                                                    colors = SliderDefaults.colors(
                                                        thumbColor = Color.Yellow,
                                                        activeTrackColor = Color.Yellow,
                                                        inactiveTrackColor = Color.LightGray.copy(
                                                            alpha = 0.6f
                                                        ),
                                                        activeTickColor = Color.LightGray.copy(
                                                            alpha = 0.4f
                                                        ),
                                                        inactiveTickColor = Color.LightGray.copy(
                                                            alpha = 0.4f
                                                        )
                                                    )
                                                )
                                                Spacer(modifier = Modifier.height(1.dp))
                                            }

                                            // Zoom out button (-)
                                            Text(
                                                "–",
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                fontFamily = FontFamily.SansSerif,
                                                fontSize = if (isTablet) MaterialTheme.typography.displayMedium.fontSize
                                                else MaterialTheme.typography.displayMedium.fontSize.times(
                                                    1.4f
                                                ),
                                                modifier = Modifier
                                                    .clickable(
                                                        onClick = onZoomOut,
                                                        indication = LocalIndication.current,                                                        role = Role.Button,
                                                        interactionSource = remember { MutableInteractionSource() },
                                                        enabled = true,
                                                        onClickLabel = "Zoom Out"
                                                    )
                                                    .minimumInteractiveComponentSize()
                                            )
                                        }

                                        // Draw Gradient
                                        is PtzUiMode.Thermal -> {
                                            // Make rectangle for temperature color gradient
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight(1f)
                                                    .width(if (isTablet) 30.dp else 20.dp)
                                                    .padding(top = 5.dp)
                                                    .drawWithCache {
                                                        onDrawBehind {
                                                            // Draw a gradient rectangle for temperature color scale
                                                            drawRoundRect(
                                                                brush = Brush.verticalGradient(
                                                                    colors = tempBar.colorPalette.map { hexColorStr ->
                                                                        Color(("#$hexColorStr").toColorInt())
                                                                    }
                                                                ),
                                                                size = size,
                                                                cornerRadius = CornerRadius(
                                                                    10f,
                                                                    10f
                                                                ),
                                                            )
                                                        }
                                                    }
                                                    .clickable {
                                                        onErrorMessage("Temperature Gradient Clicked")
                                                    }
                                            ) { }
                                        }
                                    }
                                }

                                // Bottom controls
                                Column(
                                    modifier = Modifier
                                        .height(fromBottomToBottomOfSlider),
                                    verticalArrangement = Arrangement.SpaceAround,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    // Photo Icon
                                    IconButton(
                                        modifier = Modifier
                                            .size(35.dp),
                                        onClick = onTakePhotoClick,
                                    ) {
                                        Icon(
                                            Icons.Filled.AddAPhoto,
                                            contentDescription = "Take Photo",
                                            tint = Color.White,
                                            modifier = Modifier
                                        )
                                    }

                                    RecIndicatorElement(
                                        recDotSize = 20.dp,
                                        recTextSize = 15.sp,
                                        modifier = Modifier
                                            .rotate(-90f)
                                            .scale(fontScale * .9f)
                                    )

                                    // Settings: Three Dots Icon
                                    IconButton(
                                        onClick = onMoreSettingsClick,
                                    ) {
                                        // Settings icon from material icons
                                        Icon(
                                            Icons.Filled.MoreVert,
                                            contentDescription = "Settings",
                                            tint = Color.White,
                                            modifier = Modifier.size(45.dp)
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
