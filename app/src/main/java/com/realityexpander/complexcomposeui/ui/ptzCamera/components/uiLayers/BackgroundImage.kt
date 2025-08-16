package com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.realityexpander.complexcomposeui.R
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.nsUiStateStream.PtzUiMode

@Composable
fun BackgroundImage(ptzUiMode: PtzUiMode) {
    Box {
        Image(
            painter = painterResource(
                id = when (ptzUiMode) {
                    is PtzUiMode.RGB -> R.drawable.img_1
                    is PtzUiMode.Thermal -> R.drawable.img
                }
            ),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}
