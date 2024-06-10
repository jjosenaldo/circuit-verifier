package ui.screens.select_project

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ui.navigation.AppNavigator

@Composable
fun SelectProjectScreen(
    navigator: AppNavigator = koinInject(),
    viewModel: ProjectViewModel = koinInject()
) {
    var showPicker by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val state = viewModel.selectProjectState

    LaunchedEffect(state) {
        if (state is SelectProjectSuccess) {
            navigator.navToSelectCircuit()
        }
    }

    Column {
        when (state) {
            is SelectProjectError -> TODO()
            is SelectProjectInitial -> Button(onClick = {
                showPicker = true
            }) {
                Text("Select a Clearsy project folder")
            }

            is SelectProjectLoading -> Button(onClick = {
            }, enabled = false) {
                CircularProgressIndicator()
            }

            is SelectProjectSuccess -> Box {}
        }
        ClearsyProjectPicker(
            show = showPicker,
            onShowChanged = { showPicker = it },
            onFolderSelected = { scope.launch { viewModel.loadClearsyProject(it) } }
        )
    }
}


@Composable
private fun ClearsyProjectPicker(
    show: Boolean,
    onShowChanged: (Boolean) -> Unit,
    onFolderSelected: (String) -> Unit
) {
    DirectoryPicker(show) { path ->
        onShowChanged(false)
        onFolderSelected(path ?: "")
    }
}