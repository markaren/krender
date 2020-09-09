package info.laht.krender.jme.proxy

import info.laht.krender.jme.JmeContext
import info.laht.krender.jme.extra.JmeCapsule
import info.laht.krender.proxies.CapsuleProxy

internal class JmeCapsuleProxy(
    ctx: JmeContext,
    radius: Float,
    height: Float
) : JmeProxy("capsule", ctx), CapsuleProxy {

    private var originalRadius = 0f
    private var originalHeight = 0f

    init {
        attachChild(
            JmeCapsule(
                32,
                32,
                32,
                32,
                radius.also { originalRadius = it },
                height.also { originalHeight = it }
            )
        )
    }

    override fun setRadius(radius: Float) {
        val scale = (radius / originalRadius)
        scale(scale, scale, 1f)
    }

    override fun setHeight(height: Float) {
        val scale = (height / originalHeight)
        scale(1f, 1f, scale)
    }

}
