package info.laht.krender.jme

import info.laht.krender.RenderContext
import java.awt.Color


fun main() {

    val engine = JmeRenderEngine().apply { init() }
    val ctx = RenderContext(engine)

    val sphere = ctx.createSphere(1.0).apply {
        setColor(Color.BLACK)
    }

    Thread.sleep(1000)

    sphere.setColor(Color.BLUE)

    Thread.sleep(1000)

    engine.close()

}
