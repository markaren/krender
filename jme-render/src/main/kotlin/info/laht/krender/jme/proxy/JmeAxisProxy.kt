package info.laht.krender.jme.proxy

import info.laht.krender.jme.JmeContext
import info.laht.krender.jme.extra.JmeAxis
import info.laht.krender.proxies.AxisProxy


class JmeAxisProxy(
    ctx: JmeContext,
    size: Double
) : JmeProxy("axis", ctx), AxisProxy {

    private var initialSize = 0.0


    init {
        attachChild(JmeAxis(ctx, size.also { initialSize = it }))
    }

    override fun setSize(size: Double) {
        val scale = size / initialSize
        scale(scale.toFloat())
    }

}
