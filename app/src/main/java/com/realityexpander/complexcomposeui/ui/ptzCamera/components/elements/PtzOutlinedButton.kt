package com.realityexpander.complexcomposeui.ui.ptzCamera.components.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realityexpander.complexcomposeui.ui.theme.AppDimens.roundedCornerSize
import com.realityexpander.complexcomposeui.ui.theme.PtzCameraTheme

@Preview(
    device = "spec:width=380dp,height=640dp,dpi=240",
    showBackground = true,
    backgroundColor = 0x777777,
)
@Composable
fun NSOutlinedButtonPreview() {
    PtzCameraTheme {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            PtzOutlinedButton(content = {
                CamText(
                    "Test Data",
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            })

            PtzOutlinedButton(
                modifier = Modifier.height(100.dp),
                content = {
                    CamText(
                        "Test 12345",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 30.sp,
                    )
                }
            )

            PtzOutlinedButton(content = {
                CamText(
                    "Test String",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 40.sp,
                )
            })

            // Transparent Outline (as Button)
            PtzOutlinedButton(
                outlineColor = Color.Transparent,
                content = {
                    CamText(
                        "Test String",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 40.sp,
                    )
                },
            )
            // Transparent Outline (as Surface)
            PtzOutlinedSurface(
                outlineColor = Color.Transparent,
                content = {
                    CamText(
                        "Test String",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 40.sp,
                    )
                },
            )
        }
    }
}

@Composable
fun PtzOutlinedSurface(
    modifier: Modifier = Modifier,
    outlineColor: Color = MaterialTheme.colorScheme.primary,
    padding: Modifier = Modifier.padding(25.dp,10.dp),
    shape: RoundedCornerShape? = null,
    content: @Composable () -> Unit = { CamText() },
) {
    val localShape = shape ?:
            RoundedCornerShape(roundedCornerSize, roundedCornerSize, roundedCornerSize, roundedCornerSize)

    Surface(
        modifier = modifier,
        shape = localShape,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.surface,
        tonalElevation  = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(3.dp, outlineColor),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = padding
        ) {
            content()
        }
    }
}

@Composable
fun PtzOutlinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    outlineColor: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true,
    shape: RoundedCornerShape? = null,
    content: @Composable () -> Unit = { CamText() },
) {
    val localShape = shape ?:
        RoundedCornerShape(roundedCornerSize, roundedCornerSize, roundedCornerSize, roundedCornerSize)

    OutlinedButton(
        modifier = modifier,
        border = BorderStroke(1.dp, outlineColor),
        shape = localShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.surface
        ),
        enabled = enabled,
        onClick = onClick,
    ) {
        content()
    }
}

