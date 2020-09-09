package info.laht.krender.mesh

import info.laht.krender.util.ExternalSource
import info.laht.krender.util.JomlUtil.fromArray
import org.joml.*
import java.util.*

internal data class Face(
    val a: Int,
    val b: Int,
    val c: Int
)

class Trimesh private constructor(
    builder: Builder
) {

    val indices: MutableList<Int> = builder.indices
    val vertices: MutableList<Double> = builder.vertices
    val normals: MutableList<Double> = builder.normals
    val colors: MutableList<Float> = builder.colors
    val uvs: MutableList<Double> = builder.uvs

    var source: ExternalSource? = null
    var scale: Float = 1f
        private set

    fun hasSource(): Boolean {
        return source != null
    }

    fun hasIndices(): Boolean {
        return indices.isNotEmpty()
    }

    fun hasVertices(): Boolean {
        return vertices.isNotEmpty()
    }

    fun hasNormals(): Boolean {
        return normals.isNotEmpty()
    }

    fun hasColors(): Boolean {
        return colors.isNotEmpty()
    }

    fun hasUvs(): Boolean {
        return uvs.isNotEmpty()
    }

    fun scale(scale: Float) {
        this.scale *= scale
        var i = 0
        while (i < vertices.size) {
            vertices[i + 0] = vertices[i + 0] * scale
            vertices[i + 1] = vertices[i + 1] * scale
            vertices[i + 2] = vertices[i + 2] * scale
            i += 3
        }
    }

    @JvmOverloads
    fun generateIndices(force: Boolean = false) = apply {
        if (indices.isEmpty() || force) {
            indices.clear()
            for (i in 0 until vertices.size / 3) {
                indices.add(i)
            }
        }
        return this
    }


    fun rotateX(angle: Number) = apply {
        return applyMatrix4(Matrix4d().rotate(angle.toDouble(), 1.0, 0.0, 0.0))
    }

    fun rotateY(angle: Number) = apply {
        return applyMatrix4(Matrix4d().rotate(angle.toDouble(), 0.0, 1.0, 0.0))
    }

    fun rotateZ(angle: Number) = apply {
        return applyMatrix4(Matrix4d().rotate(angle.toDouble(), 0.0, 0.0, 1.0))
    }

    fun translateX(x: Number) = apply {
        return applyMatrix4(Matrix4d().setTranslation(x.toDouble(), 0.0, 0.0))
    }

    fun translateY(y: Number) = apply {
        return applyMatrix4(Matrix4d().setTranslation(0.0, y.toDouble(), 0.0))
    }

    fun translateZ(z: Number) = apply {
        return applyMatrix4(Matrix4d().setTranslation(0.0, 0.0, z.toDouble()))
    }

    fun translate(x: Number, y: Number, z: Number) = apply {
        return applyMatrix4(Matrix4d().setTranslation(x.toDouble(), y.toDouble(), z.toDouble()))
    }

    fun applyMatrix4(m: Matrix4dc) = apply {

        val v = Vector3d()
        for (i in 0 until vertices.size / 3) {
            val index = i * 3
            v[vertices[index + 0], vertices[index + 1]] = vertices[index + 2]
            v.mulPosition(m)
            vertices[index + 0] = v.x
            vertices[index + 1] = v.y
            vertices[index + 2] = v.z
        }

        if (hasNormals()) {
            val normalMatrix = m.normal(Matrix3d())
            var i = 0
            while (i < normals.size) {
                v.set(normals[i], normals[i + 1], normals[i + 2]).mul(normalMatrix)
                normals[i + 0] = v.x
                normals[i + 1] = v.y
                normals[i + 2] = v.z
                i += 3
            }
        }

    }

    fun computeVertexNormals() = apply {
        if (vertices.isNotEmpty()) {
            normals.clear()
            for (i in vertices.indices) {
                normals.add(0.0)
            }
            var vA: Int
            var vB: Int
            var vC: Int
            val pA = Vector3d()
            val pB = Vector3d()
            val pC = Vector3d()
            val cb = Vector3d()
            val ab = Vector3d()

            // indexed elements
            if (indices.isNotEmpty()) {
                val start = 0
                val count = indices.size
                var i = start
                val il = start + count
                while (i < il) {
                    vA = indices[i + 0] * 3
                    vB = indices[i + 1] * 3
                    vC = indices[i + 2] * 3
                    pA.fromArray(vertices, vA)
                    pB.fromArray(vertices, vB)
                    pC.fromArray(vertices, vC)
                    cb.set(pC).sub(pB)
                    ab.set(pA).sub(pB)
                    cb.cross(ab)
                    normals[vA + 0] = normals[vA + 0] + cb.x
                    normals[vA + 1] = normals[vA + 1] + cb.y
                    normals[vA + 2] = normals[vA + 2] + cb.z
                    normals[vB + 0] = normals[vB + 0] + cb.x
                    normals[vB + 1] = normals[vB + 1] + cb.y
                    normals[vB + 2] = normals[vB + 2] + cb.z
                    normals[vC + 0] = normals[vC + 0] + cb.x
                    normals[vC + 1] = normals[vC + 1] + cb.y
                    normals[vC + 2] = normals[vC + 2] + cb.z
                    i += 3
                }
            } else {

                // non-indexed elements (unconnected triangle soup)
                var i = 0
                val il = vertices.size
                while (i < il) {
                    pA.fromArray(vertices, i + 0)
                    pB.fromArray(vertices, i + 3)
                    pC.fromArray(vertices, i + 6)
                    cb.set(pC).sub(pB)
                    ab.set(pA).sub(pB)
                    cb.cross(ab)
                    normals[i + 0] = cb.x
                    normals[i + 1] = cb.y
                    normals[i + 2] = cb.z
                    normals[i + 3] = cb.x
                    normals[i + 4] = cb.y
                    normals[i + 5] = cb.z
                    normals[i + 6] = cb.x
                    normals[i + 7] = cb.y
                    normals[i + 8] = cb.z
                    i += 9
                }
            }
            this.normalizeNormals()
        }
    }

    private fun normalizeNormals() {
        val vector = Vector3d()
        var i = 0
        val il = normals.size / 3
        while (i < il) {
            val index = i * 3
            vector.x = normals[index + 0]
            vector.y = normals[index + 1]
            vector.z = normals[index + 2]
            vector.normalize()
            normals[index + 0] = vector.x
            normals[index + 1] = vector.y
            normals[index + 2] = vector.z
            i++
        }
    }

    fun getVolume(): Double {
        val faces: MutableList<Face> = ArrayList()
        val vertex: MutableList<Vector3d> = ArrayList()

        var i = 0
        while (i < vertices.size) {
            vertex.add(Vector3d(vertices[i], vertices[i + 1], vertices[i + 2]))
            i += 3
        }

        i = 0
        while (i < indices.size) {
            faces.add(Face(indices[i], indices[i + 1], indices[i + 2]))
            i += 3
        }

        return faces.map { f ->
            signedVolumeOfTriangle(vertex[f.a], vertex[f.b], vertex[f.c])
        }.sum()
    }

    private fun signedVolumeOfTriangle(p1: Vector3dc, p2: Vector3dc, p3: Vector3dc): Double {
        val v321 = p3.x() * p2.y() * p1.z()
        val v231 = p2.x() * p3.y() * p1.z()
        val v312 = p3.x() * p1.y() * p2.z()
        val v132 = p1.x() * p3.y() * p2.z()
        val v213 = p2.x() * p1.y() * p3.z()
        val v123 = p1.x() * p2.y() * p3.z()
        return 1.0 / 6.0 * (-v321 + v231 + v312 - v132 - v213 + v123)
    }

    class Builder {

        internal val indices: MutableList<Int> = mutableListOf()
        internal val vertices: MutableList<Double> = mutableListOf()
        internal val normals: MutableList<Double> = mutableListOf()
        internal val colors: MutableList<Float> = mutableListOf()
        internal val uvs: MutableList<Double> = mutableListOf()

        fun indices(indices: List<Int>) = apply {
            this.indices.addAll(indices)
        }

        fun indices(indices: IntArray) = apply {
            indices.forEach { this.indices.add(it) }
        }

        fun vertices(vertices: List<Double>) = apply {
            this.vertices.addAll(vertices)
        }

        fun vertices(vertices: DoubleArray) = apply {
            vertices.forEach { this.vertices.add(it) }
        }

        fun normals(normals: List<Double>) = apply {
            this.normals.addAll(normals)
        }

        fun normals(normals: DoubleArray) = apply {
            normals.forEach { this.normals.add(it) }
        }

        fun colors(colors: List<Float>) = apply {
            this.colors.addAll(colors)
        }

        fun colors(colors: FloatArray) = apply {
            colors.forEach { this.colors.add(it) }
        }

        fun uvs(uvs: List<Double>) = apply {
            this.uvs.addAll(uvs)
        }

        fun uvs(uvs: DoubleArray) = apply {
            uvs.forEach { this.uvs.add(it) }
        }

        fun build(): Trimesh {
            return Trimesh(this)
        }

    }

}
