package com.realityexpander.complexcomposeui.ui.ptzCamera.components.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realityexpander.complexcomposeui.ui.theme.AppDimens
import com.realityexpander.complexcomposeui.ui.theme.openSansFont

@Preview(
    showBackground = true,
    backgroundColor = 0x777777,
    name = "Text",
    device = "spec:width=880px,height=840px,dpi=240",
    fontScale = 1f
)
@Composable
fun CamTextPreview() {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("displaySmall", fontSize = 25.sp)
        CamText(
            text = "PTZ Camera",
            modifier = Modifier,
            fontSize = MaterialTheme.typography.displaySmall.fontSize,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(25.dp))

        Text("displayMedium, TextAlign=Start", fontSize = 25.sp)
        CamText(
            text = "Test String 12345",
            modifier = Modifier
                .fillMaxWidth(),
            fontSize = MaterialTheme.typography.displayMedium.fontSize,
            color = Color.White,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(25.dp))

        Text("displayLarge", fontSize = 25.sp)
        CamText(
            text = "Test String 12345",
            modifier = Modifier,
            fontSize = MaterialTheme.typography.displayLarge.fontSize,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(25.dp))

        Text("displayLarge scaled 80%", fontSize = 25.sp)
        CamText(
            text = "Test String 12345",
            modifier = Modifier,
            fontSize = MaterialTheme.typography.displayLarge.fontSize,
            color = Color.White,
            fontScale = 0.8f
        )
        Spacer(modifier = Modifier.height(25.dp))
    }
}

@Composable
fun CamText(
    text: String = "Test String",
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    fontSize: TextUnit = MaterialTheme.typography.displaySmall.fontSize,
    fontScale: Float = 1f,
    textAlign: TextAlign = TextAlign.Center
) {
    Box(
        modifier = Modifier
    ) {
        Text(
            text,
            modifier = modifier
                .wrapContentHeight(unbounded = true)
                .offset((1.5).dp, (1.5).dp),
            style = TextStyle(
                color = Color.Black.copy(alpha =.5f),
                fontSize = fontSize.times(AppDimens.fontScale * fontScale),
                fontFamily = openSansFont
            ),
            textAlign = textAlign,
        )
        Text(
            text,
            modifier = modifier
                .wrapContentHeight(unbounded = true)
            ,
            style = TextStyle(
                color = color,
                fontSize = fontSize.times(AppDimens.fontScale * fontScale),
                fontFamily = openSansFont
            ),
            textAlign = textAlign
        )
    }
}
