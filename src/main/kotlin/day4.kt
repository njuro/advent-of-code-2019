import utils.readInputBlock

/** [https://adventofcode.com/2019/day/4] */
class ContainerPassword : AdventOfCodeTask {

    override fun run(part2: Boolean): Any {
        val (min, max) = readInputBlock("4.txt").split("-")

        return IntRange(min.toInt(), max.toInt()).filter { isValid(it, onlyRepeatedTwice = part2) }.size
    }

    private fun isValid(number: Int, onlyRepeatedTwice: Boolean): Boolean {
        fun Int.isSorted() = toString() == toString().toCharArray().sorted().joinToString("")
        if (!number.isSorted()) {
            return false
        }

        fun List<Char>.hasValidSize() = if (onlyRepeatedTwice) size == 2 else size >= 2
        val group = mutableListOf<Char>()
        for (ch in number.toString()) {
            if (ch != group.lastOrNull()) {
                if (group.hasValidSize()) {
                    return true
                }
                group.clear()
            }
            group.add(ch)
        }

        return group.hasValidSize()
    }
}

fun main() {
    println(ContainerPassword().run(part2 = true))
}