import java.lang.IllegalStateException
import kotlin.math.abs

val coordRegex = Regex("([URDL])(\\d+)")

data class Coordinate(val x: Int, val y: Int) {
    fun distanceFromCenter(): Int {
        return abs(x) + abs(y)
    }
}

fun wireCoordinates(wire: String): List<Coordinate> {
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

fun main() {
    val (wire1, wire2) = readInputLines("3")
    val wire1coords = wireCoordinates(wire1)
    val wire2coords = wireCoordinates(wire2)
    val intersections = wire1coords.intersect(wire2coords)

    val part2 = true
    if (part2) {
        fun steps(coord: Coordinate): Int = wire1coords.indexOf(coord) + wire2coords.indexOf(coord) + 2
        println(steps(intersections.minByOrNull(::steps)!!))
    } else {
        println(intersections.minByOrNull(Coordinate::distanceFromCenter)!!.distanceFromCenter())
    }
}