package ui.window

import androidx.compose.runtime.mutableStateListOf
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.screens.settings.SettingsViewModel

class AppWindowManager : KoinComponent {
    private val settingsViewModel: SettingsViewModel by inject()
    private val _windows = mutableStateListOf(WindowId.SelectProject)
    val windows: List<WindowId> get() = _windows

    fun openProject() {
        open(WindowId.Main)
        close(WindowId.SelectProject)
    }

    fun openSettings() {
        open(WindowId.Settings)
        settingsViewModel.loadCurrentSettings()
    }

    private fun open(window: WindowId) {
        if (_windows.contains(window).not())
            _windows.add(window)
    }

    fun close(window: WindowId) {
        _windows.remove(window)
    }
}
