package ui.screens.main.sub_screens.assertions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Check
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ui.common.ErrorDialog
import ui.navigation.AppNavigator
import ui.common.Pane

@Composable
fun AssertionsPane(
    viewModel: AssertionsViewModel = koinInject()
) {
    if (viewModel.assertions.isEmpty()) return

    var errorToShow by remember { mutableStateOf<AssertionError?>(null) }
    viewModel.assertions.firstOrNull { it is AssertionError }?.let {
        errorToShow = it as AssertionError
    }

    errorToShow?.let {
        ErrorDialog(it.error)
    }

    val scope = rememberCoroutineScope()
    
    Pane {
        viewModel.assertions.map { AssertionView(it) }

        if (viewModel.assertions.all { it is AssertionInitial || it is AssertionRunning })
            Button(
                onClick = {
                    if (viewModel.assertions.none { it is AssertionRunning }) {
                        scope.launch {
                            viewModel.runSelectedAssertions()
                        }
                    }
                }
            ) {
                Text(text = "Check selected properties")
            }
    }
}

@Composable
fun AssertionView(
    assertionState: AssertionState,
    viewModel: AssertionsViewModel = koinInject(),
    navigator: AppNavigator = koinInject()
) {
    Column {
        when (assertionState) {
            is AssertionFailed -> Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Sharp.Close,
                        "Error icon",
                        tint = Color(0xfff72702)
                    )
                    Text(text = assertionState.name)
                }
                Button(onClick = { navigator.navToFailedAssertions(assertionState.results) }) {
                    Text("View details")
                }
            }

            is AssertionPassed -> Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Sharp.Check,
                    "Check icon",
                    tint = Color(0xff5fe322)
                )
                Text(text = assertionState.name)
            }

            is AssertionRunning -> Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator()
                Text(text = assertionState.name)
            }

            is AssertionInitial -> Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = assertionState.selected,
                    onCheckedChange = { viewModel.setSelected(it, assertionState) })
                Text(text = assertionState.name)
            }

            is AssertionNotSelected, is AssertionError -> {
                Text(text = assertionState.name)
            }
        }
    }
}

