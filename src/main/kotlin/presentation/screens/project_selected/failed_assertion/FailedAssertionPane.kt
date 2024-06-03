package presentation.screens.project_selected.failed_assertion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
import presentation.screens.common.Pane

@Composable
fun FailedAssertionPane(viewModel: FailedAssertionViewModel = koinInject()) {
    Pane {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text(viewModel.selectedFailedAssertion.details)
        }
    }

}