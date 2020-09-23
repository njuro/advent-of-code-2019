import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2019/day/20] */
class Teleports : AdventOfCodeTask {

    private val map = mutableMapOf<Coordinate, Char>().withDefault { ' ' }
    private val teleports = mutableMapOf<String, MutableSet<Coordinate>>().withDefault { mutableSetOf() }
    private var shortestPath = Int.MAX_VALUE

    override fun run(part2: Boolean): Any {
        readInputLines("20.txt").forEachIndexed { y, row ->
            row.forEachIndexed { x, c -> map[Coordinate(x, y)] = c }
        }
        loadTeleports()
        findShortestPath()
        return shortestPath
    }

    private fun findShortestPath() {
        val visited = mutableSetOf<Coordinate>()
        val queue = mutableListOf(teleports.getValue("AA").elementAt(0) to 0)
        while (queue.isNotEmpty()) {
            val (position, steps) = queue.removeFirst()
            if (visited.contains(position)) {
                continue
            }
            visited.add(position)

            if (map.getValue(position) != '.') {
                continue
            }

            if (position == teleports.getValue("ZZ").elementAt(0)) {
                if (steps < shortestPath) {
                    shortestPath = steps
                }
                continue
            }

            for (neighbour in position.adjacent(offset = true).values) {
                var newPosition = neighbour
                if (map.getValue(neighbour).isUpperCase()) {
                    newPosition = teleports.values.filter { it.size > 1 }.firstOrNull { it.contains(position) }
                        ?.first { it != position } ?: neighbour
                }
                queue.add(newPosition to steps + 1)
            }
        }
    }

    private fun loadTeleports() {
        map.filter { it.value.isUpperCase() && map.getValue(it.key.right()).isUpperCase() }
            .forEach { (coord, firstLetter) ->
                val name = firstLetter.toString() + map.getValue(coord.right())
                var position = coord.left()
                if (map.getValue(position) != '.') {
                    position = coord.right().right()
                }
                teleports[name] = teleports.getValue(name).apply { add(position) }
            }

        map.filter { it.value.isUpperCase() && map.getValue(it.key.down(offset = true)).isUpperCase() }
            .forEach { (coord, firstLetter) ->
                val name = firstLetter.toString() + map.getValue(coord.down(offset = true))
                var position = coord.up(offset = true)
                if (map.getValue(position) != '.') {
                    position = coord.down(offset = true).down(offset = true)
                }
                teleports[name] = teleports.getValue(name).apply { add(position) }
            }
    }
}

fun main() {
    println(Teleports().run(part2 = false))
}