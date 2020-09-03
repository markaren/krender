package info.laht.krender.util

import org.joml.Matrix4dc
import org.joml.Vector3d
import org.joml.Vector3dc

object JomlUtil {

    fun toString(v: Vector3dc): String {
        return "[x=" + v.x() + ", y=" + v.y() + ", z=" + v.z() + "]"
    }

    fun min(v1: Vector3dc, v2: Vector3dc, store: Vector3d): Vector3d {
        store.x = kotlin.math.min(v1.x(), v2.x())
        store.y = kotlin.math.min(v1.y(), v2.y())
        store.z = kotlin.math.min(v1.z(), v2.z())
        return store
    }

    fun max(v1: Vector3dc, v2: Vector3dc, store: Vector3d): Vector3d {
        store.x = kotlin.math.max(v1.x(), v2.x())
        store.y = kotlin.math.max(v1.y(), v2.y())
        store.z = kotlin.math.max(v1.z(), v2.z())
        return store
    }

    fun clamp(v: Vector3d, min: Double, max: Double): Vector3d {
        v.x = kotlin.math.max(min, kotlin.math.min(max, v.x()))
        v.y = kotlin.math.max(min, kotlin.math.min(max, v.y()))
        v.z = kotlin.math.max(min, kotlin.math.min(max, v.z()))
        return v
    }

    fun clamp(v: Vector3d, min: Vector3dc, max: Vector3dc): Vector3d {
        v.x = kotlin.math.max(min.x(), kotlin.math.min(max.x(), v.x()))
        v.y = kotlin.math.max(min.y(), kotlin.math.min(max.y(), v.y()))
        v.z = kotlin.math.max(min.z(), kotlin.math.min(max.z(), v.z()))
        return v
    }

    fun clamp(v: Vector3dc, min: Vector3dc, max: Vector3dc, store: Vector3d): Vector3d {
        store.x = kotlin.math.max(min.x(), kotlin.math.min(max.x(), v.x()))
        store.y = kotlin.math.max(min.y(), kotlin.math.min(max.y(), v.y()))
        store.z = kotlin.math.max(min.z(), kotlin.math.min(max.z(), v.z()))
        return store
    }

    /*fun normalize(v: Vector3d, min: Vector3dc, max: Vector3dc): Vector3d {
        v.x = MathUtil.map(MathUtil.clamp(v.x, min.x(), max.x()), min.x(), max.x(), 0, 1)
        v.y = MathUtil.map(MathUtil.clamp(v.y, min.y(), max.y()), min.y(), max.y(), 0, 1)
        v.z = MathUtil.map(MathUtil.clamp(v.z, min.z(), max.z()), min.z(), max.z(), 0, 1)
        return v
    }

    fun denormalize(v: Vector3d, min: Vector3dc, max: Vector3dc): Vector3d {
        v.x = MathUtil.map(MathUtil.clamp(v.x, 0, 1), 0, 1, min.x(), max.x())
        v.y = MathUtil.map(MathUtil.clamp(v.y, 0, 1), 0, 1, min.y(), max.y())
        v.z = MathUtil.map(MathUtil.clamp(v.z, 0, 1), 0, 1, min.z(), max.z())
        return v
    }
*/
    fun getMaxScaleOnAxis(m: Matrix4dc): Double {
        val scaleXSq = m.m00() * m.m00() + m.m01() * m.m01() + m.m02() * m.m02()
        val scaleYSq = m.m10() * m.m10() + m.m11() * m.m11() + m.m12() * m.m12()
        val scaleZSq = m.m20() * m.m20() + m.m21() * m.m21() + m.m22() * m.m22()
        return kotlin.math.sqrt(kotlin.math.max(kotlin.math.max(scaleXSq, scaleYSq), scaleZSq))
    }

    fun rowMajor(m: Matrix4dc, dest: FloatArray): FloatArray {
        var dest = dest
        if (dest == null) {
            dest = FloatArray(16)
        }
        dest[0] = m.m00().toFloat()
        dest[1] = m.m10().toFloat()
        dest[2] = m.m20().toFloat()
        dest[3] = m.m30().toFloat()
        dest[4] = m.m01().toFloat()
        dest[5] = m.m11().toFloat()
        dest[6] = m.m21().toFloat()
        dest[7] = m.m31().toFloat()
        dest[8] = m.m02().toFloat()
        dest[9] = m.m12().toFloat()
        dest[10] = m.m22().toFloat()
        dest[11] = m.m32().toFloat()
        dest[12] = m.m03().toFloat()
        dest[13] = m.m13().toFloat()
        dest[14] = m.m23().toFloat()
        dest[15] = m.m33().toFloat()
        return dest
    }
}
