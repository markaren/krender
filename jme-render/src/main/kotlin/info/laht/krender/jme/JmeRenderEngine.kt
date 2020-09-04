package info.laht.krender.jme

import com.jme3.app.SimpleApplication
import com.jme3.app.state.AbstractAppState
import com.jme3.input.event.KeyInputEvent
import com.jme3.input.event.MouseButtonEvent
import com.jme3.light.AmbientLight
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import info.laht.krender.RenderEngine
import info.laht.krender.Trimesh
import info.laht.krender.jme.extra.RawInputAdapter
import info.laht.krender.jme.proxy.*
import info.laht.krender.proxies.*
import info.laht.krender.util.ExternalSource
import info.laht.krender.util.InputSource
import org.joml.Matrix4dc
import org.joml.Vector3dc
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


class JmeRenderEngine : RenderEngine {

    private lateinit var renderer: JmeRenderer
    private val parent: Node = Node("parent")

    private val ctx: JmeContext
        get() = renderer.ctx

    private var onCloseCallback: (() -> Unit)? = null

    override fun init() {
        renderer = JmeRenderer(parent).apply {
            start()
        }
    }

    override fun close() {
        renderer.stop()
    }

    override fun onClose(callback: () -> Unit) {
        onCloseCallback = callback
    }

    override fun createMesh(mesh: Trimesh): MeshProxy {
        return JmeMeshProxy(ctx, mesh).also {
            ctx.invokeLater {
                parent.attachChild(it)
            }
        }
    }

    override fun createMesh(source: ExternalSource, scale: Float, offset: Matrix4dc?): MeshProxy {
        return JmeMeshProxy(ctx, source, scale, offset).also {
            ctx.invokeLater {
                parent.attachChild(it)
            }
        }
    }

    override fun createSphere(radius: Float, offset: Matrix4dc?): SphereProxy {
        return JmeSphereProxy(ctx, radius, offset).also {
            ctx.invokeLater {
                parent.attachChild(it)
            }
        }
    }

    override fun createBox(width: Float, height: Float, depth: Float, offset: Matrix4dc?): BoxProxy {
        return JmeBoxProxy(ctx, width, height, depth, offset).also {
            ctx.invokeLater {
                parent.attachChild(it)
            }
        }
    }

    override fun createCylinder(radius: Float, height: Float, offset: Matrix4dc?): CylinderProxy {
        return JmeCylinderProxy(ctx, radius, height, offset).also {
            ctx.invokeLater {
                parent.attachChild(it)
            }
        }
    }

    override fun createCapsule(radius: Float, height: Float, offset: Matrix4dc?): CapsuleProxy {
        return JmeCapsuleProxy(ctx, radius, height, offset).also {
            ctx.invokeLater {
                parent.attachChild(it)
            }
        }
    }

    override fun createPlane(width: Float, height: Float, offset: Matrix4dc?): PlaneProxy {
        return JmePlaneProxy(ctx, width, height, offset).also {
            ctx.invokeLater {
                parent.attachChild(it)
            }
        }
    }

    override fun createAxis(size: Float): AxisProxy {
        return JmeAxisProxy(ctx, size).also {
            ctx.invokeLater {
                parent.attachChild(it)
            }
        }
    }

    override fun createHeightmap(widthSegments: Int, heightSegments: Int, width: Float, height: Float): TerrainProxy {
        TODO("Not yet implemented")
    }

    override fun createCurve(radius: Float, points: List<Vector3dc>): CurveProxy {
        TODO("Not yet implemented")
    }

    override fun createArrow(length: Float): ArrowProxy {
        TODO("Not yet implemented")
    }

    override fun createPointCloud(pointSize: Float, points: List<Vector3dc>): PointCloudProxy {
        TODO("Not yet implemented")
    }

    override fun createWater(width: Float, height: Float): WaterProxy {
        TODO("Not yet implemented")
    }

    private inner class JmeRenderer(
        private val parent: Node
    ) : SimpleApplication() {

        private val lock = ReentrantLock()
        private var initialized = lock.newCondition()

        lateinit var ctx: JmeContext

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
                    onCloseCallback?.invoke()
                }
            })

            rootNode.attachChild(parent)

            ctx = JmeContext(assetManager)

            lock.withLock {
                initialized.signalAll()
            }

        }

        override fun simpleUpdate(tpf: Float) {
            ctx.invokePendingTasks()
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
