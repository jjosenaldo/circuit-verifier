package ui.screens.project_selected.sub_screens.select_circuit

import androidx.compose.runtime.Composable
import org.koin.compose.koinInject
import ui.model.UiCircuitParams
import ui.screens.project_selected.SelectedProjectScreenContent

@Composable
fun SelectCircuitScreen(
    circuitVm: CircuitViewModel = koinInject()
) {
    SelectedProjectScreenContent(
        UiCircuitParams(circuitVm.selectedCircuitImage)
    ) { SelectCircuitPane() }
}