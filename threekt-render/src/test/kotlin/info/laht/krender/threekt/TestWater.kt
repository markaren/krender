package info.laht.krender.threekt

import info.laht.krender.ColorConstants
import org.joml.Matrix4d

fun main() {

    ThreektRenderer().apply {
        init(Matrix4d().setTranslation(-50.0, 50.0, -50.0))
        setBackGroundColor(ColorConstants.aliceblue)
        createWater(100f, 100f)
    }

}
