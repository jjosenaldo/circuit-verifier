package ui.screens.main.sub_screens.assertions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.model.MonostableSimpleContact
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.screens.main.sub_screens.select_circuit.CircuitViewModel
import verifier.AssertionManager
import verifier.model.common.AssertionType

class AssertionsViewModel(
    private val assertionManager: AssertionManager
) : KoinComponent {
    private val circuitViewModel: CircuitViewModel by inject()

    var multiselectDataId by mutableStateOf(0)
        private set

    var assertions by mutableStateOf(listOf<AssertionState>())
        private set

    fun setAssertionsFromTypes(types: List<AssertionType>) {
        assertions = types.map {
            AssertionInitial(
                type = it, data = when (it) {
                    AssertionType.ContactStatus -> MultiselectAssertionData(
                        keys = circuitViewModel.selectedCircuit.circuit.components.filterIsInstance<MonostableSimpleContact>()
                            .map { contact -> contact.name },
                        defaultValue = false
                    )

                    else -> EmptyAssertionData
                }
            )
        }
    }

    fun reset() {
        setAssertionsFromTypes(assertions.map(AssertionState::type))
    }

    fun setSelected(selected: Boolean, assertion: AssertionInitial) {
        val assertionIndex = assertions.indexOfFirst { it.type == assertion.type }
        val newAssertions = ArrayList(assertions)
        assertion.data.onSelected(selected)
        newAssertions[assertionIndex] = assertion.copyWith(selected = selected)
        assertions = newAssertions
    }

    fun updateMultiselect(newData: MultiselectAssertionData<*>) {
        val assertionIndex = assertions.indexOfFirst { it.data is MultiselectAssertionData<*> }
        val newAssertions = ArrayList(assertions)
        val newAssertion = assertions[assertionIndex].withData(newData)
        newAssertions[assertionIndex] = newAssertion
        assertions = newAssertions
        multiselectDataId += 1

        if (!newAssertion.data.isValid() && newAssertion is AssertionInitial)
            setSelected(false, newAssertion)
    }

    suspend fun runSelectedAssertions() {
        assertions = assertions.map {
            if (it is AssertionInitial && it.selected)
                AssertionRunning(it.type, it.data)
            else
                AssertionNotSelected(it.type, it.data)
        }

        val typesToCheck = assertions.filterIsInstance<AssertionRunning>().map { it.type }

        try {
            val allFailingAssertions =
                assertionManager.runAssertionsReturnFailing(
                    circuitViewModel.selectedCircuit.circuit,
                    typesToCheck
                )
            assertions = assertions.map {
                val failingAssertions = allFailingAssertions[it.type] ?: listOf()

                when {
                    failingAssertions.isNotEmpty() -> AssertionFailed(it.type, failingAssertions, it.data)
                    typesToCheck.contains(it.type) -> AssertionPassed(it.type, it.data)
                    else -> it
                }
            }
        } catch (e: Throwable) {
            assertions = assertions.map { AssertionError(it.type, e, it.data) }
        }
    }

}
