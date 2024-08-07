package ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ui.common.*
import ui.window.AppWindowManager
import ui.window.WindowId

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = koinInject(),
    windowManager: AppWindowManager = koinInject()
) {
    Column(Modifier.padding(16.dp).fillMaxSize()) {
        BidirectionalScrollBar(modifier = Modifier.fillMaxHeight().weight(1f)) {
            SettingId.entries.forEachIndexed { index, state ->
                if (index > 0) Spacer(modifier = Modifier.height(10.dp))
                settingsViewModel.settingsStates[state]?.let {
                    SettingRow(it)
                }
            }
        }
        Divider(color = tertiaryBackgroundColor, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
        Row(modifier = Modifier.align(Alignment.End), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            AppButton(
                onClick = {
                    settingsViewModel.saveSettings()
                    windowManager.closeSettings()
                },
                enabled = settingsViewModel.isSaveEnabled(),
            ) {
                AppText("Save")
            }
            AppButton(
                onClick = windowManager::closeSettings,
                kind = AppButtonKind.Secondary
            ) {
                AppText("Cancel")
            }
        }
    }
}

@Composable
private fun SettingRow(state: UiState<SettingConfig>) {
    Column {
        state.data?.let {
            AppText(it.id.title)
            when (it) {
                is FdrPathSetting -> FdrPathSettingRow(it)
                is TimeoutSetting -> TimeoutTimeSettingRow(it)
            }
            if (state is UiError)
                AppText(state.message, kind = AppTextKind.Error)

        }
    }

}

@Composable
private fun TimeoutTimeSettingRow(
    data: TimeoutSetting,
    settingsViewModel: SettingsViewModel = koinInject()
) {
    AppEditText(data.data, Modifier.width(100.dp)) { text ->
        val newText =
            text.filter { it.isDigit() }.substring(0, text.length.coerceAtMost(TimeoutSetting.MAX_TIME_MINUTES_LENGTH))
        settingsViewModel.setSetting(data.id, newText)
        newText
    }
}

@Composable
private fun FdrPathSettingRow(
    data: FdrPathSetting,
    settingsViewModel: SettingsViewModel = koinInject()
) {
    val scope = rememberCoroutineScope()
    var showPicker by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { showPicker = true }, modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)) {
            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = "Open FDR folder",
                tint = iconColor
            )
        }
        AppText(data.data, modifier = Modifier.padding(start = 4.dp))
    }

    FolderPicker(
        show = showPicker,
        onShowChanged = { showPicker = it },
        onFolderSelected = {
            if (it != null) {
                scope.launch { settingsViewModel.setSettingAsync(SettingId.FdrPath, it) }
            }
        }
    )
}


private fun AppWindowManager.closeSettings() {
    close(WindowId.Settings)
}