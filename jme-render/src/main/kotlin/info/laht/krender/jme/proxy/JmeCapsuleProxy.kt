package info.laht.krender.jme.proxy

import info.laht.krender.jme.JmeContext
import info.laht.krender.jme.extra.JmeCapsule
import info.laht.krender.proxies.CapsuleProxy
import org.joml.Matrix4dc

class JmeCapsuleProxy(
    ctx: JmeContext,
    radius: Double,
    height: Double,
    offset: Matrix4dc?
) : JmeProxy("capsule", ctx, offset), CapsuleProxy {

    private var originalRadius = 0.0
    private var originalHeight = 0.0

    init {
        attachChild(
            JmeCapsule(
                32,
                32,
                32,
                32,
                radius.also { originalRadius = it }.toFloat(),
                height.also { originalHeight = it }
                    .toFloat()))
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
