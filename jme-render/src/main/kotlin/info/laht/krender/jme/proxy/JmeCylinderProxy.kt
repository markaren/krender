package info.laht.krender.jme.proxy

import com.jme3.scene.Geometry
import com.jme3.scene.shape.Cylinder
import info.laht.krender.jme.JmeContext
import info.laht.krender.proxies.CylinderProxy
import org.joml.Matrix4dc

class JmeCylinderProxy(
    ctx: JmeContext,
    private val originalRadius: Float,
    private val originalHeight: Float,
    offset: Matrix4dc?
) : JmeProxy("cylinder", ctx, offset), CylinderProxy {

    init {
        attachChild(Geometry("", Cylinder(32, 32, originalRadius, originalHeight, true)))
    }

    override fun setRadius(radius: Double) {
        val scale = (radius / originalRadius).toFloat()
        scale(scale, scale, 1f)
    }

    override fun setHeight(height: Double) {
        val scale = (height / originalHeight).toFloat()
        scale(1f, 1f, scale)
    }

}
