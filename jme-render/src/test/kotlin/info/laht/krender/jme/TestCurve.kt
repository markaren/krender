package info.laht.krender.jme

import org.joml.Vector3d
import java.awt.Color

fun main() {

    val points = listOf(
        Vector3d(0.0, 0.0, 0.0),
        Vector3d(1.0, 0.0, 0.0),
        Vector3d(1.0, 2.0, 0.0),
        Vector3d(1.0, 2.0, 5.0),
    )

    val renderEngine = JmeRenderEngine().apply { init() }
    val curve = renderEngine.createCurve(0.1f, points)

    Thread.sleep(1000)

    curve.setColor(Color.blue)
    curve.setWireframe(true)

}
