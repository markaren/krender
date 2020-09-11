package info.laht.krender.threekt

import info.laht.krender.AbstractRenderEngine
import info.laht.krender.ColorConstants
import info.laht.krender.mesh.TrimeshShape
import info.laht.krender.proxies.*
import info.laht.krender.util.RenderContext
import info.laht.threekt.Window
import info.laht.threekt.cameras.PerspectiveCamera
import info.laht.threekt.controls.OrbitControls
import info.laht.threekt.core.Clock
import info.laht.threekt.input.KeyAction
import info.laht.threekt.math.Matrix4
import info.laht.threekt.renderers.GLRenderer
import info.laht.threekt.scenes.Scene
import org.joml.Matrix4fc
import org.joml.Vector3fc
import java.io.File
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ThreektRenderer : AbstractRenderEngine() {

    private val ctx: RenderContext = RenderContext()

    private val scene: Scene = Scene().apply {
        setBackground(ColorConstants.aliceblue)
    }
    private var internalRenderer: InternalRenderer? = null

    private var water: ThreektWaterProxy? = null

    private val lock = ReentrantLock()
    private var initialized = lock.newCondition()

    override fun init(cameraTransform: Matrix4fc?) {
        internalRenderer = InternalRenderer(cameraTransform?.let { Matrix4().set(it) })
        Thread(internalRenderer).apply { start() }
        lock.withLock {
            initialized.await()
        }
    }

    override fun setBackGroundColor(color: Int) {
        scene.setBackground(color)
    }

    override fun createAxis(size: Float): AxisProxy {
        TODO("Not yet implemented")
    }

    override fun createArrow(length: Float): ArrowProxy {
        TODO("Not yet implemented")
    }

    override fun createMesh(mesh: TrimeshShape): MeshProxy {
        return ThreektTrimeshProxy(ctx, mesh).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createMesh(source: File, scale: Float, offset: Matrix4fc?): MeshProxy {
        return ThreektTrimeshProxy(ctx, source, scale).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createSphere(radius: Float, offset: Matrix4fc?): SphereProxy {
        return ThreektSphereProxy(ctx, radius).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createPlane(width: Float, height: Float, offset: Matrix4fc?): PlaneProxy {
        return ThreektPlaneProxy(ctx, width, height).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createBox(width: Float, height: Float, depth: Float, offset: Matrix4fc?): BoxProxy {
        return ThreektBoxProxy(ctx, width, height, depth).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createCylinder(radius: Float, height: Float, offset: Matrix4fc?): CylinderProxy {
        return ThreektCylinderProxy(ctx, radius, height).also {
            offset?.also { offset -> it.setOffsetTransform(offset) }
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createCapsule(radius: Float, height: Float, offset: Matrix4fc?): CapsuleProxy {
        TODO("Not yet implemented")
    }

    override fun createCurve(radius: Float, points: List<Vector3fc>): CurveProxy {
        return ThreektCurveProxy(ctx, radius, points).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createLine(points: List<Vector3fc>): LineProxy {
        return ThreektLineProxy(ctx, points).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun createHeightmap(widthSegments: Int, heightSegments: Int, width: Float, height: Float): HeightmapProxy {
        TODO("Not yet implemented")
    }

    override fun createWater(width: Float, height: Float): WaterProxy {
        return ThreektWaterProxy(ctx, width, height).also {
            water = it
            scene.add(it.parentNode)
        }
    }

    override fun createPointCloud(pointSize: Float, points: List<Vector3fc>): PointCloudProxy {
        return ThreektPointCloudProxy(ctx, pointSize, points).also {
            ctx.invokeLater {
                scene.add(it.parentNode)
            }
        }
    }

    override fun close() {

    }

    private inner class InternalRenderer(
        private val cameraTransform: Matrix4?
    ) : Runnable {

        override fun run() {

            Window(
                antialias = 4,
                resizeable = true
            ).use { window ->

                lock.withLock {
                    initialized.signalAll()
                }

                val renderer = GLRenderer(window.size)
                val camera = PerspectiveCamera(75, window.aspect, 0.1, 1000)
                cameraTransform?.also {
                    camera.position.setFromMatrixPosition(it)
                    camera.quaternion.setFromRotationMatrix(it)
                }
                OrbitControls(camera, window)

                window.onWindowResize { width, height ->
                    camera.aspect = window.aspect
                    camera.updateProjectionMatrix()
                    renderer.setSize(width, height)
                }

                window.addKeyListener {
                    if (it.action == KeyAction.PRESS) {
                        keyListener?.onKeyPressed(it.keyCode)
                    }
                }

                val clock = Clock()
                window.animate {
                    ctx.invokePendingTasks()

                    water?.water?.apply {
                        val wTime = uniforms["time"]!!.value as Float
                        uniforms["time"]!!.value = wTime + (0.2f * clock.getDelta())
                    }

                    renderer.render(scene, camera)
                }

            }

            closeListener?.onClose()

        }

    }

}
