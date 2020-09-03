package info.laht.krender

import info.laht.krender.proxies.*
import info.laht.krender.util.InputSource
import org.joml.Matrix4dc
import org.joml.Vector3dc

interface RenderEngine {

    fun init()
    fun shutdown()

    fun createMesh(mesh: Trimesh): MeshProxy
    fun createMesh(source: InputSource, scale: Double, offset: Matrix4dc?): MeshProxy?
    fun createSphere(radius: Double, offset: Matrix4dc?): SphereProxy
    fun createBox(width: Double, height: Double, depth: Double, offset: Matrix4dc?): BoxProxy
    fun createCylinder(radius: Double, height: Double, offset: Matrix4dc?): CylinderProxy
    fun createCapsule(radius: Double, height: Double, offset: Matrix4dc?): CapsuleProxy
    fun createPlane(width: Double, height: Double, offset: Matrix4dc?): PlaneProxy
    fun createAxis(size: Double): AxisProxy
    fun createHeightmap(widthSegments: Int, heightSegments: Int, width: Double, height: Double): TerrainProxy
    fun createCurve(radius: Double, points: List<Vector3dc>): CurveProxy
    fun createArrow(length: Double): ArrowProxy
    fun createPointCloud(pointSize: Double, points: List<Vector3dc>): PointCloudProxy

    /*fun registerKeyListener(consumer: KeyConsumer?)
    fun registerClickListener(consumer: ClickConsumer?)*/

}
