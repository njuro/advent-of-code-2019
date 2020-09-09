/** [https://adventofcode.com/2019/day/2] */
class Intcode : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val ops = readInputBlock("2.txt").split(",").map { it.toInt() }.toMutableList()

        if (part2) {
            for (noun in 0..99) {
                for (verb in 0..99) {
                    if (compute(ops.toMutableList(), noun, verb) == 19690720) {
                        return 100 * noun + verb
                    }
                }
            }
        } else {
            return compute(ops.toMutableList(), 12, 2)
        }

        throw IllegalStateException()
    }

    private fun compute(ops: MutableList<Int>, noun: Int, verb: Int): Int {
        ops[1] = noun
        ops[2] = verb
        var i = 0
        while (true) {
            when (ops[i]) {
                1 -> {
                    ops[ops[i + 3]] = ops[ops[i + 1]] + ops[ops[i + 2]]
                    i += 4
                }
                2 -> {
                    ops[ops[i + 3]] = ops[ops[i + 1]] * ops[ops[i + 2]]
                    i += 4
                }
                99 -> break
                else -> throw IllegalStateException()
            }
        }

        return ops[0]
    }
}

fun main() {
    println(Intcode().run(part2 = true))
}