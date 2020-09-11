/** [https://adventofcode.com/2019/day/13] */
class ArcadeGame : AdventOfCodeTask {
    private data class Coordinate(val x: Int, val y: Int)

    override fun run(part2: Boolean): Any {
        val computer = ExtendedIntCode()
        val ops = computer.parseOps("13.txt")
        val board = mutableMapOf<Coordinate, Char>().withDefault { '.' }
        val buffer = mutableListOf<Long>()
        var score = 0

        fun handleOutput(output: Long) {
            buffer.add(output)
            if (buffer.size == 3) {
                val (x, y, value) = buffer
                buffer.clear()

                if (part2 && x == -1L && y == 0L) {
                    score = value.toInt()
                    return
                }

                val tile = when (value.toInt()) {
                    0 -> '.'
                    1 -> '#'
                    2 -> 'B'
                    3 -> 'P'
                    4 -> 'O'
                    else -> throw IllegalStateException("Unknown tile ID $value")
                }
                board[Coordinate(x.toInt(), y.toInt())] = tile
            }
        }

        fun sendJoyStickCommand(): Long {
            val ball = board.filterValues { it == 'O' }.keys.first()
            val paddle = board.filterValues { it == 'P' }.keys.first()

            return when {
                ball.x > paddle.x -> 1
                ball.x < paddle.x -> -1
                else -> 0
            }
        }

        if (part2) {
            ops[0] = 2
        }

        computer.compute(ops, onInput = ::sendJoyStickCommand, onOutput = ::handleOutput)
        printBoard(board)

        return if (part2) score else board.count { it.value == 'B' }
    }

    private fun printBoard(board: Map<Coordinate, Char>) {
        val minX = board.minByOrNull { it.key.x }!!.key.x
        val maxX = board.maxByOrNull { it.key.x }!!.key.x
        val minY = board.minByOrNull { it.key.y }!!.key.y
        val maxY = board.maxByOrNull { it.key.y }!!.key.y
        var boardGrid = ""
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                boardGrid += board.getValue(Coordinate(x, y)) + " "
            }
            boardGrid += "\n"
        }

        println(boardGrid)
    }
}

fun main() {
    println(ArcadeGame().run(part2 = true))
}