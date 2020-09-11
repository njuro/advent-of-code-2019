/** [https://adventofcode.com/2019/day/11] */
class PaintingRobot : AdventOfCodeTask {
    private data class Coordinate(val x: Int, val y: Int)

    override fun run(part2: Boolean): Any {
        val computer = ExtendedIntCode()
        val ops = computer.parseOps("11.txt")

        var outputCount = 0
        var direction = 0
        var position = Coordinate(0, 0)
        val fields = mutableMapOf(position to (if (part2) 1 else 0)).withDefault { 0 }
        val painted = mutableSetOf<Coordinate>()

        fun handleInput(): Long {
            return fields.getValue(position).toLong()
        }

        fun handleOutput(value: Long) {
            if (outputCount % 2 == 0) {
                fields[position] = value.toInt()
                painted.add(position)
            } else {
                direction = changeDirection(direction, value)
                position = changePosition(position, direction)
            }
            outputCount++
        }

        computer.compute(ops, onInput = ::handleInput, onOutput = ::handleOutput)

        if (!part2) return painted.size

        val whiteFields = fields.filter { it.value == 1 }.map { it.key }
        val minY = whiteFields.minByOrNull(Coordinate::y)!!.y
        val maxY = whiteFields.maxByOrNull(Coordinate::y)!!.y
        val minX = whiteFields.minByOrNull(Coordinate::x)!!.x
        val maxX = whiteFields.maxByOrNull(Coordinate::x)!!.x
        var field = ""
        for (y in maxY downTo minY) {
            for (x in minX..maxX) {
                field += if (fields.getValue(Coordinate(x, y)) == 0) ". " else "# "
            }
            field += "\n"
        }

        return field.trimEnd()
    }

    private fun changeDirection(current: Int, change: Long): Int {
        return (current + if (change == 0L) -1 else 1).let { if (it == -1) 3 else if (it == 4) 0 else it }
    }

    private fun changePosition(current: Coordinate, direction: Int): Coordinate {
        return when (direction) {
            0 -> Coordinate(current.x, current.y + 1)
            1 -> Coordinate(current.x + 1, current.y)
            2 -> Coordinate(current.x, current.y - 1)
            3 -> Coordinate(current.x - 1, current.y)
            else -> throw IllegalStateException("Unknown direction $direction")
        }
    }
}

fun main() {
    println(PaintingRobot().run(part2 = true))
}