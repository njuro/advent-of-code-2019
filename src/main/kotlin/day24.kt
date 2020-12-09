import utils.Coordinate
import utils.readInputLines
import utils.toStringRepresentation
import kotlin.math.pow

class Day24 : AdventOfCodeTask {

    private val levels = mutableMapOf<Int, MutableMap<Coordinate, Char>>()

    override fun run(part2: Boolean): Any {
        val originalMap = readInputLines("24.txt")
            .flatMapIndexed { y, row -> row.mapIndexed { x, c -> Coordinate(x, y) to c }  }
            .toMap().toMutableMap()
        levels[0] = originalMap

        return if (part2) {
            repeat(200) {
                turn()
            }
            countBugs()
        } else {
            val seen = mutableSetOf<String>()
            var repr = ""
            while(!seen.contains(repr)) {
                seen.add(repr)
                turn()
                repr = levels[0]!!.toStringRepresentation(true)
            }
            calculateBiodiversity()
        }


    }

    private fun calculateBiodiversity(): Int {
        return levels.values.sumBy { it.filterValues { c -> c == '#' }
            .keys.sumBy { coord -> 2.0.pow(coord.y * 5 + coord.x).toInt() } }
    }

    private fun countBugs(): Int {
        return levels.values.sumBy { it.values.count { c -> c == '#' } }
    }

    private fun turn() {
        levels.forEach { (level, map) ->
            val updated = map.toMutableMap()
            map.forEach { (coordinate, c) ->
                val count = coordinate.adjacent().values.count { map.getOrDefault(it, '.') == '#' }
                if (c == '#' && count != 1) {
                    updated[coordinate] = '.'
                }
                if (c == '.' && (count == 1 || count == 2)) {
                    updated[coordinate] = '#'
                }
            }
            levels[level] = updated
        }
    }

}

fun main() {
    println(Day24().run(part2 = false))
}
