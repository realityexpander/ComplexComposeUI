package com.realityexpander.complexcomposeui.ui.util

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun DisplayDebugInfo(isTablet: Boolean) {
    Box(
        // Display screen metrics
        contentAlignment = Alignment.Center,
    ) {
        val windowSizeInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
        val screenSize = WindowSizeClass.calculateFromSize(
            DpSize(
                windowSizeInfo.windowSizeClass.minWidthDp.dp,
                windowSizeInfo.windowSizeClass.minHeightDp.dp
            )
        )
        val windowSizeClassString = windowSizeInfo.windowSizeClass.toString()

        Text(
            windowSizeClassString +
                    "\nTablet=" + isTablet +
                    "\nSize: " + screenSize.toString() +
                    "\npixelsWidth: " + LocalContext.current.resources.displayMetrics.widthPixels +
                    "\npixelsHeight: " + LocalContext.current.resources.displayMetrics.heightPixels +
                    "\npixelsDensity: " + LocalContext.current.resources.displayMetrics.density +
                    "\npixelsDpi: " + LocalContext.current.resources.displayMetrics.densityDpi ,
            color = Color.White,
            fontSize = 30.sp
        )
    }
}
