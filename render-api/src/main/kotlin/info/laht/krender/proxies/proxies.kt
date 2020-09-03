package info.laht.krender.proxies

import java.awt.Color
import java.io.File

interface ArrowProxy : RenderProxy, ColorProxy, SpatialProxy {
    fun setLength(length: Double)
}

interface AxisProxy : RenderProxy, SpatialProxy {
    fun setSize(size: Double)
}

interface BoxProxy : RenderProxy, ColorProxy, SpatialProxy, WireframeProxy

interface CapsuleProxy : RenderProxy, ColorProxy, SpatialProxy, WireframeProxy {
    fun setRadius(radius: Double)
    fun setHeight(height: Double)
}

interface ColorProxy {
    fun setColor(color: Color)
}

interface CurveProxy : RenderProxy, ColorProxy, WireframeProxy {
    fun update(points: List<Triple<Float, Float, Float>>)
}

interface CylinderProxy : RenderProxy, ColorProxy, SpatialProxy, WireframeProxy, TextureProxy {
    fun setRadius(radius: Double)
    fun setHeight(height: Double)
}

interface RenderProxy {
    fun setVisible(visible: Boolean)
    fun dispose()
}

interface SpatialProxy {
    fun setTranslate(x: Float, y: Float, z: Float)
    fun setRotate(x: Float, y: Float, z: Float, w: Float)
    fun setTransform(m: DoubleArray)
    fun setOffsetTransform(offset: DoubleArray)
}

interface SphereProxy : RenderProxy, ColorProxy, SpatialProxy, WireframeProxy {
    fun setRadius(radius: Double)
}

interface TerrainProxy : RenderProxy, SpatialProxy, WireframeProxy, ColorProxy, TextureProxy {
    fun setHeights(heights: FloatArray?)
}

interface TextureProxy {
    fun setTexture(source: File)
}

interface WaterProxy : RenderProxy, WireframeProxy {
    fun setTranslate(x: Float, y: Float, z: Float)
}

interface WireframeProxy {
    fun setWireframe(flag: Boolean)
}

interface MeshProxy : RenderProxy, WireframeProxy, SpatialProxy

interface PlaneProxy : RenderProxy, ColorProxy, SpatialProxy, WireframeProxy

interface PointCloudProxy
