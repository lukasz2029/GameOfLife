package lukkon

import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import java.awt.Point

class Board(val width: Int,
            val height: Int,
            val cellSize: Double = 15.0,
            val spacing: Double = 2.0,
            val arcSize: Double = 2.0) {

    val cellArray = Array<Array<Cell>>(width) { _ -> Array(height){ SimpleCell() } }
    val canvas = Canvas(width*(cellSize + spacing) + spacing, height*(cellSize + spacing) + spacing)

    init {
        initCellArray()
        initCanvas()
    }

    private fun initCellArray(){
        for (y in 0 until height) {
            for (x in 0 until width) {
                val cell = cellArray[x][y]
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
    }

    private fun initCanvas() {
        val gc = canvas.graphicsContext2D

        canvas.setOnMouseClicked {
            if (it.eventType == MouseEvent.MOUSE_CLICKED && it.button == MouseButton.PRIMARY){
                val x = Math.ceil(it.x/(cellSize + spacing)).toInt() - 1
                val y = Math.ceil(it.y/(cellSize + spacing)).toInt() - 1
                cellArray[x][y].state = !cellArray[x][y].state
                cellArray[x][y].updateView()
                gc.fill = (cellArray[x][y] as SimpleCell).color
                gc.fillRoundRect(
                        x*(cellSize + spacing) + spacing,
                        y*(cellSize + spacing) + spacing,
                        cellSize, cellSize, arcSize, arcSize)
            }
        }

        for (x: Int in 0 until width){
            for (y: Int in 0 until height) {
                val cell = cellArray[x][y] as SimpleCell
                gc.fill = cell.color
                gc.fillRoundRect(
                        x*(cellSize + spacing) + spacing,
                        y*(cellSize + spacing) + spacing,
                        cellSize, cellSize, 5.0, 5.0)
            }
        }
    }

}