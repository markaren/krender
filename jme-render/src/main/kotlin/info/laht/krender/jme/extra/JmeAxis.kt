package info.laht.krender.jme.extra

import com.jme3.asset.AssetManager
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.debug.Arrow
import info.laht.krender.jme.JmeContext

class JmeAxis(ctx: JmeContext, length: Double) : Node() {

    init {
        attachChild(createArrow(ctx.assetManager, Vector3f.UNIT_X.mult(length.toFloat()), ColorRGBA.Red))
        attachChild(createArrow(ctx.assetManager, Vector3f.UNIT_Y.mult(length.toFloat()), ColorRGBA.Green))
        attachChild(createArrow(ctx.assetManager, Vector3f.UNIT_Z.mult(length.toFloat()), ColorRGBA.Blue))
    }

    companion object {
        private fun createArrow(assetManager: AssetManager, extent: Vector3f, color: ColorRGBA): Geometry {
            val arrowGeometry = Geometry("", Arrow(extent))
            val material = JmeUtils.getUnshadedMaterial(assetManager)
            material.setColor("Color", color)
            arrowGeometry.material = material
            return arrowGeometry
        }
    }

}
