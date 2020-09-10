package info.laht.krender.jme

import com.jme3.math.Vector2f
import com.jme3.math.Vector3f
import org.joml.Vector2d
import org.joml.Vector3d

internal fun Vector2d.set(v: Vector2f): Vector2d {
    return set(v.x.toDouble(), v.y.toDouble())
}

internal fun Vector3d.set(v: Vector3f): Vector3d {
    return set(v.x.toDouble(), v.y.toDouble(), v.z.toDouble())
}
