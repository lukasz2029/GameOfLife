package lukkon

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import java.awt.Point

class Cell{

    companion object {
        var bornSet: Set<Short> = setOf(3)
        var surviveSet: Set<Short> = setOf(2, 3)
    }

    var location: Point = Point()
    var state: Boolean = false
    var nextState: Boolean = false
    var color= Color.YELLOW!!
    var nextColor= Color.YELLOW!!

    var NW: Cell? = null
    var N: Cell? = null
    var NE: Cell? = null
    var W: Cell? = null
    var E: Cell? = null
    var SW: Cell? = null
    var S: Cell? = null
    var SE: Cell? = null

    fun calculateNextState(){
        val sum =
                (NW?.state.toInt() + N?.state.toInt() + NE?.state.toInt() +
                W?.state.toInt() + E?.state.toInt() +
                SW?.state.toInt() + S?.state.toInt() + SE?.state.toInt()).toShort()
        nextState = (!state && bornSet.contains(sum)) || (state && surviveSet.contains(sum))
        if (willChange() && nextState){
            nextColor = ColorCalculator(this, sum.toDouble()).color
        }
    }

    fun updateState(){
        state = nextState
        color = nextColor
    }

    fun willChange(): Boolean{
        return state != nextState
    }

    fun reset(){
        state = false
        nextState = false
    }

    fun draw(gc: GraphicsContext, cellSize: Double, spacing: Double, arcSize: Double) {
        gc.fill = if (state) color else Color.GRAY
        gc.fillRoundRect(
                location.x*(cellSize + spacing) + spacing,
                location.y*(cellSize + spacing) + spacing,
                cellSize, cellSize, arcSize, arcSize)
    }

    private fun Boolean?.toInt() = if (this == true) 1 else 0

}