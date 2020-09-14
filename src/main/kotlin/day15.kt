/** [https://adventofcode.com/2019/day/15] */
class RepairDroid : AdventOfCodeTask {
    private class SearchFinished : Exception()

    private data class Coordinate(val x: Int, val y: Int) {
        fun neighbours(): List<Pair<Int, Coordinate>> {
            return (1..4).map { it to adjacent(it) }
        }

        fun adjacent(direction: Int): Coordinate {
            return when (direction) {
                1 -> copy(y = y + 1)
                2 -> copy(y = y - 1)
                3 -> copy(x = x - 1)
                4 -> copy(x = x + 1)
                else -> throw IllegalStateException("Unknown direction $direction")
            }
        }
    }

    override fun run(part2: Boolean): Any {
        val computer = ExtendedIntCode()
        val ops = computer.parseOps("15.txt")
        var position = Coordinate(0, 0)
        val board = mutableMapOf(position to '.')
        val visited = mutableSetOf(position)
        val path = mutableListOf<Int>()
        var direction = 1

        fun handleInput(): Long {
            fun getNewDirection(): Int {
                val neighbours = position.neighbours()

                return neighbours.firstOrNull { !board.containsKey(it.second) }?.first
                    ?: when (path.removeLast()) {
                        1 -> 2
                        2 -> 1
                        3 -> 4
                        4 -> 3
                        else -> throw IllegalStateException("Unknown direction $direction")
                    }
            }


            direction = getNewDirection()
            return direction.toLong()
        }

        fun handleOutput(status: Long) {
            when (status.toInt()) {
                0 -> {
                    board[position.adjacent(direction)] = '#'
                }
                1 -> {
                    position = position.adjacent(direction)
                    board[position] = '.'
                    if (!visited.contains(position)) {
                        path.add(direction)
                    }
                    visited.add(position)
                }
                2 -> {
                    position = position.adjacent(direction)
                    board[position] = 'O'
                    path.add(direction)
                    throw SearchFinished()
                }
                else -> throw IllegalStateException("Invalid status $status")
            }
        }

        try {
            computer.compute(ops, onInput = ::handleInput, onOutput = ::handleOutput)
        } catch (ex: SearchFinished) {
            // pass
        }

        if (!part2) return path.size

        var minutes = 0
        while (board.filterValues { it == '.' }.isNotEmpty()) {
            minutes++
            board.filterValues { it == 'O' }
                .flatMap { it.key.neighbours().map { pair -> pair.second } }
                .filter { cell -> board.getOrDefault(cell, ' ') == '.' }.forEach { cell ->
                    board[cell] = 'O'
                }
        }

        return minutes
    }
}

fun main() {
    println(RepairDroid().run(part2 = true))
}