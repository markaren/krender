package info.laht.krender.util

import java.io.File
import java.net.URL

interface InputSource {

    val extension: String

    fun readBytes(): ByteArray

    fun readText(): String

}

abstract class ExternalSource : InputSource {

    abstract val fullPath: String

    val location: String
        get() = fullPath.replace(filename, "")

    val filename: String
        get() = "$baseName.$extension"

    val baseName: String
        get() {
            val index = filename.lastIndexOf(File.separatorChar)
            return fullPath.substring(index + 1)
        }

}

class FileSource(
    val file: File
) : ExternalSource() {

    override val extension: String
        get() = file.extension

    override val fullPath: String
        get() = file.absolutePath

    override fun readBytes(): ByteArray {
        return file.readBytes()
    }

    override fun readText(): String {
        return file.readText()
    }

}

class URLSource(
    val url: URL
) : ExternalSource() {

    override val extension: String
        get() = TODO("Not yet implemented")

    override val fullPath: String
        get() = url.toExternalForm()

    override fun readBytes(): ByteArray {
        return url.openStream().buffered().use { it.readBytes() }
    }

    override fun readText(): String {
        return url.openStream().bufferedReader().use { it.readText() }
    }

}

class StringSource(
    private val string: String,
    override val extension: String
) : InputSource {

    override fun readBytes(): ByteArray {
        throw UnsupportedOperationException()
    }

    override fun readText(): String {
        return string
    }

}
