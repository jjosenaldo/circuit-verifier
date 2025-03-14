package ui.screens.main.sub_screens.failed_assertion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import input.model.ClearsyCircuit
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.model.assertions.UiFailedAssertion
import ui.model.assertions.UiFailedRingBell
import ui.model.assertions.UiFailedShortCircuit
import ui.model.toUiComponents
import ui.screens.main.sub_screens.select_circuit.CircuitViewModel
import verifier.model.assertions.ringbell.RingBellAssertionRunResult
import verifier.model.assertions.short_circuit.ShortCircuitAssertionRunResult
import verifier.model.common.AssertionRunResult

class FailedAssertionViewModel : KoinComponent {
    private val circuitViewModel: CircuitViewModel by inject()

    var selectedFailedAssertion by mutableStateOf(UiFailedAssertion.DEFAULT)
        private set
    var failedAssertions by mutableStateOf(listOf<UiFailedAssertion>())
        private set

    fun setup(failedAssertions: List<AssertionRunResult>) {
        val circuit = circuitViewModel.selectedCircuit

        this.failedAssertions = buildFailedAssertions(circuit, failedAssertions)
        this.failedAssertions.firstOrNull()?.let { selectedFailedAssertion = it }
    }

    fun select(failedAssertion: UiFailedAssertion) {
        selectedFailedAssertion = failedAssertion
    }

    @Suppress("UNCHECKED_CAST")
    private fun buildFailedAssertions(
        circuit: ClearsyCircuit,
        results: List<AssertionRunResult>
    ): List<UiFailedAssertion> {
        return results.groupBy { it.javaClass.kotlin }.map { (klass, results) ->
            when (klass) {
                ShortCircuitAssertionRunResult::class -> buildShortCircuitAssertions(
                    circuit,
                    results as List<ShortCircuitAssertionRunResult>
                )

                RingBellAssertionRunResult::class -> buildRingBellAssertions(
                    circuit,
                    results as List<RingBellAssertionRunResult>
                )

                else -> listOf()
            }
        }.flatten()
    }

    private fun buildShortCircuitAssertions(
        circuit: ClearsyCircuit,
        results: List<ShortCircuitAssertionRunResult>
    ): List<UiFailedAssertion> {
        return results.map {
            UiFailedShortCircuit(
                shortCircuit = it.shortCircuit.toUiComponents(circuit),
                inputs = it.inputs.toUiComponents(circuit)
            )
        }
    }

    private fun buildRingBellAssertions(
        circuit: ClearsyCircuit,
        results: List<RingBellAssertionRunResult>
    ): List<UiFailedAssertion> {
        return results.groupBy { it.activeInputs.joinToString(",") { button -> button.name } }.values.map { resultsByState ->
            UiFailedRingBell(
                contacts = resultsByState.map { it.contact }.toUiComponents(circuit),
                inputs = resultsByState.first().activeInputs.toUiComponents(circuit)
            )
        }
    }
}
