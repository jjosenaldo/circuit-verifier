package core.model

import core.model.visitor.ComponentVisitor

/**
 * Starts unpressed
 */
class Button(
    var leftNeighbor: Component = DEFAULT,
    var rightNeighbor: Component = DEFAULT,
    name: String
) : BinaryInput, Component(name) {
    override fun acceptVisitor(visitor: ComponentVisitor) {
        visitor.visitButton(this)
    }
}