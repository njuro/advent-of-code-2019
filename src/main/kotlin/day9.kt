/** [https://adventofcode.com/2019/day/9] */
class BoostCode : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val computer = ExtendedIntCode()
        val ops = computer.parseOps("9.txt")
        return computer.compute(ops, onInput = { if (part2) 2 else 1 })
    }
}

fun main() {
    println(BoostCode().run(part2 = true))
}