package info.laht.krender.jme.extra

import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.shape.Cylinder
import com.jme3.scene.shape.Sphere

class JmeCapsule(
    axisSamples: Int,
    radialSamples: Int,
    zSamples: Int,
    radialSamples2: Int,
    radius: Float,
    height: Float
) : Node() {

    val radius: Double
    val height: Double

    init {
        this.height = height.toDouble()
        this.radius = radius.toDouble()
        val body = Geometry("body", Cylinder(axisSamples, radialSamples, radius, height, false))
        val upperSphere = Geometry("upperSphere", Sphere(zSamples, radialSamples2, radius))
        val lowerSphere = Geometry("lowerSphere", Sphere(zSamples, radialSamples2, radius))
        upperSphere.setLocalTranslation(0f, 0f, height / 2)
        lowerSphere.setLocalTranslation(0f, 0f, -height / 2)
        attachChild(body)
        attachChild(upperSphere)
        attachChild(lowerSphere)
    }
}
