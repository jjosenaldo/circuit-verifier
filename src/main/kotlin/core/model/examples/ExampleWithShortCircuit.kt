package org.example.core.model.examples

import org.example.core.model.*

// It's basically circuit01 without R1 (and C1 consequently)
val exampleWithShortCircuit: Circuit by lazy {
    val p1 = Pole(isPositive = true, name = "P1", isLeft = true, neighbor = Component.DEFAULT)
    val c2 = RelayRegularContact(
        isNormallyOpen = true,
        controller = RelayContactController.DEFAULT,
        leftNeighbor = Component.DEFAULT,
        rightNeighbor = Component.DEFAULT,
        name = "C2"
    )
    val n1 = Pole(isPositive = false, name = "N1", isLeft = false, neighbor = Component.DEFAULT)
    val p2 = Pole(isPositive = true, name = "P2", isLeft = true, neighbor = Component.DEFAULT)
    val r2 = MonostableRelay(
        leftNeighbor = Component.DEFAULT,
        rightNeighbor = Component.DEFAULT,
        contacts = listOf(),
        name = "R2"
    )
    val b1 = Button(leftNeighbor = Component.DEFAULT, rightNeighbor = Component.DEFAULT, name = "B1")
    val n2 = Pole(isPositive = false, name = "N2", isLeft = false, neighbor = Component.DEFAULT)

    p1.neighbor = c2
    c2.leftNeighbor = p1
    c2.rightNeighbor = n1
    c2.controller = r2
    n1.neighbor = c2
    p2.neighbor = r2
    r2.leftNeighbor = p2
    r2.rightNeighbor = b1
    r2.contacts = listOf(c2)
    b1.leftNeighbor = r2
    b1.rightNeighbor = n2
    n2.neighbor = b1

    Circuit(components = listOf(p1, c2, n1, p2, r2, b1, n2))
}