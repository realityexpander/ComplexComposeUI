package com.realityexpander.ui.neuralSpotlightPTZ.components.uiLayers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.CenterCrosshair
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.DirectionalButton
import com.realityexpander.complexcomposeui.ui.theme.LocalIsTablet

sealed class DirectionalPadDirection {
    object Up : DirectionalPadDirection()
    object UpLeft : DirectionalPadDirection()
    object UpRight : DirectionalPadDirection()
    object Left : DirectionalPadDirection()
    object Right : DirectionalPadDirection()
    object DownLeft : DirectionalPadDirection()
    object DownRight : DirectionalPadDirection()
    object Down : DirectionalPadDirection()
}

@Composable
fun DirectionalPad(
    modifier: Modifier = Modifier,
    onDirectionClick: (DirectionalPadDirection) -> Unit
) {

    val isTablet = LocalIsTablet.current

    Box(
        modifier = modifier
    ) {
        val density = LocalContext.current.resources.displayMetrics.density
        val directionalButtonSize = if (isTablet)
                40f * density
            else
                20f * density
        val roundingButtonSize = if (isTablet)
                15f
            else
                9f

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            val phoneYInset = 24.dp // applied to CORNER top & bottom buttons
            val phoneXInset = 24.dp // applied to left & right buttons
            val phoneCenterYInset = 8.dp
            val tabletYInset = 15.dp // applied to CORNER top & bottom buttons

            // Top
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Top-Left Button
                DirectionalButton(
                    modifier = Modifier.then(
                        if (!isTablet) Modifier
                            .offset(x = -phoneXInset, y = -phoneYInset)
                        else Modifier
                    ),
                    angleDegree = 225f,
                    buttonSize = directionalButtonSize,
                    roundingButtonSize = roundingButtonSize,
                    onClick = { onDirectionClick(DirectionalPadDirection.UpLeft) }
                )

                // Top-Middle Button
                DirectionalButton(
                    modifier = Modifier.offset(
                        y = if (!isTablet) -phoneYInset - phoneCenterYInset else -tabletYInset
                    ),
                    angleDegree = 270f,
                    buttonSize = directionalButtonSize,
                    roundingButtonSize = roundingButtonSize,
                    onClick = { onDirectionClick(DirectionalPadDirection.Up) }
                )

                // Top-Right Button
                DirectionalButton(
                    modifier = Modifier.then(
                        if (!isTablet) Modifier
                            .offset(x = phoneXInset, y = -phoneYInset)
                        else Modifier
                    ),
                    angleDegree = 315f,
                    buttonSize = directionalButtonSize,
                    roundingButtonSize = roundingButtonSize,
                    onClick = { onDirectionClick(DirectionalPadDirection.UpRight) }
                )
            }

            // Center
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f)
            ) {
                val phoneEdgeInset = 30.dp
                val tabletEdgeInset = 15.dp

                // Center-Left Button
                DirectionalButton(
                    modifier = Modifier.then(
                        if (!isTablet)
                            Modifier
                                .offset(x = -phoneEdgeInset, y = 0.dp)
                        else Modifier
                            .offset(x = -tabletEdgeInset, y = 0.dp)
                    ),
                    angleDegree = 180f,
                    buttonSize = directionalButtonSize,
                    roundingButtonSize = roundingButtonSize,
                    onClick = { onDirectionClick(DirectionalPadDirection.Left) }
                )

                // Center-Middle Button
                CenterCrosshair(
                    modifier = Modifier
                        .scale(if (!isTablet) .7f else 1f)
                )

                // Center-Right Button
                DirectionalButton(
                    modifier = Modifier.then(
                        if (!isTablet)
                            Modifier.offset(x = phoneEdgeInset, y = 0.dp)
                        else
                            Modifier.offset(x = tabletEdgeInset, y = 0.dp)
                    ),
                    angleDegree = 0f,
                    buttonSize = directionalButtonSize,
                    roundingButtonSize = roundingButtonSize,
                    onClick = { onDirectionClick(DirectionalPadDirection.Right) }
                )
            }

            // Bottom
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Bottom-Left Button
                DirectionalButton(
                    modifier = Modifier.then(
                        if (!isTablet) Modifier
                            .offset(x = -phoneXInset, y = phoneYInset)
                        else Modifier
                    ),
                    angleDegree = 135f,
                    buttonSize = directionalButtonSize,
                    roundingButtonSize = roundingButtonSize,
                    onClick = { onDirectionClick(DirectionalPadDirection.DownLeft) }
                )

                // Bottom-Middle Button
                DirectionalButton(
                    modifier = Modifier.offset(
                        y = if (!isTablet) phoneYInset + phoneCenterYInset else tabletYInset
                    ),
                    angleDegree = 90f,
                    buttonSize = directionalButtonSize,
                    roundingButtonSize = roundingButtonSize,
                    onClick = { onDirectionClick(DirectionalPadDirection.Down) }
                )

                // Bottom-Right Button
                DirectionalButton(
                    modifier = Modifier.then(
                        if (!isTablet)
                            Modifier.offset(x = phoneXInset, y = phoneYInset)
                        else Modifier
                    ),
                    angleDegree = 45f,
                    buttonSize = directionalButtonSize,
                    roundingButtonSize = roundingButtonSize,
                    onClick = { onDirectionClick(DirectionalPadDirection.DownRight) }
                )
            }
        }
    }
}
