package core.model.visitor

import core.model.*

interface ComponentVisitor {
    fun visitButton(button: Button)
    fun visitPole(pole: Pole)
    fun visitMonostableRelay(monostableRelay: MonostableRelay)
    fun visitRelayRegularContact(contact: RelayRegularContact)
    fun visitCapacitor(capacitor: Capacitor)
    fun visitJunction(junction: Junction)
}