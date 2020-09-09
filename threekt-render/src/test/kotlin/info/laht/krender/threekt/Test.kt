package info.laht.krender.threekt

import java.awt.Color

fun main() {

    ThreektRenderer().apply {
        init()

        val s = createSphere(0.5f).apply {
            setColor(Color.red)
        }

    }

}
