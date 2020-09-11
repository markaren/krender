package info.laht.krender.jme.proxy

import com.jme3.asset.AssetManager
import com.jme3.asset.plugins.FileLocator
import com.jme3.scene.*
import info.laht.krender.jme.JmeContext
import info.laht.krender.jme.extra.JmeUtils
import info.laht.krender.mesh.Trimesh
import info.laht.krender.proxies.MeshProxy
import org.joml.Matrix4fc
import java.io.File

internal class JmeMeshProxy : JmeProxy, MeshProxy {

    constructor(ctx: JmeContext, data: Trimesh) : super("mesh", ctx) {
        attachChild(create(data))
    }

    @JvmOverloads
    constructor(ctx: JmeContext, source: File, scale: Float, offset: Matrix4fc? = null) : super("mesh", ctx) {
        attachChild(create(ctx.assetManager, source, scale, offset))
    }

    companion object {

        private fun create(data: Trimesh): Spatial {
            val jmeMesh = Mesh()
            if (data.hasIndices()) {
//            mesh.generateIndices();
                jmeMesh.setBuffer(VertexBuffer.Type.Index, 1, data.indices.toIntArray())
            }
            jmeMesh.setBuffer(VertexBuffer.Type.Position, 3, data.vertices.toFloatArray())
            if (data.hasNormals()) {
//            mesh.computeVertexNormals();
                jmeMesh.setBuffer(VertexBuffer.Type.Normal, 3, data.normals.toFloatArray())
            }
            if (data.hasColors()) {
                jmeMesh.setBuffer(VertexBuffer.Type.Color, 3, data.colors.toFloatArray())
            }
            if (data.hasUvs()) {
                jmeMesh.setBuffer(VertexBuffer.Type.TexCoord, 2, data.uvs.toFloatArray())
            }
            jmeMesh.mode = Mesh.Mode.Triangles
            jmeMesh.updateCounts()
            jmeMesh.updateBound()
            return Geometry("", jmeMesh)
        }

        private fun create(
            assetManager: AssetManager,
            source: File,
            scale: Float,
            offset: Matrix4fc? = null
        ): Spatial {
            val extension: String = source.extension
            if (extension != "obj") {
                throw Exception("Unsupported format: $extension")
            }

            assetManager.registerLocator(source.absolutePath, FileLocator::class.java)
            assetManager.registerLocator("${source.absolutePath}${File.separatorChar}textures", FileLocator::class.java)

            val loadModel: Spatial = assetManager.loadModel(source.name)
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
