package info.laht.krender.jme.proxy

import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box
import info.laht.krender.jme.JmeContext
import info.laht.krender.proxies.BoxProxy
import org.joml.Matrix4dc

class JmeBoxProxy(
    ctx: JmeContext,
    width: Double,
    height: Double,
    depth: Double,
    offset: Matrix4dc?
) : JmeProxy("box", ctx, offset), BoxProxy {

    init {
        attachChild(Geometry("", Box((width * 0.5).toFloat(), (height * 0.5).toFloat(), (depth * 0.5).toFloat())))
    }

}
