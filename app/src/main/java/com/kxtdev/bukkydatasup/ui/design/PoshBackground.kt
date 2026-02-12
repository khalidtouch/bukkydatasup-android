package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.kxtdev.bukkydatasup.ui.theme.MainAppTheme
import com.kxtdev.bukkydatasup.ui.theme.ThemePreviews

@Composable
fun PoshCustomBackground(
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.secondary,
    content: @Composable () -> Unit,
) {

    Box(Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface))

    Canvas(modifier) {

        val w = size.width
        val h = 600.0f

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(w + 45f, 0f)

            cubicTo(
                x1 = w * 0.75f,
                y1 = h * 0.35f,
                x2 = w * 0.55f,
                y2 = h * 0.55f,
                x3 = w * 0.4f,
                y3 = h * 0.4f
            )

            cubicTo(
                x1 = w * 0.25f,
                y1 = h * 0.25f,
                x2 = w * 0.1f,
                y2 = h * 0.45f,
                x3 = 0f,
                y3 = h * 0.35f
            )

            lineTo(0f, 0f)

            close()
        }

        drawPath(
            path = path,
            color = primaryColor
        )

    }

    Box(
        Modifier.fillMaxSize()
    ) { content.invoke() }

}



@Composable
@ThemePreviews
private fun PoshBackgroundPreviews() {
    MainAppTheme {
        Box(Modifier.fillMaxSize()) {
            PoshCustomBackground(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                primaryColor = Red
            ) {

                Text("Yewa sye")

            }
        }

    }
}