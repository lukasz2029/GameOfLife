package lukkon

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class SimpleCell: Cell() {

    var color= Color.YELLOW!!

    fun draw(gc: GraphicsContext, cellSize: Double, spacing: Double, arcSize: Double) {
        gc.fill = if (state) color else Color.GRAY
        gc.fillRoundRect(
                location.x*(cellSize + spacing) + spacing,
                location.y*(cellSize + spacing) + spacing,
                cellSize, cellSize, arcSize, arcSize)
    }

}