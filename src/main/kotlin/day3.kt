import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2019/day/3] */
class WireIntersection : AdventOfCodeTask {
    private val coordRegex = Regex("([URDL])(\\d+)")

    override fun run(part2: Boolean): Any {
        val (wire1, wire2) = readInputLines("3.txt")
        val wire1coords = getWireCoordinates(wire1)
        val wire2coords = getWireCoordinates(wire2)
        val intersections = wire1coords.intersect(wire2coords)

        return if (part2) {
            intersections.map { wire1coords.indexOf(it) + wire2coords.indexOf(it) + 2 }.minOrNull()!!
        } else {
            intersections.map(Coordinate::distanceToCenter).minOrNull()!!
        }
    }

    private fun getWireCoordinates(wire: String): List<Coordinate> {
        val coords = mutableListOf<Coordinate>()
        var coord = Coordinate(0, 0)

        for (operation in wire.split(",")) {
            val (direction, length) = coordRegex.find(operation)!!.destructured
            val transform: (Coordinate) -> Coordinate = when (direction) {
                "U" -> Coordinate::north
                "R" -> Coordinate::east
                "D" -> Coordinate::south
                "L" -> Coordinate::west
                else -> throw IllegalAccessException("Unknown direction $direction")
            }

            repeat(length.toInt()) {
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