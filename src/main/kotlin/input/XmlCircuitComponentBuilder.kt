package input

import core.model.*
import input.model.XmlComponentAttributeException
import input.model.XmlComponentFieldException

abstract class XmlCircuitComponentBuilder(val className: String) {
    abstract fun buildXmlComponent(fields: List<Pair<String, String>>): XmlCircuitComponent

    fun readMandatoryAttributeAt(fields: List<Pair<String, String>>, index: Int): String {
        val attributes = fields.filter { it.first == "Attribute" }

        if (attributes.indices.contains(index).not()) {
            throw XmlComponentAttributeException(
                className = className,
                index = index,
                data = null
            )
        }

        return attributes[index].second
    }

    @Suppress("SameParameterValue")
    private fun readMandatoryField(fields: List<Pair<String, String>>, field: String): String {
        return fields.firstOrNull { it.first == field }?.second ?: throw XmlComponentFieldException(
            className = className,
            name = field,
            data = null
        )
    }

    fun readName(fields: List<Pair<String, String>>) = readMandatoryField(fields, "Name")
}

class XmlButtonBuilder : XmlCircuitComponentBuilder("Button") {
    override fun buildXmlComponent(fields: List<Pair<String, String>>): XmlCircuitComponent {
        val button = Button(name = readName(fields))

        return object : XmlCircuitComponent(button, fields) {
            override fun setComponentConnections() {
                connections[0]?.let { button.leftNeighbor = it.component }
                connections[1]?.let { button.rightNeighbor = it.component }
            }
        }
    }
}

class XmlPoleBuilder(private val isPositive: Boolean) : XmlCircuitComponentBuilder("Pole") {
    override fun buildXmlComponent(fields: List<Pair<String, String>>): XmlCircuitComponent {
        val pole = Pole(name = readName(fields), isPositive = isPositive)

        return object : XmlCircuitComponent(pole, fields) {
            override fun setComponentConnections() {
                connections[0]?.let { pole.neighbor = it.component }
            }
        }
    }

}

class XmlMonostableRelayBuilder : XmlCircuitComponentBuilder("Monostable Relay") {
    override fun buildXmlComponent(fields: List<Pair<String, String>>): XmlCircuitComponent {
        val relay = MonostableRelay(name = readName(fields))

        return object : XmlCircuitComponent(relay, fields) {
            override fun setComponentConnections() {
                connections[0]?.let { relay.leftNeighbor = it.component }
                connections[1]?.let { relay.rightNeighbor = it.component }
            }
        }
    }
}

class XmlBistableRelayBuilder : XmlCircuitComponentBuilder("Bistable Relay") {
    override fun buildXmlComponent(fields: List<Pair<String, String>>): XmlCircuitComponent {
        val relay = BistableRelay(name = readName(fields))

        return object : XmlCircuitComponent(relay, fields) {
            override fun setComponentConnections() {
                connections[0]?.let { relay.leftUpNeighbor = it.component }
                connections[1]?.let { relay.leftDownNeighbor = it.component }
                connections[2]?.let { relay.rightUpNeighbor = it.component }
                connections[3]?.let { relay.rightDownNeighbor = it.component }
            }
        }
    }
}

class XmlRelayRegularContactBuilder(private val isNormallyOpen: Boolean) : XmlCircuitComponentBuilder("Relay contact") {
    override fun buildXmlComponent(fields: List<Pair<String, String>>): XmlCircuitComponent {
        val contact = RelayRegularContact(name = readName(fields), isNormallyOpenOrIsLeftOpen = isNormallyOpen)

        return object : XmlCircuitComponent(contact, fields) {
            override fun setComponentConnections() {
                connections[0]?.let { contact.leftNeighbor = it.component }
                connections[1]?.let { contact.rightNeighbor = it.component }
            }
        }
    }
}

class XmlBistableRelayRegularContactBuilder : XmlCircuitComponentBuilder("Bistable contact") {
    private val isLeftClosedIndex = 0

    override fun buildXmlComponent(fields: List<Pair<String, String>>): XmlCircuitComponent {
        val contact = RelayRegularContact(
            name = readName(fields),
            isNormallyOpenOrIsLeftOpen = parseIsLeftOpen(
                readMandatoryAttributeAt(fields, isLeftClosedIndex),
                isLeftClosedIndex
            )
        )

        return object : XmlCircuitComponent(contact, fields) {
            override fun setComponentConnections() {
                connections[0]?.let { contact.leftNeighbor = it.component }
                connections[1]?.let { contact.rightNeighbor = it.component }
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun parseIsLeftOpen(closeSide: String, index: Int): Boolean {
        return when (closeSide) {
            "left" -> false
            "right" -> true
            else -> throw XmlComponentAttributeException(
                className = className,
                index = index,
                data = closeSide
            )
        }
    }
}

class XmlJunctionBuilder : XmlCircuitComponentBuilder("Junction") {
    override fun buildXmlComponent(fields: List<Pair<String, String>>): XmlCircuitComponent {
        val junction = Junction(name = readName(fields))

        return object : XmlCircuitComponent(junction, fields) {
            override fun setComponentConnections() {
                connections[0]?.let { junction.leftNeighbor = it.component }
                connections[1]?.let { junction.upNeighbor = it.component }
                connections[2]?.let { junction.downNeighbor = it.component }
            }
        }
    }
}

class XmlLeverBuilder : XmlCircuitComponentBuilder("Lever") {
    override fun buildXmlComponent(fields: List<Pair<String, String>>): XmlCircuitComponent {
        val lever = Lever(name = readName(fields))

        return object : XmlCircuitComponent(lever, fields) {
            override fun setComponentConnections() {}
        }
    }
}

class XmlLeverContactBuilder : XmlCircuitComponentBuilder("Lever contact") {
    private val openSideAttributeName = "Open side"
    private val openSideAttributeIndex = 0

    override fun buildXmlComponent(fields: List<Pair<String, String>>): XmlCircuitComponent {
        val openSide = parseOpenSide(readMandatoryAttributeAt(fields, openSideAttributeIndex))
        val contact = LeverContact(name = readName(fields), isLeftOpen = openSide)

        return object : XmlCircuitComponent(contact, fields) {
            override fun setComponentConnections() {
                connections[0]?.let { contact.leftNeighbor = it.component }
                connections[1]?.let { contact.rightNeighbor = it.component }
            }
        }
    }

    private fun parseOpenSide(openSide: String): Boolean {
        return when (openSide.lowercase()) {
            "left" -> true
            "right" -> false
            else -> throw XmlComponentFieldException(
                name = openSideAttributeName,
                className = className,
                data = openSide
            )
        }
    }
}

class XmlLampBuilder : XmlCircuitComponentBuilder("Lamp") {
    override fun buildXmlComponent(fields: List<Pair<String, String>>): XmlCircuitComponent {
        val lamp = Lamp(name = readName(fields))

        return object : XmlCircuitComponent(lamp, fields) {
            override fun setComponentConnections() {
                connections[0]?.let { lamp.leftNeighbor = it.component }
                connections[1]?.let { lamp.rightNeighbor = it.component }
            }
        }
    }
}

class XmlRelayChangeOverContactBuilder : XmlCircuitComponentBuilder("RelayChangeOverContact") {
    private val openSideAttributeName = "Open side"
    private val openSideAttributeIndex = 0

    override fun buildXmlComponent(fields: List<Pair<String, String>>): XmlCircuitComponent {
        val openSide = parseOpenSide(readMandatoryAttributeAt(fields, openSideAttributeIndex))
        val contact = RelayChangeOverContact(name = readName(fields), isNormallyUp = openSide)

        return object : XmlCircuitComponent(contact, fields) {
            override fun setComponentConnections() {
                connections[0]?.let { contact.leftNeighbor = it.component }
                connections[1]?.let { contact.upNeighbor = it.component }
                connections[2]?.let { contact.downNeighbor = it.component }
            }
        }
    }

    private fun parseOpenSide(openSide: String): Boolean {
        return when (openSide.lowercase()) {
            "up" -> true
            "down" -> false
            else -> throw XmlComponentFieldException(
                name = openSideAttributeName,
                className = className,
                data = openSide
            )
        }
    }
}

class XmlResistorBuilder : XmlCircuitComponentBuilder("Resistor") {
    override fun buildXmlComponent(fields: List<Pair<String, String>>): XmlCircuitComponent {
        val resistor = Resistor(name = readName(fields))

        return object : XmlCircuitComponent(resistor, fields) {
            override fun setComponentConnections() {
                connections[0]?.let { resistor.leftNeighbor = it.component }
                connections[1]?.let { resistor.rightNeighbor = it.component }
            }
        }
    }
}

class XmlCapacitorBuilder : XmlCircuitComponentBuilder("Capacitor") {
    private val minMaxCharge = 1
    private val maxMaxCharge = 50
    private val minInitialCharge = 0
    private val maxInitialCharge = maxMaxCharge
    private val maxChargeIndex = 0
    private val initialChargeIndex = 1

    override fun buildXmlComponent(fields: List<Pair<String, String>>): XmlCircuitComponent {
        val maxCharge = parseCharge(
            readMandatoryAttributeAt(fields, maxChargeIndex),
            minMaxCharge,
            maxMaxCharge,
            maxChargeIndex
        )
        val capacitor = Capacitor(
            name = readName(fields),
            maxCharge = maxCharge,
            initialCharge = parseInitialCharge(
                readMandatoryAttributeAt(fields, initialChargeIndex),
                minInitialCharge,
                maxInitialCharge,
                initialChargeIndex,
                maxCharge
            )
        )

        return object : XmlCircuitComponent(capacitor, fields) {
            override fun setComponentConnections() {
                connections[0]?.let { capacitor.leftNeighbor = it.component }
                connections[1]?.let { capacitor.rightNeighbor = it.component }
            }
        }
    }

    private fun parseCharge(charge: String, min: Int, max: Int, index: Int): Int {
        try {
            val chargeInt = charge.toInt()

            if (chargeInt < min || chargeInt > max) {
                throw XmlComponentAttributeException(
                    className = className,
                    index = index,
                    data = charge,
                    details = "Must be between $min and $max."
                )
            }

            return chargeInt
        } catch (_: Exception) {
            throw XmlComponentAttributeException(
                className = className,
                index = index,
                data = charge
            )
        }
    }

    @Suppress("SameParameterValue")
    private fun parseInitialCharge(charge: String, min: Int, max: Int, index: Int, maxCharge: Int): Int {
        val chargeInt = parseCharge(charge, min, max, index)

        if (chargeInt > maxCharge) {
            throw XmlComponentAttributeException(
                className = className,
                index = index,
                data = charge,
                details = "Can't be higher than max charge."
            )
        }

        return chargeInt
    }
}