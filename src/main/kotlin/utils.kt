import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun readInputLines(filename: String): List<String> {
    return Files.readAllLines(getFilepath(filename))
}

fun readInputBlock(filename: String): String {
    return Files.readString(getFilepath(filename))
}

private fun getFilepath(filename: String): Path {
    return Paths.get("src", "main", "resources", "$filename.txt")
}
