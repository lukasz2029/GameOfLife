package lukkon

import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.layout.*
import javafx.stage.Stage
import lukkon.Utils.getCellArray
import lukkon.Utils.getGridPane

class GameOfLife : Application() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(GameOfLife::class.java)
        }
    }

    override fun start(stage: Stage) {
        val x = 100
        val y = 100
        val cellArray  = getCellArray(x, y)

        val nextBtn = Button("Next")
        nextBtn.onAction = EventHandler{ _ ->
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

        val mainPane = BorderPane()
        val hbox = HBox(nextBtn, skipBtn, resetBtn, Label("B:"), bornTextField, Label("S:"), surviveTextField)
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

}

fun Boolean?.toInt() = if (this == true) 1 else 0