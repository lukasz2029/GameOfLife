package lukkon

import javafx.animation.AnimationTimer


class BoardAnimator(private val board: Board) : AnimationTimer() {

    private var lastTime: Long = 0
    var active: Boolean = false
    private set

    override fun handle(now: Long) {
        if (now - lastTime > 100_000_000){
            board.next()
            lastTime = now
        }
    }

    fun startStop(){
        active = if (active){
            stop()
            false
        }else{
            start()
            true
        }
    }

}