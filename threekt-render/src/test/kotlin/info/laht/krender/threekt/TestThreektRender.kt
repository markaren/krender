package info.laht.krender.threekt

import info.laht.krender.util.FileSource
import org.joml.Matrix4d
import org.joml.Vector3d
import java.awt.Color
import java.io.File

private class Dummy

fun main() {

    val tex = File(Dummy::class.java.classLoader.getResource("textures/checker.png")!!.file)

    ThreektRenderer().apply {
        init()

        val c = createCylinder(0.5f, 1f).apply {
            setTexture(FileSource(tex))
            setOffsetTransform(Matrix4d().translate(0.0, 0.0, 0.0))
            setHeight(2f)
        }

        val s = createSphere(0.5f).apply {
            setColor(Color.red)
            setWireframe(true)
            setOffsetTransform(Matrix4d().translate(0.0, -2.0, 0.0))
        }

        val b = createBox(1f, 1f, 1f).apply {
            setColor(Color.blue)
            setTexture(FileSource(tex))
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
