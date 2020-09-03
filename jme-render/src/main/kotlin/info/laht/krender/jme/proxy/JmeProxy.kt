package info.laht.krender.jme.proxy

import com.jme3.material.Material
import com.jme3.material.RenderState
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.SceneGraphVisitorAdapter
import com.jme3.scene.Spatial
import com.jme3.texture.Texture
import info.laht.krender.jme.JmeContext
import info.laht.krender.jme.extra.JmeUtils
import info.laht.krender.jme.extra.JmeUtils.convert
import info.laht.krender.jme.extra.JmeUtils.getLightingMaterial
import info.laht.krender.jme.extra.JmeUtils.getWireFrameMaterial
import info.laht.krender.proxies.*
import info.laht.krender.util.ExternalSource
import org.joml.*
import java.awt.Color
import java.io.IOException

abstract class JmeProxy @JvmOverloads constructor(
    name: String?,
    protected val ctx: JmeContext,
    offset: Matrix4dc? = null
) : Node(name), RenderProxy, ColorProxy, SpatialProxy, WireframeProxy, TextureProxy {

    private val v = Vector3d()
    private val q = Quaterniond()
    private var color: Color? = null
    private var material_: Material
    private var isWireframe = false
    private val node: Node = Node()

    init {
        material_ = getLightingMaterial(ctx.assetManager)
        material_.additionalRenderState.faceCullMode = RenderState.FaceCullMode.Off
        attachChild(node)
        if (offset != null) {
            offset.getTranslation(v)
            offset.getNormalizedRotation(q)
            node.setLocalTranslation(v.x.toFloat(), v.y.toFloat(), v.z.toFloat())
            node.localRotation = convert(q)
        }
    }

    override fun setVisible(visible: Boolean) {
        ctx.invokeLater { setCullHint(if (visible) CullHint.Never else CullHint.Always) }
    }

    override fun dispose() {
        ctx.invokeLater { removeFromParent() }
    }

    override fun setColor(color: Color) {
        ctx.invokeLater {
            val colorRGBA = convert(color.also { this.color = it })
            if (isWireframe) {
                material_.setColor("Color", colorRGBA)
            } else {
                material_.setBoolean("UseMaterialColors", true)
                material_.setColor("Ambient", colorRGBA)
                material_.setColor("Diffuse", colorRGBA)
                material_.setColor("Specular", colorRGBA)
                material_.setColor("GlowColor", colorRGBA)
            }
        }
    }

    override fun setTexture(source: ExternalSource) {
        ctx.invokeLater {
            if (!isWireframe) {
                try {
                    val loadTexture: Texture = JmeUtils.loadTexture(ctx.assetManager, source)
                    material_.setTexture("DiffuseMap", loadTexture)
                } catch (ex: IOException) {
                    // Logger.getLogger(JmeProxy::class.java.name).log(Level.SEVERE, null, ex)
                }
            }
        }
    }

    override fun setTranslate(v: Vector3dc) {
        ctx.invokeLater {
            localTranslation[v.x().toFloat(), v.y().toFloat()] = v.z().toFloat()
            setTransformRefresh()
        }
    }

    override fun setRotate(q: Quaterniondc) {
        ctx.invokeLater {
            localRotation[q.x().toFloat(), q.y().toFloat(), q.z().toFloat()] = q.w().toFloat()
            setTransformRefresh()
        }
    }

    override fun setOffsetTransform(offset: Matrix4dc) {
        ctx.invokeLater {
            node.localTranslation = convert(offset.getTranslation(Vector3d()))
            node.localRotation = convert(offset.getNormalizedRotation(Quaterniond()))
        }
    }

    override fun setTransform(m: Matrix4dc) {
        m.getTranslation(v)
        m.getNormalizedRotation(q)
        ctx.invokeLater {
            localTranslation[v.x.toFloat(), v.y.toFloat()] = v.z.toFloat()
            localRotation[q.x.toFloat(), q.y.toFloat(), q.z.toFloat()] = q.w.toFloat()
            setTransformRefresh()
        }
    }

    override fun setWireframe(flag: Boolean) {
        ctx.invokeLater {
            if (flag && !isWireframe) {
                setMaterial(getWireFrameMaterial(ctx.assetManager, Color.BLACK).also { material_ = it })
            } else if (!flag && isWireframe) {
                setMaterial(getLightingMaterial(ctx.assetManager, color!!).also { material_ = it })
            }
            material_.additionalRenderState.faceCullMode = RenderState.FaceCullMode.Off
            isWireframe = flag
        }
    }

    override fun attachChild(child: Spatial): Int {
        return if (child === node) {
            super.attachChild(child)
        } else {
            val attachChild = node.attachChild(child)
            child.breadthFirstTraversal(object : SceneGraphVisitorAdapter() {
                override fun visit(geom: Geometry) {
                    if (geom.material == null || isWireframe) {
                        geom.material = material_
                    }
                }
            })
            attachChild
        }
    }

}
