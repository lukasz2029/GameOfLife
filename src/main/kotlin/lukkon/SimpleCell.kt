package lukkon

import javafx.scene.paint.Color

class SimpleCell: Cell() {
    var color = Color.GRAY!!
    override fun updateView() {
        color = if (state) Color.YELLOW else Color.GRAY
    }
}