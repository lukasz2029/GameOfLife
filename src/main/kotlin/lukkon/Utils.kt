package lukkon

import javafx.scene.layout.GridPane
import java.awt.Point

object Utils {

    fun getCellArray(width: Int, height: Int): Array<Array<Cell>>{
        val cellArray = Array(width) { _ -> Array(height) { Cell() } }
        for (y in 0 until height) {
            for (x in 0 until width) {
                val cell = Cell()
                cellArray[x][y] = cell
                cell.point2D = Point(x, y)
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
        }
        return cellArray
    }

    fun getGridPane(cellArray: Array<Array<Cell>>): GridPane{
        val gridPane = GridPane()
        cellArray.forEachIndexed { x, row ->
            row.forEachIndexed { y, cell ->
                gridPane.add(cell.component, x, y)
            }
        }
        return gridPane
    }

}