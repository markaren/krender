package info.laht.krender.jme.extra

import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.shape.Cylinder

class JmeArrow(length: Double) : Node() {
    init {
        val bodyRadius = (length * 0.05).toFloat()
        val tipRadius = (bodyRadius * 1.5).toFloat()
        val bodyLen = (length * 0.8).toFloat()
        val tipLen = (length * 0.2).toFloat()
        val body = Geometry("", Cylinder(32, 32, bodyRadius, bodyLen, true))
        val cone = Geometry("", Cylinder(32, 32, 0.001f, tipRadius, tipLen, true, false))
        body.move(0f, 0f, bodyLen / 2)
        cone.move(0f, 0f, bodyLen + tipLen / 2)
        attachChild(body)
        attachChild(cone)

//        move(0, 0, (float)(length*0.5));
    }

}
