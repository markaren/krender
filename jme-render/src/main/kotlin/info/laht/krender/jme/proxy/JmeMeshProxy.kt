package info.laht.krender.jme.proxy

import com.jme3.asset.AssetManager
import com.jme3.asset.plugins.FileLocator
import com.jme3.asset.plugins.UrlLocator
import com.jme3.scene.*
import info.laht.krender.Trimesh
import info.laht.krender.jme.JmeContext
import info.laht.krender.jme.extra.JmeUtils
import info.laht.krender.proxies.MeshProxy
import info.laht.krender.util.ExternalSource
import info.laht.krender.util.FileSource
import info.laht.krender.util.URLSource
import org.joml.Matrix4dc

class JmeMeshProxy : JmeProxy, MeshProxy {

    constructor(ctx: JmeContext, data: Trimesh) : super("mesh", ctx) {
        attachChild(create(data))
    }

    constructor(ctx: JmeContext, source: ExternalSource, scale: Double, offset: Matrix4dc?) : super("mesh", ctx) {
        attachChild(create(ctx.assetManager, source, scale, offset))
    }

    companion object {

        private fun create(data: Trimesh): Spatial {
            val jmeMesh = Mesh()
            if (data.hasIndices()) {
//            mesh.generateIndices();
                jmeMesh.setBuffer(VertexBuffer.Type.Index, 1, data.indices.toIntArray())
            }
            jmeMesh.setBuffer(VertexBuffer.Type.Position, 3, data.vertices.map { it.toFloat() }.toFloatArray())
            if (data.hasNormals()) {
//            mesh.computeVertexNormals();
                jmeMesh.setBuffer(VertexBuffer.Type.Normal, 3, data.normals.map { it.toFloat() }.toFloatArray())
            }
            if (data.hasColors()) {
                jmeMesh.setBuffer(VertexBuffer.Type.Color, 3, data.colors.toFloatArray())
            }
            if (data.hasUvs()) {
                jmeMesh.setBuffer(VertexBuffer.Type.TexCoord, 2, data.uvs.map { it.toFloat() }.toFloatArray())
            }
            jmeMesh.mode = Mesh.Mode.Triangles
            jmeMesh.updateCounts()
            jmeMesh.updateBound()
            return Geometry("", jmeMesh)
        }

        private fun create(
            assetManager: AssetManager,
            source: ExternalSource,
            scale: Double,
            offset: Matrix4dc?
        ): Spatial {
            val extension: String = source.extension
            if (extension != "obj") {
                throw Exception("Unsupported format: $extension")
            }
            when (source) {
                is FileSource -> {
                    assetManager.registerLocator(source.location, FileLocator::class.java)
                    //            assetManager.registerLocator(((FileSource) source).getLocation() + "/textures", FileLocator.class);
                }
                is URLSource -> {
                    assetManager.registerLocator(source.location, UrlLocator::class.java)
                    //            assetManager.registerLocator(((URLSource) source).getLocation() + "/textures", UrlLocator.class);
                }
                else -> {
                    throw UnsupportedOperationException()
                }
            }
            val loadModel: Spatial = assetManager.loadModel(source.filename)
            loadModel.scale(scale.toFloat())
            if (offset != null) {
                loadModel.localTransform = JmeUtils.convertT(offset)
            }
            return Node().apply {
                attachChild(loadModel)
            }
        }
    }
}
