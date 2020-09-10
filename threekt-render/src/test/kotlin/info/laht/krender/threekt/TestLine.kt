package info.laht.krender.threekt

import info.laht.krender.ColorConstants
import org.joml.Matrix4d
import org.joml.Vector3d

fun main() {

    val points = listOf(
        Vector3d(0.0, 0.0, 0.0),
        Vector3d(1.0, 0.0, 0.0),
        Vector3d(1.0, 2.0, 0.0),
        Vector3d(1.0, 2.0, 5.0),
    )

    val renderEngine = ThreektRenderer().apply {

        init(Matrix4d().setTranslation(0.0, 0.0, -5.0))
        setBackGroundColor(ColorConstants.aliceblue)

        val curve = createLine(points).apply {
            setColor(ColorConstants.green)
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

}
