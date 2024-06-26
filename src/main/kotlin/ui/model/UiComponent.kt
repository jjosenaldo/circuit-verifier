package ui.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import core.model.Component
import core.model.PositionDouble
import input.model.ClearsyComponent
import ui.common.distanceBetween
import ui.common.middlePoint
import ui.common.toOffset

data class UiComponent(val positions: List<PositionDouble>, val component: Component) {
    val name = component.name

    fun center(canvasSize: Size): Offset {
        return middlePoint(positions).toOffset(canvasSize)
    }

    fun circle(canvasSize: Size): UiCircleInfo {
        val center = center(canvasSize)
        val radius = when (positions.size) {
            2 -> distanceBetween(positions[0].toOffset(canvasSize), positions[1].toOffset(canvasSize)) / 2
            else -> 50f
        }

        return UiCircleInfo(center, radius)
    }

    companion object {
        fun fromClearsyComponent(component: ClearsyComponent): UiComponent {
            return UiComponent(component.positions, component.component)
        }
    }
}