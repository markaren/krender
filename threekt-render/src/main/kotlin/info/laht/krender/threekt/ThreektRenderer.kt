package info.laht.krender.threekt

import info.laht.krender.RenderEngine
import info.laht.krender.mesh.Trimesh
import info.laht.krender.proxies.*
import info.laht.krender.util.ExternalSource
import info.laht.krender.util.RenderContext
import info.laht.threekt.Window
import info.laht.threekt.cameras.PerspectiveCamera
import info.laht.threekt.controls.OrbitControls
import info.laht.threekt.math.Color
import info.laht.threekt.renderers.GLRenderer
import info.laht.threekt.scenes.Scene
import org.joml.Matrix4dc
import org.joml.Vector3dc
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ThreektRenderer : RenderEngine {

    private val mutex: Unit = Unit
    private var onCloseCallback: (() -> Unit)? = null

    private val scene: Scene = Scene().apply {
        setBackground(Color.aliceblue)
    }
    private val ctx: RenderContext = RenderContext()

    private var internalRenderer: InternalRenderer? = null

    private val lock = ReentrantLock()
    private var initialized = lock.newCondition()

    override fun init() {
        internalRenderer = InternalRenderer()
        val t = Thread(internalRenderer).apply { start() }
        lock.withLock {
            initialized.await()
        }
    }

    override fun createAxis(size: Float): AxisProxy {
        TODO("Not yet implemented")
    }

    override fun createArrow(length: Float): ArrowProxy {
        TODO("Not yet implemented")
    }

    override fun createMesh(mesh: Trimesh): MeshProxy {
        TODO("Not yet implemented")
    }

    override fun createMesh(source: ExternalSource, scale: Float, offset: Matrix4dc?): MeshProxy {
        TODO("Not yet implemented")
    }

    override fun createSphere(radius: Float, offset: Matrix4dc?): SphereProxy {
        return ThreektSphereProxy(ctx, radius).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createPlane(width: Float, height: Float, offset: Matrix4dc?): PlaneProxy {
        TODO("Not yet implemented")
    }

    override fun createBox(width: Float, height: Float, depth: Float, offset: Matrix4dc?): BoxProxy {
        return ThreektBoxProxy(ctx, width, height, depth).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createCylinder(radius: Float, height: Float, offset: Matrix4dc?): CylinderProxy {
        TODO("Not yet implemented")
    }

    override fun createCapsule(radius: Float, height: Float, offset: Matrix4dc?): CapsuleProxy {
        TODO("Not yet implemented")
    }

    override fun createCurve(radius: Float, points: List<Vector3dc>): CurveProxy {
        TODO("Not yet implemented")
    }

    override fun createHeightmap(widthSegments: Int, heightSegments: Int, width: Float, height: Float): TerrainProxy {
        TODO("Not yet implemented")
    }

    override fun createWater(width: Float, height: Float): WaterProxy {
        TODO("Not yet implemented")
    }

    override fun createPointCloud(pointSize: Float, points: List<Vector3dc>): PointCloudProxy {
        TODO("Not yet implemented")
    }

    override fun onClose(callback: () -> Unit) {
        onCloseCallback = callback
    }

    override fun close() {
        //internalRenderer?.window?.close()
    }

    private inner class InternalRenderer : Runnable {

        override fun run() {

            Window().use { window ->

                lock.withLock {
                    initialized.signalAll()
                }

                val renderer = GLRenderer(window.size)
                val camera = PerspectiveCamera(75, window.aspect, 0.1, 1000).apply {
                    position.z = 5f
                }
                OrbitControls(camera, window)

                window.animate {

                    ctx.invokePendingTasks()
                    renderer.render(scene, camera)

                }

            }

            onCloseCallback?.invoke()

        }

    }

}
