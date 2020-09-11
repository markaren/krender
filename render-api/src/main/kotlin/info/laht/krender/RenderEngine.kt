package info.laht.krender

import info.laht.krender.listeners.CloseListener
import info.laht.krender.listeners.KeyListener
import info.laht.krender.listeners.MouseClickListener
import info.laht.krender.mesh.TrimeshShape
import info.laht.krender.proxies.*
import org.joml.Matrix4fc
import org.joml.Vector3fc
import java.io.Closeable
import java.io.File

interface RenderEngine : Closeable {

    fun init(cameraTransform: Matrix4fc? = null)

    fun setBackGroundColor(color: Int)

    fun createAxis(size: Float): AxisProxy
    fun createArrow(length: Float): ArrowProxy

    fun createMesh(mesh: TrimeshShape): MeshProxy
    fun createMesh(source: File, scale: Float = 1f, offset: Matrix4fc? = null): MeshProxy

    fun createSphere(radius: Float, offset: Matrix4fc? = null): SphereProxy
    fun createPlane(width: Float, height: Float, offset: Matrix4fc? = null): PlaneProxy
    fun createBox(width: Float, height: Float, depth: Float, offset: Matrix4fc? = null): BoxProxy
    fun createCylinder(radius: Float, height: Float, offset: Matrix4fc? = null): CylinderProxy
    fun createCapsule(radius: Float, height: Float, offset: Matrix4fc? = null): CapsuleProxy

    fun createLine(points: List<Vector3fc>): LineProxy
    fun createCurve(radius: Float, points: List<Vector3fc>): CurveProxy
    fun createHeightmap(widthSegments: Int, heightSegments: Int, width: Float, height: Float): HeightmapProxy

    fun createWater(width: Float, height: Float): WaterProxy
    fun createPointCloud(pointSize: Float, points: List<Vector3fc>): PointCloudProxy

    fun registerKeyListener(listener: KeyListener)
    fun registerClickListener(listener: MouseClickListener)

    fun registerCloseListener(listener: CloseListener)

}

abstract class AbstractRenderEngine : RenderEngine {

    protected var keyListener: KeyListener? = null
    protected var closeListener: CloseListener? = null
    protected var mouseClickListener: MouseClickListener? = null

    override fun registerKeyListener(listener: KeyListener) {
        this.keyListener = listener
    }

    override fun registerCloseListener(listener: CloseListener) {
        this.closeListener = listener
    }

    override fun registerClickListener(listener: MouseClickListener) {
        this.mouseClickListener = listener
    }

}

