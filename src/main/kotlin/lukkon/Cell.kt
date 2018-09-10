package lukkon

import java.awt.Point

abstract class Cell{

    companion object {
        var bornSet: Set<Short> = setOf(3)
        var surviveSet: Set<Short> = setOf(2, 3)
    }

    var location: Point = Point()
    var state: Boolean = false
    var nextState: Boolean = false

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
    }

    fun updateState(){
        state = nextState
    }

    abstract fun updateView()

    fun willChange(): Boolean{
        return state != nextState
    }

    fun reset(){
        nextState = false
    }

    private fun Boolean?.toInt() = if (this == true) 1 else 0

}