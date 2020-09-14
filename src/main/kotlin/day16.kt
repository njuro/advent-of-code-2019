import kotlin.math.abs

/** [https://adventofcode.com/2019/day/16] */
class SignalTransmission : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val data = readInputBlock("16.txt")
        val sb = StringBuilder(data)
        if (part2) {
            repeat(9999) {
                sb.append(data)
            }
        }

        var input = sb.toString().map(Character::getNumericValue).toMutableList()
        val offset = if (part2) input.subList(0, 7).joinToString("").toInt() else 0
        repeat(100) {
            input = processSignal(input, offset)
        }

        return input.joinToString("").trimStart('0').substring(0 + offset, 8 + offset)
    }

    private fun processSignal(input: MutableList<Int>, offset: Int): MutableList<Int> {
        val basePattern = listOf(0, 1, 0, -1)
        val result = mutableListOf<Int>()
        for (row in offset + 1..input.size) {
            val pattern = basePattern.flatMap { generateSequence { it }.take(row) }
            result.add(abs(input.mapIndexed { index, number -> number * pattern[(index + 1) % pattern.size] }
                .sum()) % 10)
        }
        return result
    }
}

fun main() {
    println(SignalTransmission().run(part2 = false))
}