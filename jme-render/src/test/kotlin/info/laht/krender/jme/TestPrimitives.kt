package info.laht.krender.jme

import org.joml.Vector3d

fun main() {

    JmeRenderEngine().apply {

        init()

        createBox(1f, 1f, 1f).apply {
            setTranslate(Vector3d(-2.0, 0.0, 0.0))
        }

        createSphere(0.5f).apply {
            setTranslate(Vector3d(0.0, 0.0, 0.0))
        }

        createCapsule(0.5f, 1f).apply {
            setTranslate(Vector3d(2.0, 0.0, 0.0))
        }

        createCylinder(0.5f, 1f).apply {
            setTranslate(Vector3d(4.0, 0.0, 0.0))
        }


    }

}
