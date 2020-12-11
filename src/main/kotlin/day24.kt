import utils.Coordinate
import utils.readInputLines
import utils.toStringRepresentation
import kotlin.math.pow

/** [https://adventofcode.com/2019/day/24] */
class Bugs : AdventOfCodeTask {

    private val maxX = 4
    private val maxY = 4
    private val center = Coordinate(maxX / 2, maxY / 2)
    private val rounds = 200

    private lateinit var levels: MutableMap<Int, MutableMap<Coordinate, Char>>
    private lateinit var emptyLevel: MutableMap<Coordinate, Char>

    override fun run(part2: Boolean): Any {
        val originalLevel = readInputLines("24.txt")
            .flatMapIndexed { y, row -> row.mapIndexed { x, c -> Coordinate(x, y) to c } }
            .toMap().toMutableMap()
        emptyLevel = originalLevel.mapValues { '.' }.toMutableMap()

        levels = if (part2) (-(rounds / 2)..rounds / 2).map { it to emptyLevel.toMutableMap() }.toMap()
            .toMutableMap() else mutableMapOf()
        levels[0] = originalLevel

        return if (part2) {
            repeat(rounds) {
                turn(recursive = true)
            }
            countBugs()
        } else {
            val seen = mutableSetOf<String>()
            var repr = ""
            while (!seen.contains(repr)) {
                seen.add(repr)
                turn(recursive = false)
                repr = levels[0]!!.toStringRepresentation(true)
            }
            calculateBiodiversity()
        }
    }

    private fun turn(recursive: Boolean) {
        val updatedLevels = levels.toMutableMap()
        levels.forEach { (level, map) ->
            val updated = map.toMutableMap()
            map.forEach { (coordinate, c) ->
                val adjacent = getAdjacent(coordinate, level, recursive)
                val count = adjacent.count { (adj, lvl) ->
                    levels.getOrDefault(lvl, emptyLevel.toMutableMap()).getOrDefault(adj, '.') == '#'
                }
                if (c == '#' && count != 1) {
                    updated[coordinate] = '.'
                }
                if (c == '.' && (count == 1 || count == 2)) {
                    updated[coordinate] = '#'
                }
            }
            updatedLevels[level] = updated
        }
        levels = updatedLevels
    }

    private fun getAdjacent(coordinate: Coordinate, level: Int, recursive: Boolean): List<Pair<Coordinate, Int>> {
        val adjacent = coordinate.adjacent().values
        return if (recursive) {
            adjacent.flatMap {
                when {
                    (it.x < 0) -> listOf(center.left() to level - 1)
                    (it.x > maxX) -> listOf(center.right() to level - 1)
                    (it.y < 0) -> listOf(center.up(offset = true) to level - 1)
                    (it.y > maxY) -> listOf(center.down(offset = true) to level - 1)
                    (it == center) -> when {
                        (coordinate.x == center.left().x) -> (0..maxY).map { y -> Coordinate(0, y) to level + 1 }
                        (coordinate.x == center.right().x) -> (0..maxY).map { y -> Coordinate(maxX, y) to level + 1 }
                        (coordinate.y == center.up(offset = true).y) -> (0..maxX).map { x ->
                            Coordinate(
                                x,
                                0
                            ) to level + 1
                        }
                        (coordinate.y == center.down(offset = true).y) -> (0..maxX).map { x ->
                            Coordinate(
                                x,
                                maxY
                            ) to level + 1
                        }
                        else -> throw IllegalStateException("${coordinate.x} ${coordinate.y}")
                    }
                    else -> listOf(it to level)
                }
            }
        } else {
            adjacent.map { it to level }
        }
    }

    private fun countBugs(): Int {
        return levels.values.sumBy { it.filterKeys { it != Coordinate(2, 2) }.values.count { c -> c == '#' } }
    }

    private fun calculateBiodiversity(): Int {
        return levels.values.sumBy {
            it.filterValues { c -> c == '#' }
                .keys.sumBy { coord -> 2.0.pow(coord.y * 5 + coord.x).toInt() }
        }
    }
}

fun main() {
    println(Bugs().run(part2 = true))
}
