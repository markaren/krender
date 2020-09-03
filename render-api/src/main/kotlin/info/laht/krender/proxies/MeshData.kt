package info.laht.krender.proxies

class MeshData private constructor(
    builder: Builder
) {

    val indices: List<Int>
    val vertices: List<Float>
    val normals: List<Float>
    val colors: List<Float>
    val uvs: List<Float>

    init {
        indices = builder.indices
        vertices = builder.vertices
        normals = builder.normals
        colors = builder.colors
        uvs = builder.uvs
    }

    class Builder {

        internal val indices: MutableList<Int> = mutableListOf()
        internal val vertices: MutableList<Float> = mutableListOf()
        internal val normals: MutableList<Float> = mutableListOf()
        internal val colors: MutableList<Float> = mutableListOf()
        internal val uvs: MutableList<Float> = mutableListOf()

        fun indices(indices: List<Int>) = apply {
            this.indices.addAll(indices)
        }

        fun indices(indices: IntArray) = apply {
            indices.forEach { this.indices.add(it) }
        }

        fun vertices(vertices: List<Float>) = apply {
            this.vertices.addAll(vertices)
        }

        fun vertices(vertices: FloatArray) = apply {
            vertices.forEach { this.vertices.add(it) }
        }

        fun normals(normals: List<Float>) = apply {
            this.normals.addAll(normals)
        }

        fun normals(normals: FloatArray) = apply {
            normals.forEach { this.normals.add(it) }
        }

        fun colors(colors: List<Float>) = apply {
            this.colors.addAll(colors)
        }

        fun colors(colors: FloatArray) = apply {
            colors.forEach { this.colors.add(it) }
        }

        fun uvs(uvs: List<Float>) = apply {
            this.uvs.addAll(uvs)
        }

        fun uvs(uvs: FloatArray) = apply {
            uvs.forEach { this.uvs.add(it) }
        }

        fun build(): MeshData {
            return MeshData(this)
        }

    }

    fun hasIndices(): Boolean {
        return !indices.isEmpty()
    }

    fun hasVertices(): Boolean {
        return !vertices.isEmpty()
    }

    fun hasNormals(): Boolean {
        return !normals.isEmpty()
    }

    fun hasColors(): Boolean {
        return !colors.isEmpty()
    }

    fun hasUvs(): Boolean {
        return !uvs.isEmpty()
    }

}
