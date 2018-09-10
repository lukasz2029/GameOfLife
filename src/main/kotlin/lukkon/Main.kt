package lukkon

import javafx.application.Application
import javafx.beans.property.SimpleIntegerProperty
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import lukkon.Utils.size
import lukkon.Utils.spacing
import java.util.function.Supplier

class GameOfLife : Application() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(GameOfLife::class.java)
        }
        var counter = SimpleIntegerProperty(0)
    }

    override fun start(stage: Stage) {
        val x = 50
        val y = 50
        val cellArray: Array<Array<Cell>> = Utils.getCellArray(Supplier { SimpleCell() }, x, y)

        val canvas = Utils.getCanvas(cellArray)
        val holder = StackPane(canvas)
        holder.style = "-fx-background-color: lightgrey"

        val nextBtn = Button("Next")
        nextBtn.onAction = EventHandler{ next(cellArray, canvas) }
        val resetBtn = Button("Reset")
        resetBtn.onAction = EventHandler{ reset(cellArray, canvas) }
        val skipBtn = Button("Skip 10")
        skipBtn.onAction = EventHandler{ skip(cellArray, canvas, 10) }

        val rulesRegex = "^(?!.*(.).*\\1)[012345678]*\$".toRegex()
        val bornTextField = TextField("2")
        bornTextField.prefWidth = 75.0
        bornTextField.textProperty().addListener { _, oldValue, newValue ->
            if (!newValue.matches(rulesRegex)){
                bornTextField.text = oldValue
            }else if(oldValue.matches(rulesRegex)){
                val set = HashSet<Short>()
                newValue.forEach {
                    set.add((Integer.parseInt(it.toString())).toShort()) //XXX
                }
                Cell.bornSet = set
            }
        }
        val surviveTextField = TextField("23")
        surviveTextField.prefWidth = 75.0
        surviveTextField.textProperty().addListener { _, oldValue, newValue ->
            if (!newValue.matches(rulesRegex)){
                surviveTextField.text = oldValue
            }else if(oldValue.matches(rulesRegex)){
                val set = HashSet<Short>()
                newValue.forEach {
                    set.add((Integer.parseInt(it.toString())).toShort()) //XXX
                }
                Cell.surviveSet = set
            }
        }

        val counterLabel = Label()
        counter.addListener { _, _, newValue ->
            counterLabel.text = newValue.toString()
        }

        val mainPane = BorderPane()
        val hbox = HBox(nextBtn, skipBtn, resetBtn, Label("B:"), bornTextField, Label("S:"), surviveTextField,
                counterLabel)
        hbox.spacing = 5.0
        hbox.padding = Insets(5.0)
        hbox.alignment = Pos.CENTER_LEFT
        mainPane.center = ScrollPane(holder)
        mainPane.top = hbox
        val scene = Scene(mainPane,500.0,300.0)
        stage.scene = scene
        stage.title = "The game of life"
        stage.show()
    }

    private fun next(cellArray: Array<Array<Cell>>, canvas: Canvas){
        cellArray.forEach { array -> array.forEach { it.calculateNextState() } }
        if(updateCells(cellArray, canvas)){
            counter.value++
        }
    }

    private fun skip(cellArray: Array<Array<Cell>>, canvas: Canvas, count: Int){
        var steps = 0
        cellArray.forEach { array -> array.forEach { it.calculateNextState() } }
        while ((steps < count) && updateCells(cellArray, canvas)){
            cellArray.forEach { array -> array.forEach { it.calculateNextState() } }
            steps++
        }
        counter.value += steps
    }

    private fun reset(cellArray: Array<Array<Cell>>, canvas: Canvas) {
        cellArray.forEach { array -> array.forEach { it.reset() } }
        updateCells(cellArray, canvas)
        counter.value = 0
    }

    private fun updateCells(cellArray: Array<Array<Cell>>, canvas: Canvas): Boolean{
        var changed = false
        val gc = canvas.graphicsContext2D
        cellArray.forEach { col -> col.forEach {cell ->
            if (cell.willChange()) {
                cell.updateState()
                cell.updateView()
                val x = cell.location!!.x
                val y = cell.location!!.y
                gc.fill = (cellArray[x][y] as SimpleCell).color
                gc.fillRoundRect(
                        x*(size + spacing) + spacing,
                        y*(size + spacing) + spacing,
                        size, size, 5.0, 5.0)
                changed = true
            }
        } }
        return changed
    }

}

