package ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import ui.window.AppWindowManager

@Composable
fun FrameWindowScope.AppMenuBar(appWindowManager: AppWindowManager) {
    MenuBar {
        Menu("File", mnemonic = 'F') {
            Item(
                "Settings",
                onClick = appWindowManager::openSettings,
                shortcut = KeyShortcut(Key.S, ctrl = true)
            )
        }
    }
}