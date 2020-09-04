package info.laht.krender.jme

import info.laht.krender.util.FileSource
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
        engine.createMesh(source, 10f)

        Thread.sleep(1000)

        sphere.setColor(Color.BLUE)

        engine.close()

    }

}
