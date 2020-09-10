package info.laht.krender.jme

import info.laht.krender.ColorConstants
import org.joml.Vector3d

fun main() {

    JmeRenderEngine().apply {

        init()

        createBox(1f, 1f, 1f).apply {
            setTranslate(Vector3d(-2.0, 0.0, 0.0))
            setColor(ColorConstants.bisque)
        }

        createSphere(0.5f).apply {
            setTranslate(Vector3d(0.0, 0.0, 0.0))
            setColor(ColorConstants.firebrick)
        }

        createCapsule(0.5f, 1f).apply {
            setTranslate(Vector3d(2.0, 0.0, 0.0))
            setColor(ColorConstants.antiquewhite)
        }

        createCylinder(0.5f, 1f).apply {
            setTranslate(Vector3d(4.0, 0.0, 0.0))
            setColor(ColorConstants.rebeccapurple)
        }

    }

}
