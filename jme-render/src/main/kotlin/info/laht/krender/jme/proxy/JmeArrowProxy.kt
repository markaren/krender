package info.laht.krender.jme.proxy

import info.laht.krender.jme.JmeContext
import info.laht.krender.jme.extra.JmeArrow
import info.laht.krender.proxies.ArrowProxy

class JmeArrowProxy(
    ctx: JmeContext,
    private val originalLength: Double
) : JmeProxy("arrow", ctx), ArrowProxy {

    init {
        attachChild(JmeArrow(originalLength))
    }

    override fun setVisible(visible: Boolean) {
        ctx.invokeLater { setCullHint(if (visible) CullHint.Never else CullHint.Always) }
    }

    override fun setLength(length: Double) {
        val scale = length / originalLength
        scale(scale.toFloat())
    }

}
