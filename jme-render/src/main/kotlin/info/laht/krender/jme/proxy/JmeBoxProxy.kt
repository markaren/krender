package info.laht.krender.jme.proxy

import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box
import info.laht.krender.jme.JmeContext
import info.laht.krender.proxies.BoxProxy
import org.joml.Matrix4dc

internal class JmeBoxProxy(
    ctx: JmeContext,
    width: Float,
    height: Float,
    depth: Float,
    offset: Matrix4dc?
) : JmeProxy("box", ctx, offset), BoxProxy {

    init {
        attachChild(Geometry("", Box((width * 0.5f), (height * 0.5f), (depth * 0.5f))))
    }

}
