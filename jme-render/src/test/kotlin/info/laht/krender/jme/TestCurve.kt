package info.laht.krender.jme

import info.laht.krender.ColorConstants
import org.joml.Vector3d

fun main() {

    val points = listOf(
        Vector3d(0.0, 0.0, 0.0),
        Vector3d(1.0, 0.0, 0.0),
        Vector3d(1.0, 2.0, 0.0),
        Vector3d(1.0, 2.0, 5.0),
    )

    val renderEngine = JmeRenderEngine().apply { init() }
    val curve = renderEngine.createCurve(0.1f, points)

    val arrow = renderEngine.createArrow(1f).apply {
        setColor(ColorConstants.yellow)
    }

    Thread.sleep(1000)

    curve.setColor(ColorConstants.blue)
    curve.setWireframe(true)

}
