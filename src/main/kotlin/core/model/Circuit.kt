package core.model

class Circuit(val components: List<Component>) {
    val relayContacts =
        components.filterIsInstance<Contact>().filter { it is MonostableSimpleContact || it is RelayChangeoverContact }
    val buttons = components.filterIsInstance<Button>()

    companion object {
        val DEFAULT = Circuit(listOf())
    }
}