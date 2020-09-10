package info.laht.krender.threekt

import org.joml.Vector3d
import java.awt.Color

fun main() {

    val points = listOf(
        Vector3d(0.0, 0.0, 0.0),
        Vector3d(1.0, 0.0, 0.0),
        Vector3d(1.0, 2.0, 0.0),
        Vector3d(1.0, 2.0, 5.0),
    )

    val renderEngine = ThreektRenderer().apply { init() }
    val curve = renderEngine.createLine(points).apply {
        setColor(Color.green)
    }

    Thread.sleep(1000)

    curve.update(
        listOf(
            Vector3d(0.0, 0.0, 0.0),
            Vector3d(-1.0, 0.0, 0.0),
            Vector3d(-1.0, 2.0, 0.0),
            Vector3d(-1.0, 2.0, 5.0),
        )
    )


}
