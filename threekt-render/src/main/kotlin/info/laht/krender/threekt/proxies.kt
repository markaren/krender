package info.laht.krender.threekt

import info.laht.krender.proxies.*
import info.laht.krender.util.ExternalSource
import info.laht.krender.util.FileSource
import info.laht.krender.util.RenderContext
import info.laht.threekt.Side
import info.laht.threekt.core.Object3D
import info.laht.threekt.core.Object3DImpl
import info.laht.threekt.geometries.*
import info.laht.threekt.loaders.TextureLoader
import info.laht.threekt.materials.MaterialWithColor
import info.laht.threekt.materials.MaterialWithWireframe
import info.laht.threekt.materials.MeshBasicMaterial
import info.laht.threekt.math.Curve3
import info.laht.threekt.math.Vector3
import info.laht.threekt.math.curves.CatmullRomCurve3
import info.laht.threekt.objects.Mesh
import org.joml.Matrix4dc
import org.joml.Quaterniondc
import org.joml.Vector3dc
import java.awt.Color
import kotlin.math.roundToInt

internal open class ThreektProxy(
    val ctx: RenderContext
) : RenderProxy, WireframeProxy, ColorProxy, SpatialProxy, TextureProxy {

    val parentNode = Object3DImpl().apply { matrixAutoUpdate = false }
    private val childNode = Object3DImpl().apply { matrixAutoUpdate = false }

    init {
        ctx.invokeLater {
            parentNode.add(childNode)
        }
    }

    override fun setTranslate(v: Vector3dc) {
        ctx.invokeLater {
            parentNode.position.set(v)
            parentNode.updateMatrix()
        }
    }

    override fun setRotate(q: Quaterniondc) {
        ctx.invokeLater {
            parentNode.quaternion.set(q)
            parentNode.updateMatrix()
        }
    }

    override fun setTransform(m: Matrix4dc) {
        ctx.invokeLater {
            parentNode.matrix.set(m)
        }
    }

    override fun setOffsetTransform(offset: Matrix4dc) {
        ctx.invokeLater {
            childNode.matrix.set(offset)
        }
    }

    override fun setTexture(source: ExternalSource) {
        if (source is FileSource) {
            ctx.invokeLater {
                for (o in childNode.children) {
                    if (o is Mesh) {
                        val m = o.material
                        if (m is MeshBasicMaterial) {
                            m.map = TextureLoader.load(source.file.absolutePath)
                        }
                    }
                }
            }
        }
    }

    override fun setWireframe(flag: Boolean) {
        ctx.invokeLater {
            for (o in childNode.children) {
                if (o is Mesh) {
                    val material = o.material
                    if (material is MaterialWithWireframe) {
                        material.wireframe = flag
                    }
                }
            }
        }
    }

    override fun setColor(color: Color) {
        ctx.invokeLater {
            for (o in childNode.children) {
                if (o is Mesh) {
                    val material = o.material
                    if (material is MaterialWithColor) {
                        val rgb = color.getRGBColorComponents(FloatArray(3))
                        material.color.set(rgb[0], rgb[1], rgb[2])
                    }
                }
            }
        }
    }

    override fun setVisible(visible: Boolean) {
        ctx.invokeLater {
            childNode.visible = visible
        }
    }

    override fun dispose() {
        ctx.invokeLater {
            childNode.parent?.remove(childNode)
        }
    }

    protected fun attachChild(obj: Object3D) {
        childNode.add(obj)
    }

}

internal class ThreektPlaneProxy(
    ctx: RenderContext,
    width: Float,
    height: Float
) : ThreektProxy(ctx), PlaneProxy {

    private val geometry = PlaneBufferGeometry(width, height)
    private val mesh = Mesh(geometry)

    init {

        ctx.invokeLater {
            attachChild(mesh)
        }

    }

}

internal class ThreektBoxProxy(
    ctx: RenderContext,
    width: Float,
    height: Float,
    depth: Float
) : ThreektProxy(ctx), BoxProxy {

    private val geometry = BoxBufferGeometry(width, height, depth)
    private val mesh = Mesh(geometry)

    init {

        ctx.invokeLater {
            attachChild(mesh)
        }

    }

}

internal class ThreektSphereProxy(
    ctx: RenderContext,
    radius: Float
) : ThreektProxy(ctx), SphereProxy {

    private val originalRadius = radius
    private val geometry = SphereBufferGeometry(radius)
    private val mesh = Mesh(geometry)

    init {
        ctx.invokeLater {
            attachChild(mesh)
        }
    }

    override fun setRadius(radius: Float) {
        val scale = originalRadius / radius
        geometry.scale(scale)
    }

}

internal class ThreektCylinderProxy(
    ctx: RenderContext,
    radius: Float,
    height: Float
) : ThreektProxy(ctx), CylinderProxy {

    private val originalRadius = radius
    private val geometry = CylinderBufferGeometry(radius, height)
    private val mesh = Mesh(geometry)

    init {
        ctx.invokeLater {
            attachChild(mesh)
        }
    }

    override fun setRadius(radius: Float) {
        val scale = originalRadius / radius
        geometry.scale(scale)
    }

    override fun setHeight(height: Float) {
        TODO("Not yet implemented")
    }

}

class CurvePath(
    private val points: List<Vector3dc>
) : Curve3() {

    private fun map(x: Float, in_min: Float, in_max: Float, out_min: Float, out_max: Float): Int {
        return ((x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min).roundToInt()
    }

    override fun getPoint(t: Float, optionalTarget: Vector3): Vector3 {
        val i = map(t.coerceIn(0f, 1f), 0f, 1f, 0f, points.size.toFloat() - 1)
        val v = points[i]
        println("t=$t, i=$i, size=${points.size}")
        return optionalTarget.set(v.x().toFloat(), v.y().toFloat(), v.z().toFloat())
    }
}

internal class ThreektCurveProxy(
    ctx: RenderContext,
    private val radius: Float,
    points: List<Vector3dc>
) : ThreektProxy(ctx), CurveProxy {

    private val mesh = Mesh(
        TubeBufferGeometry(
            CatmullRomCurve3(points.map { Vector3().set(it) }),
            radius = radius
        )
    ).apply {
        material.side = Side.Double
    }

    init {
        ctx.invokeLater {
            attachChild(mesh)
        }
    }

    override fun update(points: List<Vector3dc>) {
        val curve = CatmullRomCurve3(points.map { Vector3().set(it) })
        ctx.invokeLater {
            mesh.geometry.dispose()
            mesh.geometry = TubeBufferGeometry(curve, radius = radius)
        }
    }
}
