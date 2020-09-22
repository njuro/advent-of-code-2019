import utils.IntcodeComputer

/** [https://adventofcode.com/2019/day/19] */
class TractorBeam : AdventOfCodeTask {

    override fun run(part2: Boolean): Any {
        val computer = IntcodeComputer()
        val instructions = computer.parseInstructions("19.txt")

        fun probeCoordinate(x: Int, y: Int): Int {
            val inputList = mutableListOf(x, y)
            var output = -1
            computer.compute(instructions.toMutableList(), { inputList.removeFirst() }, { output = it })
            return output
        }

        if (!part2) {
            var counter = 0
            for (x in 0..49) {
                for (y in 0..49) {
                    if (probeCoordinate(x, y) == 1) {
                        counter++
                    }
                }
            }
            return counter
        }

        var x = 0
        var y = 100
        while (true) {
            while (probeCoordinate(x, y) != 1) {
                x++
            }
            while (probeCoordinate(x + 99, y) == 1) {
                if (probeCoordinate(x, y + 99) == 1) {
                    return x * 10_000 + y
                }
                x++
            }
            y++
        }
    }
}

fun main() {
    println(TractorBeam().run(part2 = true))
}