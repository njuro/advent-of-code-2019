import kotlin.math.abs

/** [https://adventofcode.com/2019/day/3] */
class WireIntersection : AdventOfCodeTask {
    private val coordRegex = Regex("([URDL])(\\d+)")

    private data class Coordinate(val x: Int, val y: Int) {
        fun distanceFromCenter(): Int {
            return abs(x) + abs(y)
        }
    }

    override fun run(part2: Boolean): Any {
        val (wire1, wire2) = readInputLines("3.txt")
        val wire1coords = getWireCoordinates(wire1)
        val wire2coords = getWireCoordinates(wire2)
        val intersections = wire1coords.intersect(wire2coords)

        return if (part2) {
            fun steps(coord: Coordinate): Int = wire1coords.indexOf(coord) + wire2coords.indexOf(coord) + 2
            steps(intersections.minByOrNull(::steps)!!)
        } else {
            intersections.minByOrNull(Coordinate::distanceFromCenter)!!.distanceFromCenter()
        }
    }

    private fun getWireCoordinates(wire: String): List<Coordinate> {
        val coords = mutableListOf<Coordinate>()
        var coord = Coordinate(0, 0)

        for (operation in wire.split(",")) {
            val (direction, length) = coordRegex.find(operation)!!.destructured
            val transform: (Coordinate) -> Coordinate = when (direction) {
                "U" -> {
                    { it.copy(y = it.y + 1) }
                }
                "R" -> {
                    { it.copy(x = it.x + 1) }
                }
                "D" -> {
                    { it.copy(y = it.y - 1) }
                }
                "L" -> {
                    { it.copy(x = it.x - 1) }
                }
                else -> throw IllegalStateException()
            }

            for (i in 1..length.toInt()) {
                coord = transform(coord)
                coords.add(coord)
            }
        }

        return coords
    }
}

fun main() {
    println(WireIntersection().run(part2 = true))
}