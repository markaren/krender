package info.laht.krender.proxies

import info.laht.krender.util.ExternalSource
import org.joml.Matrix4dc
import org.joml.Quaterniondc
import org.joml.Vector3dc
import java.awt.Color

interface ArrowProxy : RenderProxy, ColorProxy, SpatialProxy {
    fun setLength(length: Float)
}

interface AxisProxy : RenderProxy, SpatialProxy {
    fun setSize(size: Float)
}

interface BoxProxy : RenderProxy, ColorProxy, SpatialProxy, WireframeProxy, TextureProxy

interface CapsuleProxy : RenderProxy, ColorProxy, SpatialProxy, WireframeProxy, TextureProxy {
    fun setRadius(radius: Float)
    fun setHeight(height: Float)
}

interface ColorProxy {
    fun setColor(color: Color)
}

interface CurveProxy : RenderProxy, ColorProxy, WireframeProxy {
    fun update(points: List<Vector3dc>)
}

interface CylinderProxy : RenderProxy, ColorProxy, SpatialProxy, WireframeProxy, TextureProxy {
    fun setRadius(radius: Float)
    fun setHeight(height: Float)
}

interface RenderProxy {
    fun setVisible(visible: Boolean)
    fun dispose()
}

interface SpatialProxy {
    fun setTranslate(v: Vector3dc)
    fun setRotate(q: Quaterniondc)
    fun setTransform(m: Matrix4dc)
    fun setOffsetTransform(offset: Matrix4dc)
}

interface SphereProxy : RenderProxy, ColorProxy, SpatialProxy, WireframeProxy, TextureProxy {
    fun setRadius(radius: Float)
}

interface TerrainProxy : RenderProxy, SpatialProxy, WireframeProxy, ColorProxy, TextureProxy {
    fun setHeights(heights: FloatArray)
}

interface TextureProxy {
    fun setTexture(source: ExternalSource)
}

interface WaterProxy : RenderProxy, WireframeProxy {
    fun setTranslate(v: Vector3dc)
}

interface WireframeProxy {
    fun setWireframe(flag: Boolean)
}

interface MeshProxy : RenderProxy, WireframeProxy, SpatialProxy

interface PlaneProxy : RenderProxy, ColorProxy, SpatialProxy, WireframeProxy

interface PointCloudProxy
