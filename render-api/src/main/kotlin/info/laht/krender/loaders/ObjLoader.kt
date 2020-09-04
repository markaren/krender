package info.laht.krender.loaders

import info.laht.krender.Trimesh
import info.laht.krender.util.ExternalSource
import info.laht.krender.util.InputSource
import java.util.regex.Pattern


class ObjLoader {

    val supportedExtension: String
        get() = "obj"

    fun load(source: ExternalSource): Trimesh {
        //MeshLoader.testExtension(supportedExtension, source.extension)
        val parse = load(source.readText())
        parse.source = source
        return parse
    }

    fun load(text: String): Trimesh {

        val indices: MutableList<Int> = mutableListOf()
        val vertices: MutableList<Double> = mutableListOf()
        val normals: MutableList<Double> = mutableListOf()

        run {
            val compile = Pattern.compile(VERTEX_PATTERN)
            val matcher = compile.matcher(text)
            while (matcher.find()) {
                val result = matcher.group().replace("  ", " ").split(" ").toTypedArray()
                vertices.add(result[1].toDouble())
                vertices.add(result[2].toDouble())
                vertices.add(result[3].toDouble())
            }
        }
        run {
            val compile = Pattern.compile(NORMAL_PATTERN)
            val matcher = compile.matcher(text)
            while (matcher.find()) {
                val result = matcher.group().replace("  ", " ").split(" ").toTypedArray()
                normals.add(result[1].toDouble())
                normals.add(result[2].toDouble())
                normals.add(result[3].toDouble())
            }
        }
        run {
            val compile = Pattern.compile(FACE_PATTERN1)
            val matcher = compile.matcher(text)
            while (matcher.find()) {
                val result = matcher.group().split(" ").toTypedArray()
                indices.add(result[1].toInt() - 1)
                indices.add(result[2].toInt() - 1)
                indices.add(result[3].toInt() - 1)
            }
        }
        run {
            val compile = Pattern.compile(FACE_PATTERN2)
            val matcher = compile.matcher(text)
            while (matcher.find()) {
                val result = matcher.group().split(" ").toTypedArray()
                indices.add(result[1].split("/").toTypedArray()[0].toInt() - 1)
                indices.add(result[2].split("/").toTypedArray()[0].toInt() - 1)
                indices.add(result[3].split("/").toTypedArray()[0].toInt() - 1)
            }
        }
        run {
            val compile = Pattern.compile(FACE_PATTERN3)
            val matcher = compile.matcher(text)
            while (matcher.find()) {
                val result = matcher.group().split(" ").toTypedArray()
                indices.add(result[1].split("/").toTypedArray()[0].toInt() - 1)
                indices.add(result[2].split("/").toTypedArray()[0].toInt() - 1)
                indices.add(result[3].split("/").toTypedArray()[0].toInt() - 1)
            }
        }
        run {
            val compile = Pattern.compile(FACE_PATTERN4)
            val matcher = compile.matcher(text)
            while (matcher.find()) {
                val result = matcher.group().split(" ").toTypedArray()
                indices.add(result[1].split("//").toTypedArray()[0].toInt() - 1)
                indices.add(result[2].split("//").toTypedArray()[0].toInt() - 1)
                indices.add(result[3].split("//").toTypedArray()[0].toInt() - 1)
            }
        }
        return Trimesh.Builder()
            .indices(indices)
            .vertices(vertices)
            .normals(normals)
            .build()
    }

    companion object {

        private const val VERTEX_PATTERN =
            "v( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)"
        private const val NORMAL_PATTERN =
            "vn( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)( +[\\d|\\.|\\+|\\-|e|E]+)"

        // f vertex vertex vertex ...
        private const val FACE_PATTERN1 = "f( +-?\\d+)( +-?\\d+)( +-?\\d+)( +-?\\d+)?"

        // f vertex/uv vertex/uv vertex/uv ...
        private const val FACE_PATTERN2 =
            "f( +(-?\\d+)\\/(-?\\d+))( +(-?\\d+)\\/(-?\\d+))( +(-?\\d+)\\/(-?\\d+))( +(-?\\d+)\\/(-?\\d+))?"

        // f vertex/uv/normal vertex/uv/normal vertex/uv/normal ...
        private const val FACE_PATTERN3 =
            "f( +(-?\\d+)\\/(-?\\d+)\\/(-?\\d+))( +(-?\\d+)\\/(-?\\d+)\\/(-?\\d+))( +(-?\\d+)\\/(-?\\d+)\\/(-?\\d+))( +(-?\\d+)\\/(-?\\d+)\\/(-?\\d+))?"

        // f vertex//normal vertex//normal vertex//normal ...
        private const val FACE_PATTERN4 =
            "f( +(-?\\d+)\\/\\/(-?\\d+))( +(-?\\d+)\\/\\/(-?\\d+))( +(-?\\d+)\\/\\/(-?\\d+))( +(-?\\d+)\\/\\/(-?\\d+))?"
    }

}
