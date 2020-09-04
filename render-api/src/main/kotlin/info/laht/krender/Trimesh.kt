package info.laht.krender

import info.laht.krender.util.ExternalSource
import info.laht.krender.util.InputSource
import org.joml.Vector3d
import org.joml.Vector4d

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

    private fun Vector3d.fromArray(list: List<Double>, offset: Int) {
        x = list[offset]
        y = list[offset + 1]
        z = list[offset + 2]
    }

    private fun Vector3d.fromArray(list: DoubleArray, offset: Int) {
        x = list[offset]
        y = list[offset + 1]
        z = list[offset + 2]
    }

    private fun Vector4d.fromArray(list: List<Double>, offset: Int) {
        x = list[offset]
        y = list[offset + 1]
        z = list[offset + 2]
        w = list[offset + 3]
    }

    private fun Vector4d.fromArray(list: DoubleArray, offset: Int) {
        x = list[offset]
        y = list[offset + 1]
        z = list[offset + 2]
        w = list[offset + 3]
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
