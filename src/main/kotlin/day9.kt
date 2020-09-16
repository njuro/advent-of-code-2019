import utils.IntcodeComputer

/** [https://adventofcode.com/2019/day/9] */
class BoostCode : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val computer = IntcodeComputer()
        val instructions = computer.parseInstructions("9.txt")
        return computer.computeWithLongSupport(instructions, onInput = { if (part2) 2 else 1 })
    }
}

fun main() {
    println(BoostCode().run(part2 = true))
}