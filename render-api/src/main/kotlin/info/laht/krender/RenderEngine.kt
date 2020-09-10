package info.laht.krender

import info.laht.krender.listeners.CloseListener
import info.laht.krender.listeners.KeyListener
import info.laht.krender.listeners.MouseClickListener
import info.laht.krender.mesh.Trimesh
import info.laht.krender.proxies.*
import info.laht.krender.util.ExternalSource
import org.joml.Matrix4dc
import org.joml.Vector3dc
import java.io.Closeable

interface RenderEngine : Closeable {

    fun init()

    fun setBackGroundColor(color: Int)

    fun createAxis(size: Float): AxisProxy
    fun createArrow(length: Float): ArrowProxy

    fun createMesh(mesh: Trimesh): MeshProxy
    fun createMesh(source: ExternalSource, scale: Float = 1f, offset: Matrix4dc? = null): MeshProxy

    fun createSphere(radius: Float, offset: Matrix4dc? = null): SphereProxy
    fun createPlane(width: Float, height: Float, offset: Matrix4dc? = null): PlaneProxy
    fun createBox(width: Float, height: Float, depth: Float, offset: Matrix4dc? = null): BoxProxy
    fun createCylinder(radius: Float, height: Float, offset: Matrix4dc? = null): CylinderProxy
    fun createCapsule(radius: Float, height: Float, offset: Matrix4dc? = null): CapsuleProxy

    fun createLine(points: List<Vector3dc>): LineProxy
    fun createCurve(radius: Float, points: List<Vector3dc>): CurveProxy
    fun createHeightmap(widthSegments: Int, heightSegments: Int, width: Float, height: Float): HeightmapProxy

    fun createWater(width: Float, height: Float): WaterProxy
    fun createPointCloud(pointSize: Float, points: List<Vector3dc>): PointCloudProxy

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

