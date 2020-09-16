import utils.Coordinate
import utils.Direction
import utils.IntcodeComputer

/** [https://adventofcode.com/2019/day/15] */
class RepairDroid : AdventOfCodeTask {
    private class SearchFinished : Exception()

    override fun run(part2: Boolean): Any {
        val computer = IntcodeComputer()
        var position = Coordinate(0, 0)
        val board = mutableMapOf(position to '.')
        val visited = mutableSetOf(position)
        val path = mutableListOf<Direction>()
        var direction = Direction.UP

        fun handleInput(): Int {
            fun getNewDirection(): Direction {
                val neighbours = position.adjacent()

                return neighbours.entries.firstOrNull { !board.containsKey(it.value) }?.key
                    ?: path.removeLast().turnOpposite()
            }

            direction = getNewDirection()
            return when (direction) {
                Direction.UP -> 1
                Direction.RIGHT -> 4
                Direction.DOWN -> 2
                Direction.LEFT -> 3
            }
        }

        fun handleOutput(status: Int) {
            when (status) {
                0 -> {
                    board[position.move(direction)] = '#'
                }
                1 -> {
                    position = position.move(direction)
                    board[position] = '.'
                    if (!visited.contains(position)) {
                        path.add(direction)
                    }
                    visited.add(position)
                }
                2 -> {
                    position = position.move(direction)
                    board[position] = 'O'
                    if (!visited.contains(position)) {
                        path.add(direction)
                    }
                    visited.add(position)
                    if (!part2) {
                        throw SearchFinished()
                    }
                }
                else -> throw IllegalStateException("Invalid status $status")
            }
        }

        try {
            computer.compute(computer.parseInstructions("15.txt"), onInput = ::handleInput, onOutput = ::handleOutput)
        } catch (ex: SearchFinished) {
            // oxygen station found
        } catch (ex: NoSuchElementException) {
            // whole maze discovered
        }

        if (!part2) return path.size

        var minutes = 0
        while (board.filterValues { it == '.' }.isNotEmpty()) {
            minutes++
            board.filterValues { it == 'O' }
                .flatMap { it.key.adjacent().map { pair -> pair.value } }
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