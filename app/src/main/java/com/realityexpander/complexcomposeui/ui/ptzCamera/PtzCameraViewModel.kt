package com.realityexpander.complexcomposeui.ui.ptzCamera

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.PtzUiMode
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.PtzCameraUiStateStreamMsg
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.TempBar
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.UiMode
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.extractTempSpotItems
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.extractTempZoneItems
import com.realityexpander.complexcomposeui.ui.ptzCamera.models.ptzCameraUiStateStream.samplePtzUiStateStreamData
import com.realityexpander.complexcomposeui.ui.ptzCamera.components.uiLayers.DirectionalPadDirection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.DateFormat.getDateTimeInstance
import java.util.Date

class PtzCameraViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiStateStreamMsg =
        MutableStateFlow<PtzCameraUiStateStreamMsg>(samplePtzUiStateStreamData()) // Or observe from a repository
    val uiStateStreamMsg: StateFlow<PtzCameraUiStateStreamMsg> = _uiStateStreamMsg.asStateFlow()

    val ptzUiMode: StateFlow<PtzUiMode> = _uiStateStreamMsg.map { msg ->
        PtzUiMode.fromIdStr(msg.msg.uiMode.id)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, PtzUiMode.Thermal)

    val tempSpots = _uiStateStreamMsg.map { msg ->
        msg.extractTempSpotItems()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val tempZones = _uiStateStreamMsg.map { msg ->
        msg.extractTempZoneItems()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val tempBar = _uiStateStreamMsg.map { msg ->
        msg.msg.tempBar
    }.stateIn(viewModelScope, SharingStarted.Eagerly, TempBar())

    val zoomPercentage = _uiStateStreamMsg.map { msg ->
        msg.msg.zoomPercentage.toFloatOrNull() ?: 0f
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0f)

    val topLeftGauges = _uiStateStreamMsg.map { msg ->
        msg.msg.topLeftGaugeBar
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val topRightGauges = _uiStateStreamMsg.map { msg ->
        msg.msg.topRightGaugeBar
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val timeDate = _uiStateStreamMsg.map { msg ->
        msg.msg.timeDate
    }.stateIn(viewModelScope, SharingStarted.Eagerly, "")

    private val _snackBarMessages = MutableSharedFlow<String?>()
    val snackBarMessages = _snackBarMessages.asSharedFlow()

    init {
        startTimeDateClock()
    }

    private fun startTimeDateClock() {
        // Start coroutine to update clock every second
        viewModelScope.launch {
            while (true) {
                val currentTime = getDateTimeInstance().format(Date())
                _uiStateStreamMsg.value = _uiStateStreamMsg.value.copy(
                    msg = _uiStateStreamMsg.value.msg.copy(
                        timeDate = currentTime
                    )
                )
                delay(1000)
            }
        }
    }

    fun onDirectionClick(direction: DirectionalPadDirection) {
        // Send Command to PTZ Camera
        CommsInterfaceWebRtc.instance
            .sendJson(
                "{\"direction\":\"$direction\"}",
                DataChannels.MANUAL_CONTROL
            )

        onSnackBarMessage("onDirectionClick: ${direction.javaClass.simpleName}")
    }

    fun onZoomIn() {
        // Handle zoom in
        CommsInterfaceWebRtc.instance
            .sendJson(
                "{\"zoom\":\"in\"}",
                DataChannels.MANUAL_CONTROL
            )
    }

    fun onZoomOut() {
        // Handle zoom out
        CommsInterfaceWebRtc.instance
            .sendJson(
                "{\"zoom\":\"out\"}",
                DataChannels.MANUAL_CONTROL
            )
    }

    fun onZoomSliderChange(zoomPercentage: Float) {
        // Handle zoom slider change
        CommsInterfaceWebRtc.instance
            .sendJson(
                "{\"zoomPercentage\":\"$zoomPercentage\"}",
                DataChannels.MANUAL_CONTROL
            )
    }

    fun onTakePhotoClick() {
        // Handle take photo
        CommsInterfaceWebRtc.instance
            .sendJson(
                "{\"takePhoto\":\"true\"}",
                DataChannels.MANUAL_CONTROL
            )
    }

    fun onMoreSettingsClick() {
        // Handle more settings
        CommsInterfaceWebRtc.instance
            .sendJson(
                "{\"moreSettings\":\"true\"}",
                DataChannels.MANUAL_CONTROL
            )
    }

    fun onUiModeToggleClick() {
        // Handle toggle
        CommsInterfaceWebRtc.instance
            .sendJson(
                "{\"toggleUiMode\":\"true\"}",
                DataChannels.MANUAL_CONTROL
            )

        // Toggle UI mode
        viewModelScope.launch {
            _uiStateStreamMsg.value = _uiStateStreamMsg.value.copy(
                msg = _uiStateStreamMsg.value.msg.copy(
                    uiMode =
                        if (_uiStateStreamMsg.value.msg.uiMode.id == PtzUiMode.Thermal.id) {
                            UiMode(PtzUiMode.RGB.id, PtzUiMode.RGB.displayName)
                        } else {
                            UiMode(PtzUiMode.Thermal.id, PtzUiMode.Thermal.displayName)
                        }
                )
            )
        }
    }

    fun onSettingsClick() {
        // Handle settings
    }

    fun onPhotosClick() {
        // Handle photos
    }

    val counter = MutableStateFlow(0)
    fun onSnackBarMessage(message: String) {
        viewModelScope.launch {
            counter.value++ // creates new value to force emission
            _snackBarMessages.emit(message + ", msg id:" + counter.value)
        }
    }
}

////////////////////////////////////////////////////
/// SIMULATE CONTROL COMMANDS TO THE PTZ CAMERA ////
////////////////////////////////////////////////////

class DataChannels(command: String, manualControl: Any) {
    companion object {
        const val MANUAL_CONTROL = "manual_control"
    }
}

class CommsInterfaceWebRtc {
    fun sendJson(command: String, manualControl: Any) {
        println("CommsInterfaceWebRtc.sendJson: $command, $manualControl")
    }

    companion object {
        val instance = CommsInterfaceWebRtc()
    }
}
