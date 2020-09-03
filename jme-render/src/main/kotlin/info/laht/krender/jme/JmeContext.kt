package info.laht.krender.jme

import com.jme3.asset.AssetManager
import java.util.*
import java.util.function.Supplier

class JmeContext(
    assetManager: Supplier<AssetManager>
) {

    private val tasks: Queue<Runnable> = ArrayDeque()

    private val assetManager_: Supplier<AssetManager> = assetManager
    val assetManager by lazy { assetManager_.get() }

    @Synchronized
    fun invokeLater(task: Runnable) {
        tasks.add(task)
    }

    @Synchronized
    internal fun invokePendingTasks() {
        while (!tasks.isEmpty()) {
            val poll: Runnable = tasks.poll()
            poll.run()
        }
    }

}
