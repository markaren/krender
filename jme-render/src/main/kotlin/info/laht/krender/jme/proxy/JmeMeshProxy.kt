package info.laht.krender.jme.proxy

import com.jme3.asset.AssetManager
import com.jme3.asset.plugins.FileLocator
import com.jme3.asset.plugins.UrlLocator
import com.jme3.scene.*
import info.laht.krender.jme.JmeContext
import info.laht.krender.jme.extra.JmeUtils
import info.laht.krender.mesh.Trimesh
import info.laht.krender.proxies.MeshProxy
import info.laht.krender.util.ExternalSource
import info.laht.krender.util.FileSource
import info.laht.krender.util.URLSource
import org.joml.Matrix4fc

internal class JmeMeshProxy : JmeProxy, MeshProxy {

    constructor(ctx: JmeContext, data: Trimesh) : super("mesh", ctx) {
        attachChild(create(data))
    }

    @JvmOverloads
    constructor(ctx: JmeContext, source: ExternalSource, scale: Float, offset: Matrix4fc? = null) : super("mesh", ctx) {
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
            scale: Float,
            offset: Matrix4fc? = null
        ): Spatial {
            val extension: String = source.extension
            if (extension != "obj") {
                throw Exception("Unsupported format: $extension")
            }
            when (source) {
                is FileSource -> {
                    assetManager.registerLocator(source.location, FileLocator::class.java)
                    assetManager.registerLocator(source.location + "/textures", FileLocator::class.java)
                }
                is URLSource -> {
                    assetManager.registerLocator(source.location, UrlLocator::class.java)
                    assetManager.registerLocator(source.location + "/textures", UrlLocator::class.java)
                }
                else -> {
                    throw UnsupportedOperationException("Source $source not supported!")
                }
            }
            val loadModel: Spatial = assetManager.loadModel(source.filename)
            loadModel.scale(scale)
            if (offset != null) {
                loadModel.localTransform = JmeUtils.convertT(offset)
            }
            return Node().apply {
                attachChild(loadModel)
            }
        }
    }
}
