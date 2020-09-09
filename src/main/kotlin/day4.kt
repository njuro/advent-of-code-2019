/** [https://adventofcode.com/2019/day/4] */
class ContainerPassword : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val (min, max) = readInputBlock("4.txt").split("-")
        return IntRange(min.toInt(), max.toInt()).filter { isValid(it, onlyRepeatedTwice = part2) }.size
    }

    private fun isValid(number: Int, onlyRepeatedTwice: Boolean): Boolean {
        val numberString = number.toString()
        val sortedNumberString = numberString.toCharArray().sorted().joinToString("")
        if (numberString != sortedNumberString) {
            return false
        }

        val tokens = mutableListOf<List<Char>>()
        var token: Char = sortedNumberString[0]
        var group = mutableListOf<Char>()
        for (ch in numberString) {
            if (ch != token) {
                tokens.add(group)
                token = ch
                group = mutableListOf()
            }

            group.add(ch)
        }
        tokens.add(group)

        return tokens.any { if (onlyRepeatedTwice) it.size == 2 else it.size >= 2 }
    }
}

fun main() {
    println(ContainerPassword().run(part2 = true))
}