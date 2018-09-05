package lukkon

import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
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
        val x = 50
        val y = 50
        val cellArray  = getCellArray(x, y)

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
        val vBox = VBox(btnHBox, getGridPane(cellArray))
        val pane = Pane()
        pane.children.add(vBox)
        val scene = Scene(pane,200.0,200.0)
        stage.scene = scene
        stage.show()
    }


}

fun Boolean?.toInt() = if (this == true) 1 else 0