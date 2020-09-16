import utils.IntcodeComputer

/** [https://adventofcode.com/2019/day/5] */
class ExtendedIntCode : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val computer = IntcodeComputer()
        return computer.compute(computer.parseInstructions("5.txt"), onInput = { if (part2) 5 else 1 })
    }
}

fun main() {
    println(ExtendedIntCode().run(part2 = true))
}