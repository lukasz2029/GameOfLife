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
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.stage.Stage

class GameOfLife : Application() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(GameOfLife::class.java)
        }
    }

    override fun start(stage: Stage) {
        val board = Board(200, 200, 13.0, 1.0, 2.0)
        val boardAnimator = BoardAnimator(board)

        val startStopBtn = Button("Start")
        startStopBtn.onAction = EventHandler{
            startStopBtn.text = if (!boardAnimator.active){ "Stop" } else { "Start" }
            boardAnimator.startStop()
        }
        val nextBtn = Button("Next")
        nextBtn.onAction = EventHandler{
            board.next()
        }
        val skip10Btn = Button("Skip 10")
        skip10Btn.onAction = EventHandler{
            board.skip(10)
        }
        val skip100Btn = Button("Skip 100")
        skip100Btn.onAction = EventHandler{
            board.skip(100)
        }
        val resetBtn = Button("Reset")
        resetBtn.onAction = EventHandler{
            board.reset()
        }
        val redBtn = Button("  ")
        redBtn.style = "-fx-background-color: red"
        redBtn.onAction = EventHandler{
            board.newCellColor = Color.RED
        }
        val greenBtn = Button("  ")
        greenBtn.style = "-fx-background-color: green"
        greenBtn.onAction = EventHandler{
            board.newCellColor = Color.GREEN
        }
        val blueBtn = Button("  ")
        blueBtn.style = "-fx-background-color: blue"
        blueBtn.onAction = EventHandler{
            board.newCellColor = Color.BLUE
        }
        val yellowBtn = Button("  ")
        yellowBtn.style = "-fx-background-color: yellow"
        yellowBtn.onAction = EventHandler{
            board.newCellColor = Color.YELLOW
        }
        val whiteBtn = Button("  ")
        whiteBtn.style = "-fx-background-color: white"
        whiteBtn.onAction = EventHandler{
            board.newCellColor = Color.WHITE
        }
        val blackBtn = Button("  ")
        blackBtn.style = "-fx-background-color: black"
        blackBtn.onAction = EventHandler{
            board.newCellColor = Color.BLACK
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

        val counterLabel = Label("Step: 0")
        board.counter.addListener { _, _, newValue ->
            counterLabel.text = "Step: $newValue"
        }

        val mainPane = BorderPane()
        val hBox = HBox(startStopBtn, nextBtn, skip10Btn, skip100Btn, resetBtn, Label("B:"), redBtn, greenBtn, blueBtn, yellowBtn, whiteBtn, blackBtn, bornTextField, Label("S:"), surviveTextField,
                counterLabel)
        hBox.spacing = 5.0
        hBox.padding = Insets(5.0)
        hBox.alignment = Pos.CENTER_LEFT
        mainPane.center = ScrollPane(board)
        mainPane.top = hBox
        val scene = Scene(mainPane,600.0,300.0)
        stage.scene = scene
        stage.title = "The game of life"
        stage.show()
    }

}

