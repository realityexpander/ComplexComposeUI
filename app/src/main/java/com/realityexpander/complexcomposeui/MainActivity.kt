package com.realityexpander.complexcomposeui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.realityexpander.complexcomposeui.ui.ptzCamera.PtzCamera
import com.realityexpander.complexcomposeui.ui.ptzCamera.PtzCameraUi
import com.realityexpander.complexcomposeui.ui.ptzCamera.PtzCameraViewModel
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.PtzUiMode
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.extractTempSpotItems
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.extractTempZoneItems
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.samplePtzUiStateStreamData
import com.realityexpander.complexcomposeui.ui.theme.ComplexComposeUITheme
import com.realityexpander.complexcomposeui.ui.theme.PtzCameraTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class) // for WindowSizeClass.calculateFromSize()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val isTablet = calculateUiScaleFromScreenSize()

        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()
            val viewModel = viewModel<PtzCameraViewModel>()

            ComplexComposeUITheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                ) { innerPadding ->
                    CollectSnackBarMessages(viewModel, scope, snackbarHostState)

                    PtzCamera(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        isTablet = isTablet
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private fun MainActivity.calculateUiScaleFromScreenSize(): Boolean {
    // Calculate screen size & find class
    val (widthDp, heightDp) = if (Build.VERSION.SDK_INT < 30) {
        val metrics = resources.displayMetrics
        val widthDp = metrics.widthPixels / metrics.density
        val heightDp = metrics.heightPixels / metrics.density
        Pair(widthDp, heightDp)
    } else {
        val windowMetrics = windowManager.currentWindowMetrics
        val bounds = windowMetrics.bounds
        val widthDp = bounds.width() / resources.displayMetrics.density
        val heightDp = bounds.height() / resources.displayMetrics.density
        Pair(widthDp, heightDp)
    }
    val windowSizeInfo = WindowSizeClass.calculateFromSize(DpSize(widthDp.dp, heightDp.dp))
    val isTablet = windowSizeInfo.heightSizeClass == WindowHeightSizeClass.Expanded

    return isTablet
}

@Composable
private fun CollectSnackBarMessages(
    viewModel: PtzCameraViewModel,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState

) {
    // Collect error messages from ViewModel
    val snackBarMessage by viewModel.snackBarMessages.collectAsState(null)
    LaunchedEffect(snackBarMessage) {
        snackBarMessage?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }
}


@Preview(
    device = "spec:width=1080px,height=2400px,dpi=480,orientation=landscape", // expanded compact
)
@Composable
fun PTZ_RGB_Phone() {
    PtzCameraTheme {
        PtzCameraUi(
            uiMode = PtzUiMode.RGB,
            tempBar = samplePtzUiStateStreamData().msg.tempBar,
            zoomPercentage = 65f,
            topLeftGauges = samplePtzUiStateStreamData().msg.topLeftGaugeBar,
            topRightGauges = samplePtzUiStateStreamData().msg.topRightGaugeBar,
            tempSpots = samplePtzUiStateStreamData().extractTempSpotItems(),
            tempZones = samplePtzUiStateStreamData().extractTempZoneItems(),
            isPreviewImageVisible = true,
        )
    }
}

@Preview(
    device = "spec:width=1080px,height=2400px,dpi=480,orientation=landscape", // expanded compact
)
@Composable
fun PTZ_Thermal_Phone(
) {
    PtzCameraTheme {
        PtzCameraUi(
            uiMode = PtzUiMode.Thermal,
            tempBar = samplePtzUiStateStreamData().msg.tempBar,
            zoomPercentage = 65f,
            topLeftGauges = samplePtzUiStateStreamData().msg.topLeftGaugeBar,
            topRightGauges = samplePtzUiStateStreamData().msg.topRightGaugeBar,
            tempSpots = samplePtzUiStateStreamData().extractTempSpotItems(),
            tempZones = samplePtzUiStateStreamData().extractTempZoneItems(),
            isPreviewImageVisible = true,
        )
    }
}
