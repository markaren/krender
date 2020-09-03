package info.laht.krender.jme.proxy

import com.jme3.scene.Geometry
import info.laht.krender.jme.JmeContext
import info.laht.krender.jme.extra.JmeGrid
import info.laht.krender.proxies.PlaneProxy
import org.joml.Matrix4dc

class JmePlaneProxy(
    ctx: JmeContext,
    width: Float,
    height: Float,
    offset: Matrix4dc?
) : JmeProxy("plane", ctx, offset), PlaneProxy {

    init {
        attachChild(Geometry("", JmeGrid(1, 1, width, height)))
    }

}
