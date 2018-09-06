package lukkon

import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import java.awt.geom.Point2D

class Cell{

    companion object {
        val whiteBg = Background( BackgroundFill(Color.WHITE, CornerRadii(3.0), Insets(2.0)))
        val blackBg = Background( BackgroundFill(Color.BLACK, CornerRadii(3.0), Insets(2.0)))
        var bornSet: Set<Short> = setOf(3)
        var surviveSet: Set<Short> = setOf(2, 3)
    }

    var point2D: Point2D? = null
    var state: Boolean = false
    var nextState: Boolean = false
    var component: Button = Button()
    var NW: Cell? = null
    var N: Cell? = null
    var NE: Cell? = null
    var W: Cell? = null
    var E: Cell? = null
    var SW: Cell? = null
    var S: Cell? = null
    var SE: Cell? = null

    init {
        component.setMinSize(20.0,20.0)
        component.setMaxSize(20.0,20.0)
        component.background = whiteBg

        component.onAction = EventHandler {
            state = !state
            component.background = if(state) blackBg else whiteBg
        }
    }

    fun calculateNextState(){
        val sum =
                (NW?.state.toInt() + N?.state.toInt() + NE?.state.toInt() +
                W?.state.toInt() + E?.state.toInt() +
                SW?.state.toInt() + S?.state.toInt() + SE?.state.toInt()).toShort()
        nextState = (!state && bornSet.contains(sum)) || (state && surviveSet.contains(sum))
    }

    fun updateState(){
        state = nextState
    }

    fun updateView(){
        component.background = if(state) blackBg else whiteBg
    }

    fun willChange(): Boolean{
        return state == nextState
    }

    fun reset(){
        nextState = false
        updateState()
        updateView()
    }

    private fun Boolean?.toInt() = if (this == true) 1 else 0
}