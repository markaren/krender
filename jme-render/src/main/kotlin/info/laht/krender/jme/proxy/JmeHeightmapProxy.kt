package info.laht.krender.jme.proxy

import com.jme3.renderer.queue.RenderQueue
import com.jme3.scene.Geometry
import info.laht.krender.jme.JmeContext
import info.laht.krender.jme.extra.JmeGrid
import info.laht.krender.proxies.HeightmapProxy

internal class JmeHeightmapProxy(
    ctx: JmeContext,
    widthSegments: Int,
    heightSegments: Int,
    width: Float,
    height: Float
) : JmeProxy("Heightmap", ctx), HeightmapProxy {

    private var grid: JmeGrid

    init {
        attachChild(Geometry("", JmeGrid(widthSegments, heightSegments, width, height).also { grid = it }))
        setQueueBucket(RenderQueue.Bucket.Transparent)
    }

    override fun setHeights(heights: FloatArray) {
        ctx.invokeLater { grid.setHeights(heights) }
    }

}
