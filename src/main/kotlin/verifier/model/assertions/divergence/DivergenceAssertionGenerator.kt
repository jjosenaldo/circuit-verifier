package verifier.model.assertions.divergence

import core.model.Circuit
import verifier.model.common.AssertionGenerator
import verifier.model.common.AssertionDefinition

class DivergenceAssertionGenerator : AssertionGenerator {
    override fun generateAssertions(circuit: Circuit): List<AssertionDefinition> {
        return listOf(DivergenceAssertion())
    }
}