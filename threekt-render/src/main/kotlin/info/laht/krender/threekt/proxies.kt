package info.laht.krender.threekt

import info.laht.krender.proxies.*
import info.laht.krender.util.RenderContext
import info.laht.threekt.core.Object3D
import info.laht.threekt.core.Object3DImpl
import info.laht.threekt.geometries.BoxBufferGeometry
import info.laht.threekt.geometries.SphereBufferGeometry
import info.laht.threekt.materials.MaterialWithColor
import info.laht.threekt.materials.MaterialWithWireframe
import info.laht.threekt.objects.Mesh
import org.joml.Matrix4dc
import org.joml.Quaterniondc
import org.joml.Vector3dc
import java.awt.Color

internal open class ThreektProxy(
    val ctx: RenderContext
) : RenderProxy, WireframeProxy, ColorProxy, SpatialProxy {

    val parentNode = Object3DImpl()
    private val childNode = Object3DImpl()

    init {
        ctx.invokeLater {
            parentNode.add(childNode)
        }
    }

    override fun setTranslate(v: Vector3dc) {
        ctx.invokeLater {
            parentNode.position.set(
                v.x().toFloat(),
                v.y().toFloat(),
                v.z().toFloat()
            )
        }
    }

    override fun setRotate(q: Quaterniondc) {
        ctx.invokeLater {
            parentNode.quaternion.set(
                q.x().toFloat(),
                q.y().toFloat(),
                q.z().toFloat(),
                q.w().toFloat()
            )
        }
    }

    override fun setTransform(m: Matrix4dc) {
        ctx.invokeLater {
            parentNode.matrix.set(
                m.m00().toFloat(), m.m01().toFloat(), m.m02().toFloat(), m.m03().toFloat(),
                m.m10().toFloat(), m.m11().toFloat(), m.m12().toFloat(), m.m13().toFloat(),
                m.m20().toFloat(), m.m21().toFloat(), m.m22().toFloat(), m.m23().toFloat(),
                m.m30().toFloat(), m.m31().toFloat(), m.m32().toFloat(), m.m33().toFloat()
            )
        }
    }

    override fun setOffsetTransform(offset: Matrix4dc) {
        ctx.invokeLater {
            childNode.matrix.set(
                offset.m00().toFloat(), offset.m01().toFloat(), offset.m02().toFloat(), offset.m03().toFloat(),
                offset.m10().toFloat(), offset.m11().toFloat(), offset.m12().toFloat(), offset.m13().toFloat(),
                offset.m20().toFloat(), offset.m21().toFloat(), offset.m22().toFloat(), offset.m23().toFloat(),
                offset.m30().toFloat(), offset.m31().toFloat(), offset.m32().toFloat(), offset.m33().toFloat()
            )
        }
    }

    override fun setWireframe(flag: Boolean) {
        ctx.invokeLater {
            if (childNode is Mesh) {
                val material = childNode.material
                if (material is MaterialWithWireframe) {
                    material.wireframe = flag
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
