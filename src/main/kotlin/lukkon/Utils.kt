package lukkon

import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import java.util.function.Supplier
import java.awt.Point

object Utils {

    fun <E:Cell> getCellArray(supplier : Supplier<E>, width: Int, height: Int): Array<Array<Cell>>{
        val cellArray = Array<Array<Cell>>(width) { _ -> Array(height){supplier.get()} }

        for (y in 0 until height) {
            for (x in 0 until width) {
                val cell = supplier.get()
                cellArray[x][y] = cell
                cell.location = Point(x, y)
                if (x > 0) {
                    cell.W = cellArray[x - 1][y]
                    cellArray[x - 1][y].E = cell
                }
                if (y > 0) {
                    cell.N = cellArray[x][y - 1]
                    cellArray[x][y - 1].S = cell
                    if (x > 0) {
                        cell.NW = cellArray[x - 1][y - 1]
                        cellArray[x - 1][y - 1].SE = cell
                    }
                    if (x < width - 1) {
                        cell.NE = cellArray[x + 1][y - 1]
                        cellArray[x + 1][y - 1].SW = cell
                    }
                }
            }
        }
        return cellArray
    }

    val size = 15.0
    val spacing = 2.0
    val arcSize = 5.0

    fun getCanvas(cellArray: Array<Array<Cell>>): Canvas {
        val width: Int = cellArray.size
        val height: Int = cellArray.first().size

        val canvas = Canvas(width*(size + spacing) + spacing, height*(size + spacing) + spacing)
        val gc = canvas.graphicsContext2D

        canvas.setOnMouseClicked {
            if (it.eventType == MouseEvent.MOUSE_CLICKED && it.button == MouseButton.PRIMARY){
                val x = Math.ceil(it.x/(size + spacing)).toInt() - 1
                val y = Math.ceil(it.y/(size + spacing)).toInt() - 1
                println("$x, $y")
                cellArray[x][y].state = !cellArray[x][y].state
                cellArray[x][y].updateView()
                gc.fill = (cellArray[x][y] as SimpleCell).color
                gc.fillRoundRect(
                        x*(size + spacing) + spacing,
                        y*(size + spacing) + spacing,
                        size, size, arcSize, arcSize)
            }
        }

        for (x: Int in 0 until width){
            for (y: Int in 0 until height) {
                val cell = cellArray[x][y] as SimpleCell
                gc.fill = cell.color
                gc.fillRoundRect(
                        x*(size + spacing) + spacing,
                        y*(size + spacing) + spacing,
                        size, size, 5.0, 5.0)
            }
        }
        return canvas
    }

}