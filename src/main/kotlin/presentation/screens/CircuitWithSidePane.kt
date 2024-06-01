package presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.loadImageBitmap
import presentation.model.UiCircuitParams

@Composable
fun CircuitWithSidePane(
    params: UiCircuitParams,
    pane: @Composable () -> Unit
) {
    Row {
        pane()
        CircuitImage(params)
    }
}


@Composable
private fun CircuitImage(params: UiCircuitParams) {
    if (params.image == null) return
    val image = remember(params.image) {
        loadImageBitmap(params.image.inputStream())
    }
    Canvas(modifier = Modifier) {
        drawImage(image)
        params.circles(Size(image.width.toFloat(), image.height.toFloat())).forEach {
            drawCircle(
                center = it.center,
                color = Color.Red,
                radius = it.radius,
                style = Stroke(
                    width = 3.0f
                )
            )
        }
    }
}