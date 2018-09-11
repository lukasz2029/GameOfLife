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

class GameOfLife : Application() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(GameOfLife::class.java)
        }
        var counter = SimpleIntegerProperty(0)
    }

    override fun start(stage: Stage) {
        val board = Board(200, 200, 13.0, 1.0, 2.0)

        val nextBtn = Button("Next")
        nextBtn.onAction = EventHandler{
            if (board.next()){
                counter.value++
            }
        }
        val resetBtn = Button("Reset")
        resetBtn.onAction = EventHandler{
            board.reset()
            counter.value = 0
        }
        val skipBtn = Button("Skip 10")
        skipBtn.onAction = EventHandler{
            counter.value += board.skip(10)
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

        val counterLabel = Label()
        counter.addListener { _, _, newValue ->
            counterLabel.text = newValue.toString()
        }

        val mainPane = BorderPane()
        val hBox = HBox(nextBtn, skipBtn, resetBtn, Label("B:"), bornTextField, Label("S:"), surviveTextField,
                counterLabel)
        hBox.spacing = 5.0
        hBox.padding = Insets(5.0)
        hBox.alignment = Pos.CENTER_LEFT
        mainPane.center = ScrollPane(board)
        mainPane.top = hBox
        val scene = Scene(mainPane,500.0,300.0)
        stage.scene = scene
        stage.title = "The game of life"
        stage.show()
    }

}

