package info.laht.krender.jme

import info.laht.krender.ColorConstants
import info.laht.krender.loaders.ObjLoader
import info.laht.krender.util.FileSource
import org.joml.Matrix4f
import org.joml.Vector3f
import java.io.File


object JmeRenderEngineTest {

    @JvmStatic
    fun main(args: Array<String>) {

        JmeRenderEngine().apply {

            init()
            setBackGroundColor(0xF0F8FF)

            val sphere = createSphere(0.1f).apply {
                setColor(ColorConstants.black)
            }

            val source =
                FileSource(File(JmeRenderEngineTest::class.java.classLoader.getResource("obj/bunny.obj")!!.file))
            val load = ObjLoader().load(source).apply {
                computeVertexNormals()
                scale(10f)
            }
            val bunny = createMesh(load).apply {
                setOffsetTransform(Matrix4f().setTranslation(2f, 0f, 0f))
            }

            Thread.sleep(1000)

            sphere.setColor(ColorConstants.blue)
            bunny.setWireframe(true)

            var stop = false
            Thread {

                val transform = Matrix4f()

                while (!stop) {

                    transform.rotate(0.1f * 0.1f, Vector3f(0f, 1f, 0f))
                    bunny.setTransform(transform)
                    Thread.sleep(10)
                }

            }.start()

            registerCloseListener {
                stop = true
            }

        }

    }

}
