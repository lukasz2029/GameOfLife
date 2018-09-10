package lukkon

import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent

class Board(width: Int,
            height: Int,
            val cellSize: Double = 15.0,
            val spacing: Double = 2.0,
            val arcSize: Double = 2.0) {

    val cellArray = Array(width) { _ -> Array(height){ SimpleCell() } }
    val canvas = Canvas(width*(cellSize + spacing) + spacing, height*(cellSize + spacing) + spacing)

    init {
        initCellArray(width)
        initCanvas()
    }

    private fun initCellArray(width: Int){
        foreachCellIndexed { x, y, cell ->
            cell.location.x = x
            cell.location.y = y
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

    private fun initCanvas() {
        val gc = canvas.graphicsContext2D

        canvas.setOnMouseClicked {
            if (it.eventType == MouseEvent.MOUSE_CLICKED && it.button == MouseButton.PRIMARY){
                val x = Math.ceil(it.x/(cellSize + spacing)).toInt() - 1
                val y = Math.ceil(it.y/(cellSize + spacing)).toInt() - 1
                cellArray[x][y].state = !cellArray[x][y].state
                cellArray[x][y].updateView()
                gc.fill = (cellArray[x][y]).color
                gc.fillRoundRect(
                        x*(cellSize + spacing) + spacing,
                        y*(cellSize + spacing) + spacing,
                        cellSize, cellSize, arcSize, arcSize)
            }
        }

        foreachCellIndexed { x, y, cell ->
            gc.fill = cell.color
            gc.fillRoundRect(
                    x*(cellSize + spacing) + spacing,
                    y*(cellSize + spacing) + spacing,
                    cellSize, cellSize, 5.0, 5.0)
        }
    }

    inline fun foreachCell(action: (SimpleCell) -> Unit){
        for (element in cellArray){
            for (cell in element){
                action(cell)
            }
        }
    }

    inline fun foreachCellIndexed(action: (x: Int, y: Int, SimpleCell) -> Unit){
        for ((x, array) in cellArray.withIndex()) {
            for ((y, cell) in array.withIndex()) {
                action(x, y, cell)
            }
        }
    }

}