package ui.window.windows

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.*
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import org.koin.compose.koinInject
import ui.common.AppMenuBar
import ui.screens.main.MainScreen
import ui.screens.main.sub_screens.select_circuit.CircuitViewModel

// TODO(ft): window title
@Composable
fun ApplicationScope.MainWindow(
    circuitViewModel: CircuitViewModel = koinInject()
) =
    Window(onCloseRequest = ::exitApplication, title = "App", onKeyEvent = {
        if (it.isCtrlPressed && it.type == KeyEventType.KeyDown && it.key in listOf(Key.Equals, Key.Minus)) {
            circuitViewModel.zoom(it.key == Key.Equals)
            true
        } else {
            // let other handlers receive this event
            false
        }
    }) {
        AppMenuBar()
        MainScreen()
    }

