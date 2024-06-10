package ui.screens.project_selected

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import ui.common.CircuitImage
import ui.common.SecondaryMenuBar
import ui.common.VerticalDivider
import ui.model.UiCircuitParams

@Composable
fun SelectedProjectScreenContent(
    params: UiCircuitParams,
    pane: @Composable () -> Unit
) {
    Column {
        SecondaryMenuBar()
        Row {
            pane()
            VerticalDivider()
            CircuitImage(params)
        }
    }
}