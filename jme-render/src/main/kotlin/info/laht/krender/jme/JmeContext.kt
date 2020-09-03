package info.laht.krender.jme

import com.jme3.asset.AssetManager
import java.util.*

class JmeContext(
    val assetManager: AssetManager
) {

    private val tasks: Queue<() -> Unit> = ArrayDeque()

    @Synchronized
    fun invokeLater(task: () -> Unit) {
        tasks.add(task)
    }

    @Synchronized
    internal fun invokePendingTasks() {
        while (!tasks.isEmpty()) {
            tasks.poll().invoke()
        }
    }

}
