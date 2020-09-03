package info.laht.krender.jme.proxy

import com.jme3.scene.Geometry
import com.jme3.scene.shape.Sphere
import info.laht.krender.jme.JmeContext
import info.laht.krender.proxies.SphereProxy
import org.joml.Matrix4dc

class JmeSphereProxy(
    ctx: JmeContext,
    private val originalRadius: Double,
    offset: Matrix4dc?
) : JmeProxy("sphere", ctx, offset), SphereProxy {

    init {
        attachChild(Geometry("", Sphere(32, 32, originalRadius.toFloat())))
    }

    override fun setRadius(radius: Double) {
        val scale = radius / originalRadius
        scale(scale.toFloat())
    }

}
