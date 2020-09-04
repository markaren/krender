package info.laht.krender

import info.laht.krender.proxies.*
import org.joml.Matrix4d
import org.joml.Matrix4dc
import org.joml.Vector3dc
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Closeable


class RenderContext(
    private val engine: RenderEngine
) : Iterable<RenderProxy>, Closeable {

    private val proxies: MutableList<RenderProxy> = mutableListOf()

    @JvmOverloads
    fun createSphere(radius: Float, offset: Matrix4dc? = null): SphereProxy {
        val proxy = engine.createSphere(radius, offset)
        proxies.add(proxy)
        return proxy
    }

    @JvmOverloads
    fun createBox(width: Float, height: Float, depth: Float, offset: Matrix4dc? = null): BoxProxy {
        val proxy = engine.createBox(width, height, depth, offset)
        proxies.add(proxy)
        return proxy
    }

    @JvmOverloads
    fun createCylinder(radius: Float, height: Float, offset: Matrix4dc? = null): CylinderProxy {
        val proxy = engine.createCylinder(radius, height, offset)
        proxies.add(proxy)
        return proxy
    }

    @JvmOverloads
    fun createCapsule(radius: Float, height: Float, offset: Matrix4dc? = null): CapsuleProxy {
        val proxy = engine.createCapsule(radius, height, offset)
        proxies.add(proxy)
        return proxy
    }

    @JvmOverloads
    fun createPlane(height: Float, width: Float, offset: Matrix4dc? = null): PlaneProxy {
        val proxy = engine.createPlane(height, width, offset)
        proxies.add(proxy)
        return proxy
    }

    fun createAxis(size: Float): AxisProxy {
        val proxy = engine.createAxis(size)
        proxies.add(proxy)
        return proxy
    }

    fun createArrow(length: Float): ArrowProxy {
        val proxy = engine.createArrow(length)
        proxies.add(proxy)
        return proxy
    }

    fun createHeightmap(widthSegments: Int, heightSegments: Int, height: Float, width: Float): TerrainProxy {
        val proxy = engine.createHeightmap(widthSegments, heightSegments, height, width)
        proxies.add(proxy)
        return proxy
    }

    fun createCurve(radius: Float, points: List<Vector3dc>): CurveProxy {
        val proxy = engine.createCurve(radius, points)
        proxies.add(proxy)
        return proxy
    }

    fun createMesh(mesh: Trimesh): MeshProxy {
        if (mesh.hasSource()) {
            try {
                return engine.createMesh(mesh.source!!, mesh.scale, Matrix4d()).also {
                    proxies.add(it)
                }
            } catch (ex: Exception) {
                LOG.warn(ex.message)
            }
        }
        return engine.createMesh(mesh).also {
            proxies.add(it)
        }
    }

    override fun iterator(): MutableIterator<RenderProxy> {
        return proxies.iterator()
    }

    override fun close() {
        proxies.forEach { obj -> obj.dispose() }
    }

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(RenderContext::class.java)
    }

}
