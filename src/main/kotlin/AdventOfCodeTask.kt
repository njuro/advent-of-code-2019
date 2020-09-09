import java.nio.file.Files
import java.nio.file.Path

interface AdventOfCodeTask {
    fun run(part2: Boolean = false): Any

    fun readInputLines(filename: String): List<String> {
        return Files.readAllLines(getFilePath(filename))
    }

    fun readInputBlock(filename: String): String {
        return Files.readString(getFilePath(filename))
    }

    private fun getFilePath(filename: String): Path {
        return Path.of("src", "main", "resources", filename)
    }
}