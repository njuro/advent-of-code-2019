import utils.Coordinate
import utils.IntcodeComputer
import utils.toStringRepresentation

/** [https://adventofcode.com/2019/day/13] */
class ArcadeGame : AdventOfCodeTask {

    override fun run(part2: Boolean): Any {
        val computer = IntcodeComputer()
        val instructions = computer.parseInstructions("13.txt")
        val board = mutableMapOf<Coordinate, Char>().withDefault { '.' }
        val buffer = mutableListOf<Int>()
        var score = 0

        fun handleOutput(output: Int) {
            buffer.add(output)
            if (buffer.size == 3) {
                val (x, y, value) = buffer
                buffer.clear()

                if (part2 && x == -1 && y == 0) {
                    score = value
                    return
                }

                val tile = when (value) {
                    0 -> '.'
                    1 -> '#'
                    2 -> 'B'
                    3 -> 'P'
                    4 -> 'O'
                    else -> throw IllegalStateException("Unknown tile ID $value")
                }
                board[Coordinate(x, y)] = tile
            }
        }

        fun handleInput(): Int {
            val ball = board.filterValues { it == 'O' }.keys.first()
            val paddle = board.filterValues { it == 'P' }.keys.first()

            return when {
                ball.x > paddle.x -> 1
                ball.x < paddle.x -> -1
                else -> 0
            }
        }

        if (part2) {
            instructions[0] = 2
        }

        computer.compute(instructions, onInput = ::handleInput, onOutput = ::handleOutput)
        println(board.toStringRepresentation(offsetCoordinates = true))

        return if (part2) score else board.count { it.value == 'B' }
    }
}

fun main() {
    println(ArcadeGame().run(part2 = false))
}