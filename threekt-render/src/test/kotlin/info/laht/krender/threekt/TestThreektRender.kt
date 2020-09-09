package info.laht.krender.threekt

import org.joml.Matrix4d
import org.joml.Vector3d
import java.awt.Color

fun main() {

    ThreektRenderer().apply {
        init()

        val s = createSphere(0.5f).apply {
            setColor(Color.red)
            setWireframe(true)
            setOffsetTransform(Matrix4d().translate(0.0, 0.0, 0.0))
        }

        val b = createBox(0.5f, 0.5f, 0.5f).apply {
            setColor(Color.blue)
            setOffsetTransform(Matrix4d().translate(Vector3d(-2.0, 0.0, 0.0)))
        }

        var stop = false
        Thread {

            val transform = Matrix4d()

            while (!stop) {

                transform.rotate(0.1 * 0.1, Vector3d(0.0, 1.0, 0.0))
                s.setTransform(transform)
                b.setTransform(transform)
                Thread.sleep(10)
            }

        }.start()

        onClose { stop = true }

    }

}
