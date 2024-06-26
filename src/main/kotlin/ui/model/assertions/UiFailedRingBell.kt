package ui.model.assertions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import ui.model.UiCircuitParams
import ui.model.UiComponent

// TODO(ft): consider levers on inputs
class UiFailedRingBell(
    val contacts: List<UiComponent>,
    private val inputs: List<UiComponent>
) : UiFailedAssertion() {
    override val details = buildAnnotatedString {
        append("Ring-bell effect detected on contact")
        if (contacts.size > 1) append("s")
        append(" ")
        appendWithColor(contacts, ", ", contactColor, UiComponent::name)
        if (inputs.isEmpty()) return@buildAnnotatedString
        append(" when pressing the button")
        if (inputs.size > 1) append("s")
        append(" ")
        appendWithColor(inputs, ", ", buttonColor, UiComponent::name)
    }
    override val id = contacts.joinToString { it.name } + inputs.joinToString { it.name }
    override fun modifyCircuitParams(circuitParams: UiCircuitParams): UiCircuitParams {
        return circuitParams.copy(
            paths = { listOf() },
            circles = { canvasSize ->
                contacts.map { it.circle(canvasSize).copy(color = contactColor) } +
                        inputs.map { it.circle(canvasSize).copy(color = buttonColor) }
            }
        )
    }

    private fun AnnotatedString.Builder.appendWithColor(text: String, color: Color) {
        withStyle(style = SpanStyle(color = color)) {
            append(text)
        }
    }

    private fun <T> AnnotatedString.Builder.appendWithColor(
        texts: List<T>,
        separator: String,
        color: Color,
        transform: (T) -> String
    ) {
        texts.indices.forEach {
            if (it > 0) append(separator)
            appendWithColor(transform(texts[it]), color)
        }
    }

    companion object {
        private val contactColor = Color.Red
        private val buttonColor = Color.Blue
    }
}