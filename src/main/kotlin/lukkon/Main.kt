package lukkon

import javafx.application.Application
import javafx.beans.property.SimpleIntegerProperty
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.stage.Stage
import lukkon.Utils.getCellArray
import lukkon.Utils.getGridPane

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
        val cellArray  = getCellArray(x, y)

        val nextBtn = Button("Next")
        nextBtn.onAction = EventHandler{ next(cellArray) }
        val resetBtn = Button("Reset")
        resetBtn.onAction = EventHandler{ reset(cellArray) }
        val skipBtn = Button("Skip 10")
        skipBtn.onAction = EventHandler{ skip(cellArray, 10) }

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
        mainPane.center = ScrollPane(getGridPane(cellArray))
        mainPane.top = hbox
        val scene = Scene(mainPane,500.0,300.0)
        stage.scene = scene
        stage.title = "The game of life"
        stage.show()
    }

    private fun next(cellArray: Array<Array<Cell>>){
        if(updateCells(cellArray)){
            counter.value++
        }
    }

    private fun skip(cellArray: Array<Array<Cell>>, count: Int){
        var steps = 0
        while ((steps < count) && updateCells(cellArray)){
            steps++
        }
        counter.value += steps
    }

    private fun reset(cellArray: Array<Array<Cell>>) {
        cellArray.forEach { array -> array.forEach { it.reset() } }
        counter.value = 0
    }

    private fun updateCells(cellArray: Array<Array<Cell>>): Boolean{
        var changed = false
        cellArray.forEach { array -> array.forEach { it.calculateNextState() } }
        cellArray.forEach { array -> array.forEach {
            if (it.willChange()) {
                it.updateState()
                it.updateView()
                changed = true
            }
        } }
        return changed
    }

}

