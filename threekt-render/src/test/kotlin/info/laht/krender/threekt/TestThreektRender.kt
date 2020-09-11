package info.laht.krender.threekt

import info.laht.krender.ColorConstants
import info.laht.krender.util.FileSource
import org.joml.Matrix4f
import org.joml.Vector3f
import java.io.File

private class Dummy

fun main() {

    val tex = File(Dummy::class.java.classLoader.getResource("textures/checker.png")!!.file)

    ThreektRenderer().apply {

        init(Matrix4f().setTranslation(0f, 0f, -5f))
        setBackGroundColor(ColorConstants.gray)

        val c = createCylinder(0.5f, 1f).apply {
            setTexture(FileSource(tex))
            setOffsetTransform(Matrix4f().translate(0f, 0f, 0f))
            setHeight(2f)
        }

        val s = createSphere(0.5f).apply {
            setColor(ColorConstants.red)
            setWireframe(true)
            setOffsetTransform(Matrix4f().translate(0f, -2f, 0f))
        }

        val b = createBox(1f, 1f, 1f).apply {
            setColor(ColorConstants.blue)
            setTexture(FileSource(tex))
            setOffsetTransform(Matrix4f().translate(Vector3f(-2f, 0f, 0f)))
        }

        var stop = false
        Thread {

            val transform = Matrix4f()

            while (!stop) {

                transform.rotate(0.1f * 0.1f, Vector3f(0f, 1f, 0f))
                s.setTransform(transform)
                b.setTransform(transform)
                Thread.sleep(10)
            }

        }.start()

        registerCloseListener { stop = true }

    }

}
