package lukkon

import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color

class Board(width: Int,
            height: Int,
            private val cellSize: Double = 15.0,
            private val spacing: Double = 2.0,
            private val arcSize: Double = 2.0): StackPane() {

    private val cellArray = Array(width) { _ -> Array(height){ Cell() } }
    private val canvas = Canvas(width*(cellSize + spacing) + spacing, height*(cellSize + spacing) + spacing)
    var counter = SimpleIntegerProperty(0)
    var newCellColor = Color.YELLOW!!

    init {
        initCellArray(width)
        initCanvas()
        children.add(canvas)
        style = "-fx-background-color: lightgrey"
    }

    private fun initCellArray(width: Int){
        for ((x, array) in cellArray.withIndex()) {
            for ((y, cell) in array.withIndex()) {
                initCell(x, y, cell, width)
            }
        }
    }

    private fun initCell(x: Int, y: Int, cell: Cell, width: Int){
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

    private fun initCanvas() {
        canvas.setOnMouseClicked {
            if (it.eventType == MouseEvent.MOUSE_CLICKED && it.button == MouseButton.PRIMARY){
                val x = Math.ceil(it.x/(cellSize + spacing)).toInt() - 1
                val y = Math.ceil(it.y/(cellSize + spacing)).toInt() - 1
                cellArray[x][y].state = !cellArray[x][y].state
                if(cellArray[x][y].state){
                    cellArray[x][y].color = newCellColor
                }
                drawCell(cellArray[x][y])
            }
        }
        drawCells()
    }

    fun next(): Boolean {
        foreachCell { it.calculateNextState() }
        val changed = updateCells()
        if (changed) {
            drawCells()
            counter.value++
        }
        return changed
    }

    fun skip(count: Int): Int{
        var steps = 0
        var draw = false
        var changed = true
        while ((steps < count) && changed){
            foreachCell { it.calculateNextState() }
            changed = updateCells()
            if (changed) {
                draw = true
                steps++
            }
        }
        if (draw){ drawCells() }
        counter.value += steps
        return steps
    }

    fun reset() {
        foreachCell {
            it.reset()
            drawCell(it)
        }
        counter.value = 0
    }

    private fun drawCells() {
        foreachCell {
            it.draw(canvas.graphicsContext2D, cellSize, spacing, arcSize)
        }
    }

    private fun drawCell(cell: Cell){
        cell.draw(canvas.graphicsContext2D, cellSize, spacing, arcSize)
    }

    private fun updateCells(): Boolean{
        var changed = false
        foreachCell {
            if (it.willChange()) {
                it.updateState()
                changed = true
            }
        }
        return changed
    }

    private inline fun foreachCell(action: (Cell) -> Unit){
        for (element in cellArray){
            for (cell in element){
                action(cell)
            }
        }
    }

}