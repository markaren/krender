package info.laht.krender.jme

import com.jme3.app.SimpleApplication
import com.jme3.app.state.AbstractAppState
import com.jme3.input.event.KeyInputEvent
import com.jme3.input.event.MouseButtonEvent
import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import info.laht.krender.RenderEngine
import info.laht.krender.Trimesh
import info.laht.krender.proxies.*
import info.laht.krender.util.InputSource
import org.joml.Matrix4dc
import org.joml.Vector3dc
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


class JmeRenderEngine : RenderEngine {

    override fun init() {
        TODO("Not yet implemented")
    }

    override fun createMesh(mesh: Trimesh): MeshProxy {
        TODO("Not yet implemented")
    }

    override fun createMesh(source: InputSource, scale: Double, offset: Matrix4dc?): MeshProxy? {
        TODO("Not yet implemented")
    }

    override fun createSphere(radius: Double, offset: Matrix4dc?): SphereProxy {
        TODO("Not yet implemented")
    }

    override fun createBox(width: Double, height: Double, depth: Double, offset: Matrix4dc?): BoxProxy {
        TODO("Not yet implemented")
    }

    override fun createCylinder(radius: Double, height: Double, offset: Matrix4dc?): CylinderProxy {
        TODO("Not yet implemented")
    }

    override fun createCapsule(radius: Double, height: Double, offset: Matrix4dc?): CapsuleProxy {
        TODO("Not yet implemented")
    }

    override fun createPlane(width: Double, height: Double, offset: Matrix4dc?): PlaneProxy {
        TODO("Not yet implemented")
    }

    override fun createAxis(size: Double): AxisProxy {
        TODO("Not yet implemented")
    }

    override fun createHeightmap(widthSegments: Int, heightSegments: Int, width: Double, height: Double): TerrainProxy {
        TODO("Not yet implemented")
    }

    override fun createCurve(radius: Double, points: List<Vector3dc>): CurveProxy {
        TODO("Not yet implemented")
    }

    override fun createArrow(length: Double): ArrowProxy {
        TODO("Not yet implemented")
    }

    override fun createPointCloud(pointSize: Double, points: List<Vector3dc>): PointCloudProxy {
        TODO("Not yet implemented")
    }

    override fun shutdown() {
        TODO("Not yet implemented")
    }

    private class JmeRenderer : SimpleApplication() {

        private val lock = ReentrantLock()
        private var initialized = lock.newCondition()

        override fun start() {
            super.start()
            lock.withLock {
                initialized.await()
            }
        }

        override fun simpleInitApp() {

            super.setPauseOnLostFocus(false)


            super.flyCam.isDragToRotate = true
            super.flyCam.moveSpeed = 10f

            super.inputManager.addRawInputListener(InputListener())
            super.viewPort.backgroundColor.set(0.6f, 0.7f, 1f, 1f)

            setupLights()

            super.stateManager.attach(object : AbstractAppState() {
                override fun cleanup() {
                    //TODO
                }
            })

            lock.withLock {
                initialized.signalAll()
            }

        }

        private fun setupLights() {

            DirectionalLight().apply {
                color = ColorRGBA.White.mult(0.8f)
                direction = Vector3f(-0.5f, -0.5f, -0.5f).normalizeLocal()
                rootNode.addLight(this)
            }

            DirectionalLight().apply {
                color = ColorRGBA.White.mult(0.8f)
                direction = Vector3f(0.5f, 0.5f, 0.5f).normalizeLocal()
                rootNode.addLight(this)
            }

            AmbientLight().apply {
                color = ColorRGBA.White.mult(0.3f)
                rootNode.addLight(this)
            }

        }

        private inner class InputListener : RawInputAdapter() {

            override fun onMouseButtonEvent(evt: MouseButtonEvent) {
                /*clickListener?.also {
                    val x = evt.x.toFloat()
                    val y = evt.y.toFloat()
                    val worldCoordinates = cam.getWorldCoordinates(Vector2f(x, y), 1000f)
                    val pos = Vector3d().set(cam.location)
                    val dir = Vector3d().set(Vector3f(cam.location).subtract(worldCoordinates).normalizeLocal())
                    val m = Matrix4f().rotateX(FastMath.PI / 2)
                    pos.mulPosition(m)
                    dir.mulDirection(m)
                    it.onMousePressed(pos, dir)
                }*/
            }

            override fun onKeyEvent(evt: KeyInputEvent) {
                /* if (evt.isPressed) {
                     val keyCode = evt.keyCode
                     keyListener?.also {
                         it.onKeyPressed(keyCode.toKeyStoke())
                     }
                     when (keyCode) {
                         KeyInput.KEY_F1 -> {
                             //showCollisionGeometries = !showCollisionGeometries
                         }
                         KeyInput.KEY_E -> engine.registerKeyPress(KeyStroke.KEY_E)
                         KeyInput.KEY_R -> engine.registerKeyPress(KeyStroke.KEY_R)
                         KeyInput.KEY_W -> engine.registerKeyPress(KeyStroke.KEY_W)
                         KeyInput.KEY_A -> engine.registerKeyPress(KeyStroke.KEY_A)
                         KeyInput.KEY_S -> engine.registerKeyPress(KeyStroke.KEY_S)
                         KeyInput.KEY_D -> engine.registerKeyPress(KeyStroke.KEY_D)
                     }
                 }*/
            }

        }

    }

}
