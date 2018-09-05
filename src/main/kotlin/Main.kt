import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.*
import javafx.stage.Stage
import java.awt.Point

class Test : Application() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Test::class.java)
        }
    }

    override fun start(stage: Stage) {
        val x = 50
        val y = 50
        val cellArray = Array(x) { _ -> Array(y) {Cell()} }

        val grid = getGrid(x, y, cellArray)
        val startBtn = Button("Start")
        startBtn.onAction = EventHandler{ _ ->
            cellArray.forEach { array -> array.forEach { it.calculateNextState() } }
            cellArray.forEach { array -> array.forEach {
                it.updateState()
                it.updateView()
            } }
        }
        val resetBtn = Button("Reset")
        resetBtn.onAction = EventHandler{ _ ->
            cellArray.forEach { array -> array.forEach { it.reset() } }
        }
        val skipBtn = Button("Skip 10")
        skipBtn.onAction = EventHandler { _ ->
            var changed = true
            var counter = 0
            while (changed && counter < 10){
                changed = false
                cellArray.forEach { array -> array.forEach { it.calculateNextState() } }
                cellArray.forEach { array -> array.forEach {
                    if (!changed) changed = changed || it.willChange()
                    it.updateState()
                } }
                counter++
            }
            cellArray.forEach { array -> array.forEach {it.updateView()} }
        }
        val btnHBox = HBox(startBtn, skipBtn, resetBtn)
        val vbox = VBox(btnHBox, grid)
        val pane = Pane()
        pane.children.add(vbox)
        val scene = Scene(pane,200.0,200.0)
        stage.scene = scene
        stage.show()
    }

    fun getGrid(width: Int, height: Int, cellArray: Array<Array<Cell>>): GridPane{
        val grid = GridPane()
        for (y in 0 until height){
            for (x in 0 until width){
                val cell = Cell()
                grid.add(cell.component, x, y)
                cellArray[x][y] = cell
                cell.point2D = Point(x, y)
                if (x > 0){
                    cell.W = cellArray[x - 1][y]
                    cellArray[x - 1][y].E = cell
                }
                if (y > 0){
                    cell.N = cellArray[x][y - 1]
                    cellArray[x][y - 1].S = cell
                    if (x > 0){
                        cell.NW = cellArray[x - 1][y - 1]
                        cellArray[x - 1][y - 1].SE = cell
                    }
                    if (x < width - 1){
                        cell.NE = cellArray[x + 1][y - 1]
                        cellArray[x + 1][y - 1].SW = cell
                    }
                }
            }
        }
        return grid
    }

}

fun Boolean?.toInt() = if (this == true) 1 else 0