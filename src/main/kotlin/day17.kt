import utils.Coordinate
import utils.Direction
import utils.IntcodeComputer
import utils.toStringRepresentation

class Scaffolding : AdventOfCodeTask {

    private class Robot(var position: Coordinate, var direction: Direction) {
        fun nextPosition(): Coordinate {
            return position.move(direction, offset = true)
        }
    }

    private class RobotRoutines(
        val main: List<String>,
        val a: List<String>,
        val b: List<String>,
        val c: List<String>,
        val live: Boolean = false
    ) {

        private val instructions: MutableList<Char> = encode().toMutableList()

        fun getNextInstruction(): Char {
            return instructions.removeFirst()
        }

        fun encode(): List<Char> {
            return listOf(main, a, b, c).joinToString("\n") { it.joinToString(",") }
                .plus("\n${if (live) "y" else "n"}\n").toCharArray().toList()
        }
    }

    private val memory = mutableMapOf<Coordinate, Char>().withDefault { ' ' }
    private lateinit var memoryPointer: Coordinate

    private lateinit var robot: Robot
    private lateinit var robotRoutines: RobotRoutines

    private var robotInitialized = false
    private var robotInitialDirection: Direction? = null
    private var robotInitialPosition: Coordinate? = null

    override fun run(part2: Boolean): Any {
        resetProperties()

        val computer = IntcodeComputer()
        val instructions = computer.parseInstructions("17.txt")

        if (part2) {
            instructions[0] = 2
        }
        val cleaned = computer.compute(instructions, onInput = ::sendInstruction, onOutput = ::receiveValue)

        println(memory.toStringRepresentation(offsetCoordinates = true, separator = ""))

        return if (part2) cleaned else memory.filterKeys(::isIntersection).keys.sumBy { it.x * it.y }
    }

    private fun resetProperties() {
        memory.clear()
        memoryPointer = Coordinate(0, 0)
        robotInitialized = false
    }

    private fun sendInstruction(): Int {
        if (!robotInitialized) {
            robot = Robot(robotInitialPosition!!, robotInitialDirection!!)
            loadInstructions()
            robotInitialized = true
        }

        return robotRoutines.getNextInstruction().toInt()
    }

    private fun receiveValue(value: Int) {
        val c = value.toChar()
        if (c == '\n') {
            memoryPointer = memoryPointer.copy(x = 0, y = memoryPointer.y + 1)
            return
        } else {
            memory[memoryPointer] = c

            robotInitialDirection = when (c) {
                '^' -> Direction.UP
                '>' -> Direction.RIGHT
                'v' -> Direction.DOWN
                '<' -> Direction.LEFT
                else -> robotInitialDirection
            }
            if (robotInitialDirection != null && robotInitialPosition == null) {
                robotInitialPosition = memoryPointer.copy()
            }

            memoryPointer = memoryPointer.copy(x = memoryPointer.x + 1)
        }
    }

    private fun loadInstructions() {
        val scaffoldingCount = memory.count { it.value == '#' }
        val visited = mutableSetOf<Coordinate>()
        val instructions = mutableListOf<String>()
        var stepInstruction = 0

        fun isNextPositionValid() =
            memory[robot.nextPosition()] == '#' && (!visited.contains(robot.nextPosition())) || isIntersection(robot.nextPosition())

        while (visited.size < scaffoldingCount) {
            if (memory[robot.position] == '#') {
                visited.add(robot.position)
            }

            if (isNextPositionValid()) {
                stepInstruction++
                robot.position = robot.nextPosition()
            } else {
                if (stepInstruction > 0) {
                    instructions.add(stepInstruction.toString())
                    stepInstruction = 0
                }

                robot.direction = robot.direction.turnLeft()
                if (isNextPositionValid()) {
                    instructions.add("L")
                } else {
                    instructions.add("R")
                    robot.direction = robot.direction.turnOpposite()
                }
            }
        }
        if (arrayOf("R", "L").contains(instructions.last())) {
            instructions.removeLast()
        }


        parseRoutines(instructions)
    }

    private fun parseRoutines(instructions: List<String>) {
        // TODO automatically divide instructions into routines

        robotRoutines =
            RobotRoutines(
                main = listOf("A", "B", "A", "C", "C", "A", "B", "C", "B", "B"),
                a = listOf("L", "8", "R", "10", "L", "8", "R", "8"),
                b = listOf("L", "12", "R", "8", "R", "8"),
                c = listOf("L", "8", "R", "6", "R", "6", "R", "10", "L", "8")
            )
    }

    private fun isIntersection(coord: Coordinate): Boolean {
        val sc = listOf('#', 'O')
        return sc.contains(memory.getValue(coord)) && coord.adjacent().all { sc.contains(memory.getValue(it.value)) }
    }
}

fun main() {
    println(Scaffolding().run(part2 = true))
}