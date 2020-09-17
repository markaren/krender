package info.laht.krender.threekt

import info.laht.krender.ColorConstants
import org.joml.Matrix4f

fun main() {

    ThreektRenderer().apply {
        setCameraTransform(Matrix4f().setTranslation(-50f, 50f, -50f))
        setBackGroundColor(ColorConstants.aliceblue)
        createWater(100f, 100f)
    }

}
