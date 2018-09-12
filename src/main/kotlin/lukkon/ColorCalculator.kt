package lukkon

import javafx.scene.paint.Color

class ColorCalculator(cell: Cell, sum: Double) {

    private var red = 0.0
    private var green = 0.0
    private var blue = 0.0

    val color: Color

    init {
        addColor(cell.NW)
        addColor(cell.N)
        addColor(cell.NE)
        addColor(cell.W)
        addColor(cell.E)
        addColor(cell.SE)
        addColor(cell.SW)
        addColor(cell.S)

        color = Color(red/sum, green/sum, blue/sum, 1.0)
    }

    private fun addColor(cell: Cell?){
        if (cell != null && cell.state){
            red += cell.color.red
            green += cell.color.green
            blue += cell.color.blue
        }
    }

}