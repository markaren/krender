package info.laht.krender

import info.laht.krender.proxies.*
import org.joml.Matrix4dc
import org.joml.Vector3dc
import java.io.Closeable


class RenderContext(
    private val engine: RenderEngine
) : Iterable<RenderProxy>, Closeable {

    private val proxies: MutableList<RenderProxy> = mutableListOf()

    @JvmOverloads
    fun createSphere(radius: Double, offset: Matrix4dc? = null): SphereProxy {
        val proxy = engine.createSphere(radius, offset)
        proxies.add(proxy)
        return proxy
    }

    @JvmOverloads
    fun createBox(width: Double, height: Double, depth: Double, offset: Matrix4dc? = null): BoxProxy {
        val proxy = engine.createBox(width, height, depth, offset)
        proxies.add(proxy)
        return proxy
    }

    @JvmOverloads
    fun createCylinder(radius: Double, height: Double, offset: Matrix4dc? = null): CylinderProxy {
        val proxy = engine.createCylinder(radius, height, offset)
        proxies.add(proxy)
        return proxy
    }

    @JvmOverloads
    fun createCapsule(radius: Double, height: Double, offset: Matrix4dc? = null): CapsuleProxy {
        val proxy = engine.createCapsule(radius, height, offset)
        proxies.add(proxy)
        return proxy
    }

    @JvmOverloads
    fun createPlane(height: Double, width: Double, offset: Matrix4dc? = null): PlaneProxy {
        val proxy = engine.createPlane(height, width, offset)
        proxies.add(proxy)
        return proxy
    }

    fun createAxis(size: Double): AxisProxy {
        val proxy = engine.createAxis(size)
        proxies.add(proxy)
        return proxy
    }

    fun createArrow(length: Double): ArrowProxy {
        val proxy = engine.createArrow(length)
        proxies.add(proxy)
        return proxy
    }

    fun createHeightmap(widthSegments: Int, heightSegments: Int, height: Double, width: Double): TerrainProxy {
        val proxy = engine.createHeightmap(widthSegments, heightSegments, height, width)
        proxies.add(proxy)
        return proxy
    }

    fun createCurve(radius: Double, points: List<Vector3dc>): CurveProxy {
        val proxy = engine.createCurve(radius, points)
        proxies.add(proxy)
        return proxy
    }

    /*fun createMesh(mesh: Trimesh): MeshProxy? {
        if (mesh.hasSource()) {
            try {
                val proxy = engine.createMesh(mesh.getSource(), mesh.getScale(), mesh.getOffsetTransform())
                proxies.add(proxy!!)
                return proxy
            } catch (ex: Exception) {
                Logger.getLogger(RenderContext::class.java.name).log(Level.SEVERE, null, ex)
            }
        }
        val proxy = engine.createMesh(
            MeshData.Builder()
                .indices(mesh.getIndices())
                .vertices(mesh.getVertices())
                .normals(mesh.getNormals())
                .colors(mesh.getColors())
                .uvs(mesh.getUvs())
                .build()
        )
        proxies.add(proxy)
        return proxy
    }*/

    override fun iterator(): MutableIterator<RenderProxy> {
        return proxies.iterator()
    }

    override fun close() {
        proxies.forEach { obj -> obj.dispose() }
    }

}
