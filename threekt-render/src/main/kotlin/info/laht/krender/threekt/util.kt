package info.laht.krender.threekt

import info.laht.threekt.math.Matrix4
import info.laht.threekt.math.Quaternion
import info.laht.threekt.math.Vector3
import org.joml.Matrix4dc
import org.joml.Quaterniondc
import org.joml.Vector3dc

internal fun Vector3.set(v: Vector3dc): Vector3 {
    return set(v.x().toFloat(), v.y().toFloat(), v.z().toFloat())
}

internal fun Quaternion.set(q: Quaterniondc): Quaternion {
    return set(q.x().toFloat(), q.y().toFloat(), q.z().toFloat(), q.w().toFloat())
}

internal fun Matrix4.set(m: Matrix4dc): Matrix4 {
    return set(
        m.m00().toFloat(), m.m10().toFloat(), m.m20().toFloat(), m.m30().toFloat(),
        m.m01().toFloat(), m.m11().toFloat(), m.m21().toFloat(), m.m31().toFloat(),
        m.m02().toFloat(), m.m12().toFloat(), m.m22().toFloat(), m.m32().toFloat(),
        m.m03().toFloat(), m.m13().toFloat(), m.m23().toFloat(), m.m33().toFloat()
    )
}

internal fun List<Vector3dc>.flatten(): FloatArray {
    var i = 0
    val array = FloatArray(3 * size)
    for (v in this) {
        array[i++] = v.x().toFloat()
        array[i++] = v.y().toFloat()
        array[i++] = v.z().toFloat()
    }
    return array
}

