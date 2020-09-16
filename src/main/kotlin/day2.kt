import utils.IntcodeComputer

/** [https://adventofcode.com/2019/day/2] */
class Intcode : AdventOfCodeTask {
    private val computer = IntcodeComputer()
    private val instructions = computer.parseInstructions("2.txt")

    override fun run(part2: Boolean): Any {
        if (part2) {
            for (noun in 0..99) {
                for (verb in 0..99) {
                    if (computeWith(noun, verb) == 19690720) {
                        return 100 * noun + verb
                    }
                }
            }
        } else {
            return computeWith(12, 2)
        }

        throw IllegalStateException()
    }

    private fun computeWith(noun: Int, verb: Int): Int {
        val instructionCopy = instructions.toMutableList()
        instructionCopy[1] = noun.toLong()
        instructionCopy[2] = verb.toLong()

        computer.compute(instructionCopy)
        return instructionCopy[0].toInt()
    }
}

fun main() {
    println(Intcode().run(part2 = true))
}