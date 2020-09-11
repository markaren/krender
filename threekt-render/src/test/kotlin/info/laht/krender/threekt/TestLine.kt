package info.laht.krender.threekt

import info.laht.krender.ColorConstants
import org.joml.Matrix4f
import org.joml.Vector3f

fun main() {

    val points = listOf(
        Vector3f(0f, 0f, 0f),
        Vector3f(1f, 0f, 0f),
        Vector3f(1f, 2f, 0f),
        Vector3f(1f, 2f, 5f),
    )

    ThreektRenderer().apply {

        init(Matrix4f().setTranslation(0f, 0f, 5f))
        setBackGroundColor(ColorConstants.aliceblue)

        val curve = createLine(points).apply {
            setColor(ColorConstants.green)
        }

        Thread.sleep(1000)

        curve.update(
            listOf(
                Vector3f(0f, 0f, 0f),
                Vector3f(-1f, 0f, 0f),
                Vector3f(-1f, 2f, 0f),
                Vector3f(-1f, 2f, 5f),
            )
        )
    }

}
