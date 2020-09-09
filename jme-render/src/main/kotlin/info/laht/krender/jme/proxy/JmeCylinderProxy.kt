package info.laht.krender.jme.proxy

import com.jme3.scene.Geometry
import com.jme3.scene.shape.Cylinder
import info.laht.krender.jme.JmeContext
import info.laht.krender.proxies.CylinderProxy

internal class JmeCylinderProxy(
    ctx: JmeContext,
    private val originalRadius: Float,
    private val originalHeight: Float
) : JmeProxy("cylinder", ctx), CylinderProxy {

    init {
        attachChild(Geometry("", Cylinder(32, 32, originalRadius, originalHeight, true)))
    }

    override fun setRadius(radius: Float) {
        val scale = (radius / originalRadius).toFloat()
        scale(scale, scale, 1f)
    }

    override fun setHeight(height: Float) {
        val scale = (height / originalHeight).toFloat()
        scale(1f, 1f, scale)
    }

}
