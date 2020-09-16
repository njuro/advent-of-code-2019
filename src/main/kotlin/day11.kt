import utils.Coordinate
import utils.Direction
import utils.IntcodeComputer
import utils.toStringRepresentation

/** [https://adventofcode.com/2019/day/11] */
class PaintingRobot : AdventOfCodeTask {

    override fun run(part2: Boolean): Any {
        val computer = IntcodeComputer()

        var outputCount = 0
        var direction = Direction.UP
        var position = Coordinate(0, 0)
        val fields = mutableMapOf(position to (if (part2) 1 else 0)).withDefault { 0 }
        val painted = mutableSetOf<Coordinate>()

        fun handleInput(): Int {
            return fields.getValue(position)
        }

        fun handleOutput(value: Int) {
            if (outputCount % 2 == 0) {
                fields[position] = value
                painted.add(position)
            } else {
                direction = if (value == 0) direction.turnLeft() else direction.turnRight()
                position = position.move(direction)
            }

            outputCount++
        }

        computer.compute(computer.parseInstructions("11.txt"), onInput = ::handleInput, onOutput = ::handleOutput)

        if (!part2) return painted.size

        val croppedField =
            fields.mapValues { if (it.value == 1) '#' else '.' }.filterValues { it == '#' }.withDefault { '.' }
        return croppedField.toStringRepresentation()
    }
}

fun main() {
    println(PaintingRobot().run(part2 = true))
}