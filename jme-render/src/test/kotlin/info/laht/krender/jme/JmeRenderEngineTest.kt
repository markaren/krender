package info.laht.krender.jme

import info.laht.krender.loaders.ObjLoader
import info.laht.krender.util.FileSource
import org.joml.Matrix4d
import org.joml.Vector3d
import java.awt.Color
import java.io.File


object JmeRenderEngineTest {

    @JvmStatic
    fun main(args: Array<String>) {

        val engine = JmeRenderEngine().apply { init() }

        val sphere = engine.createSphere(0.1f).apply {
            setColor(Color.BLACK)
        }

        val source = FileSource(File(JmeRenderEngineTest::class.java.classLoader.getResource("obj/bunny.obj")!!.file))
        val load = ObjLoader().load(source).apply {
            computeVertexNormals()
            scale(10f)
        }
        val bunny = engine.createMesh(load)

        Thread.sleep(1000)

        sphere.setColor(Color.BLUE)
        bunny.setWireframe(true)

        var stop = false
        Thread {

            val transform = Matrix4d()

            while (!stop) {

                transform.rotate(0.1 * 0.1, Vector3d(0.0,1.0,0.0))
                bunny.setTransform(transform)
                Thread.sleep(10)
            }

        }.start()

        engine.onClose {
            stop = true
        }

    }

}
