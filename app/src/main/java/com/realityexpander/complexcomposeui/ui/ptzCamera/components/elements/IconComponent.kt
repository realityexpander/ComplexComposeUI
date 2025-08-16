package com.realityexpander.complexcomposeui.ui.ptzCamera.components.elements

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.realityexpander.complexcomposeui.ui.theme.AppDimens.iconScale
import com.realityexpander.complexcomposeui.ui.theme.PtzCameraTheme

@Composable
fun IconComponent(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit = { /* Default no-op */ }
) {
    Icon(
        icon,
        contentDescription = contentDescription,
        tint = Color.White,
        modifier = Modifier
            .wrapContentSize(unbounded = true)
            .size(
                40.dp
                    .times(iconScale)
            )
            .clickable(
                onClick = onClick,
                indication = LocalIndication.current,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
            )

    )
}


@Preview
@Composable
fun IconComponentPreview() {
    PtzCameraTheme {
        IconComponent(
            icon = Icons.Filled.Add,
            contentDescription = "Add"
        )
    }
}
