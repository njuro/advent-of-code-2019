/** [https://adventofcode.com/2019/day/8] */
class ImageFormat : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val width = 25
        val height = 6
        val layers = readInputBlock("8.txt").chunked(width * height)

        if (!part2) {
            return layers.minByOrNull { it.count { ch -> ch == '0' } }!!
                .let { it.count { ch -> ch == '1' } * it.count { ch -> ch == '2' } }
        }

        fun combineLayers(first: String, second: String): String {
            return first.mapIndexed { index, c -> if (c == '2') second[index] else c }.joinToString("")
        }

        fun drawRow(row: String): String {
            return row.map { c -> if (c == '1') '#' else ' ' }.joinToString("")
        }

        return layers.reduce(::combineLayers).chunked(width).joinToString("\n", transform = ::drawRow)
    }
}

fun main() {
    println(ImageFormat().run(part2 = true))
}