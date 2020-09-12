package info.laht.krender.threekt

import info.laht.krender.mesh.TrimeshShape
import info.laht.krender.proxies.*
import info.laht.krender.util.ExternalSource
import info.laht.krender.util.FileSource
import info.laht.krender.util.RenderContext
import info.laht.threekt.Colors
import info.laht.threekt.Side
import info.laht.threekt.TextureWrapping
import info.laht.threekt.core.*
import info.laht.threekt.extras.objects.Water
import info.laht.threekt.geometries.*
import info.laht.threekt.loaders.OBJLoader
import info.laht.threekt.loaders.STLLoader
import info.laht.threekt.loaders.TextureLoader
import info.laht.threekt.materials.MaterialWithColor
import info.laht.threekt.materials.MaterialWithWireframe
import info.laht.threekt.materials.MeshBasicMaterial
import info.laht.threekt.materials.PointsMaterial
import info.laht.threekt.math.Color
import info.laht.threekt.math.Vector3
import info.laht.threekt.math.curves.CatmullRomCurve3
import info.laht.threekt.objects.Line
import info.laht.threekt.objects.Mesh
import info.laht.threekt.objects.Points
import org.joml.Matrix4fc
import org.joml.Quaternionfc
import org.joml.Vector3fc
import java.io.File
import kotlin.math.PI

internal open class ThreektProxy(
    val ctx: RenderContext
) : RenderProxy, WireframeProxy, ColorProxy, SpatialProxy, TextureProxy {

    val parentNode = Object3DImpl().apply { matrixAutoUpdate = false }
    private val childNode = Object3DImpl().apply { matrixAutoUpdate = false }

    init {
        ctx.invokeLater {
            parentNode.add(childNode)
        }
    }

    override fun setTranslate(v: Vector3fc) {
        ctx.invokeLater {
            parentNode.position.set(v)
            parentNode.updateMatrix()
        }
    }

    override fun setRotate(q: Quaternionfc) {
        ctx.invokeLater {
            parentNode.quaternion.set(q)
            parentNode.updateMatrix()
        }
    }

    override fun setTransform(m: Matrix4fc) {
        ctx.invokeLater {
            parentNode.matrix.set(m)
        }
    }

    override fun setOffsetTransform(offset: Matrix4fc) {
        ctx.invokeLater {
            childNode.matrix.set(offset)
        }
    }

    override fun setTexture(source: ExternalSource) {
        if (source is FileSource) {
            ctx.invokeLater {
                for (o in childNode.children) {
                    if (o is Mesh) {
                        val m = o.material
                        if (m is MeshBasicMaterial) {
                            m.map = TextureLoader.load(source.file.absolutePath)
                        }
                    }
                }
            }
        }
    }

    override fun setWireframe(flag: Boolean) {
        ctx.invokeLater {
            for (o in childNode.children) {
                if (o is Mesh) {
                    val material = o.material
                    if (material is MaterialWithWireframe) {
                        material.wireframe = flag
                    }
                }
            }
        }
    }

    override fun setColor(color: Int) {
        ctx.invokeLater {
            for (o in childNode.children) {
                if (o is MaterialObject) {
                    val material = o.material
                    if (material is MaterialWithColor) {
                        material.color.set(color)
                    }
                }
            }
        }
    }

    override fun setVisible(visible: Boolean) {
        ctx.invokeLater {
            childNode.visible = visible
        }
    }

    override fun dispose() {
        ctx.invokeLater {
            childNode.parent?.remove(childNode)
        }
    }

    protected fun attachChild(obj: Object3D) {
        childNode.add(obj)
    }

}

internal class ThreektPlaneProxy(
    ctx: RenderContext,
    width: Float,
    height: Float
) : ThreektProxy(ctx), PlaneProxy {

    private val geometry = PlaneBufferGeometry(width, height)
    private val mesh = Mesh(geometry)

    init {

        ctx.invokeLater {
            attachChild(mesh)
        }

    }

}

internal class ThreektBoxProxy(
    ctx: RenderContext,
    width: Float,
    height: Float,
    depth: Float
) : ThreektProxy(ctx), BoxProxy {

    private val geometry = BoxBufferGeometry(width, height, depth)
    private val mesh = Mesh(geometry)

    init {

        ctx.invokeLater {
            attachChild(mesh)
        }

    }

}

internal class ThreektSphereProxy(
    ctx: RenderContext,
    private var radius: Float
) : ThreektProxy(ctx), SphereProxy {

    private val geometry = SphereBufferGeometry(radius)
    private val mesh = Mesh(geometry)

    init {
        ctx.invokeLater {
            attachChild(mesh)
        }
    }

    override fun setRadius(radius: Float) {
        val scale = radius / this.radius
        geometry.scale(scale)
        this.radius = radius
    }

}

internal class ThreektCylinderProxy(
    ctx: RenderContext,
    private var radius: Float,
    private var height: Float
) : ThreektProxy(ctx), CylinderProxy {

    private val geometry = CylinderBufferGeometry(radius, height)
    private val mesh = Mesh(geometry)

    init {
        ctx.invokeLater {
            attachChild(mesh)
        }
    }

    override fun setRadius(radius: Float) {
        val scale = radius / this.radius
        geometry.scale(scale)
        this.radius = radius
    }

    override fun setHeight(height: Float) {
        val scale = (height / this.height)
        geometry.scale(1f, scale, 1f)
        this.height = height
    }

}

internal class ThreektTrimeshProxy private constructor(
    ctx: RenderContext
) : ThreektProxy(ctx), MeshProxy {

    constructor(ctx: RenderContext, trimesh: TrimeshShape) : this(ctx) {
        val geometry = BufferGeometry().apply {
            setIndex(IntBufferAttribute(trimesh.indices.toIntArray(), 1))
            addAttribute("position", FloatBufferAttribute(trimesh.vertices.toFloatArray(), 3))
            addAttribute("normal", FloatBufferAttribute(trimesh.normals.toFloatArray(), 3))
            addAttribute("uv", FloatBufferAttribute(trimesh.uvs.toFloatArray(), 3))
        }
        geometry.computeBoundingSphere()

        val mesh = Mesh(geometry).apply {
            material.side = Side.Double
        }
        ctx.invokeLater {
            attachChild(mesh)
        }
    }

    constructor(ctx: RenderContext, source: File, scale: Float) : this(ctx) {

        val mesh = when (val ext = source.extension.toLowerCase()) {
            "obj" -> OBJLoader().load(source.absolutePath)
            "stl" -> Mesh(STLLoader().load(source.absolutePath))
            else -> throw UnsupportedOperationException("Unsupported extension: $ext")
        }
        mesh.scale.set(scale, scale, scale)
        attachChild(mesh)

    }

}

internal class ThreektPointCloudProxy(
    ctx: RenderContext,
    pointSize: Float,
    points: List<Vector3fc>,
) : ThreektProxy(ctx), PointCloudProxy {

    init {

        var i = 0
        val positions = FloatArray(3 * points.size)
        points.forEach { v ->
            positions[i++] = v.x().toFloat()
            positions[i++] = v.y().toFloat()
            positions[i++] = v.z().toFloat()
        }

        val geometry = BufferGeometry()
        geometry.addAttribute("position", FloatBufferAttribute(positions, 3))

        val material = PointsMaterial().apply {
            size = pointSize
            vertexColors = Colors.Vertex
        }

        ctx.invokeLater {
            attachChild(Points(geometry, material))
        }

    }

}

internal class ThreektCurveProxy(
    ctx: RenderContext,
    private val radius: Float,
    points: List<Vector3fc>
) : ThreektProxy(ctx), CurveProxy {

    private val mesh = Mesh(
        TubeBufferGeometry(
            CatmullRomCurve3(points.map { Vector3().set(it) }),
            radius = radius
        )
    ).apply {
        material.side = Side.Double
    }

    init {
        ctx.invokeLater {
            attachChild(mesh)
        }
    }

    override fun update(points: List<Vector3fc>) {
        val curve = CatmullRomCurve3(points.map { Vector3().set(it) })
        ctx.invokeLater {
            mesh.geometry.dispose()
            mesh.geometry = TubeBufferGeometry(curve, radius = radius)
        }
    }
}

internal class ThreektLineProxy(
    ctx: RenderContext,
    points: List<Vector3fc>
) : ThreektProxy(ctx), LineProxy {

    private val line = Line(
        createGeometry(points)
    )

    init {
        ctx.invokeLater {
            attachChild(line)
        }
    }

    override fun update(points: List<Vector3fc>) {
        ctx.invokeLater {
            line.geometry.dispose()
            line.geometry = createGeometry(points)
        }
    }

    private companion object {

        fun createGeometry(points: List<Vector3fc>): BufferGeometry {
            return BufferGeometry().apply {
                addAttribute("position", FloatBufferAttribute(points.flatten(), 3))
            }
        }

    }

}

internal class ThreektWaterProxy(
    ctx: RenderContext,
    width: Float,
    height: Float
) : ThreektProxy(ctx), WaterProxy {

    val water: Water

    init {

        val texture =
            TextureLoader.load(
                ThreektWaterProxy::class.java.classLoader.getResource("textures/waternormals.jpg")!!.file
            ).apply {
                wrapS = TextureWrapping.Repeat
                wrapT = TextureWrapping.Repeat
            }

        val planeGeometry = PlaneBufferGeometry(width, height)
        water = Water(
            planeGeometry, Water.Options(
                alpha = 0.7f,
                waterNormals = texture,
                waterColor = Color(0x001e0f),
                sunColor = Color(0xffffff),
                textureWidth = 512,
                textureHeight = 512,
                sunDirection = Vector3(1, 1, 0).normalize(),
                distortionScale = 1f
            )
        )
        water.rotateX(-PI.toFloat() / 2)
        ctx.invokeLater {
            attachChild(water)
        }

    }

}
