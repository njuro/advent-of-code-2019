package utils

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

data class Coordinate(val x: Int, val y: Int) {

    fun distanceToCenter(): Int {
        return distanceTo(Coordinate(0, 0))
    }

    fun distanceTo(other: Coordinate): Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    fun angleTo(other: Coordinate, inDegrees: Boolean = false): Double {
        val radians = atan2(
            (other.y - y).toDouble(),
            (other.x - x).toDouble()
        )

        return if (inDegrees) (radians * 180 / PI + 90).let { if (it < 0) it + 360 else it } else radians
    }

    fun up(delta: Int = 1): Coordinate {
        return copy(y = y + delta)
    }

    fun right(delta: Int = 1): Coordinate {
        return copy(x = x + delta)
    }

    fun down(delta: Int = 1): Coordinate {
        return copy(y = y - delta)
    }

    fun left(delta: Int = 1): Coordinate {
        return copy(x = x - delta)
    }

    fun move(direction: Direction): Coordinate {
        return when (direction) {
            Direction.UP -> up()
            Direction.RIGHT -> right()
            Direction.DOWN -> down()
            Direction.LEFT -> left()
        }
    }

    fun adjacent(): Map<Direction, Coordinate> {
        return Direction.values().associateWith(::move)
    }
}

fun Map<Coordinate, Char>.minX(): Int {
    return minByOrNull { it.key.x }!!.key.x
}

fun Map<Coordinate, Char>.maxX(): Int {
    return maxByOrNull { it.key.x }!!.key.x
}

fun Map<Coordinate, Char>.minY(): Int {
    return minByOrNull { it.key.y }!!.key.y
}

fun Map<Coordinate, Char>.maxY(): Int {
    return maxByOrNull { it.key.y }!!.key.y
}

fun Map<Coordinate, Char>.toStringRepresentation(offsetCoordinates: Boolean = false): String {
    val map = StringBuilder()

    val yRange = if (offsetCoordinates) minY()..maxY() else maxY() downTo minY()
    for (y in yRange) {
        for (x in minX()..maxX()) {
            map.append(getValue(Coordinate(x, y)))
            map.append(" ")
        }
        map.append('\n')
    }

    return map.toString().trimEnd()
}
