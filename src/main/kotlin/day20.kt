import utils.Coordinate
import utils.maxX
import utils.maxY
import utils.readInputLines

/** [https://adventofcode.com/2019/day/20] */
class Teleports : AdventOfCodeTask {

    private val map = mutableMapOf<Coordinate, Char>().withDefault { ' ' }
    private var maxX = 0
    private var maxY = 0
    private val teleports = mutableMapOf<String, MutableSet<Coordinate>>().withDefault { mutableSetOf() }

    override fun run(part2: Boolean): Any {
        readInputLines("20.txt").forEachIndexed { y, row ->
            row.forEachIndexed { x, c -> map[Coordinate(x, y)] = c }
        }
        maxX = map.maxX()
        maxY = map.maxY()
        loadTeleports()
        return findShortestPath(multiLevel = part2)
    }

    private fun findShortestPath(multiLevel: Boolean): Int {
        val visited = mutableMapOf<Int, MutableSet<Coordinate>>().withDefault { mutableSetOf() }
        val queue = mutableListOf(Triple(teleports.getValue("AA").elementAt(0), 0, 0))
        while (true) {
            val (position, steps, level) = queue.removeFirst()
            if (visited.getValue(level).contains(position)) {
                continue
            }
            visited[level] = visited.getValue(level).apply { add(position) }

            if (map.getValue(position) != '.') {
                continue
            }

            if (position == teleports.getValue("ZZ").elementAt(0) && level == 0) {
                return steps
            }

            for (neighbour in position.adjacent(offset = true).values) {
                var newPosition = neighbour
                var newLevel = level
                if (map.getValue(neighbour).isUpperCase()) {
                    newPosition = teleports.values.filter { it.size > 1 }.firstOrNull { it.contains(position) }
                        ?.first { it != position } ?: neighbour
                    if (multiLevel) {
                        if (isOuterPosition(neighbour)) {
                            if (level == 0) {
                                continue
                            } else {
                                newLevel--
                            }
                        } else {
                            newLevel++
                        }
                    }
                }

                queue.add(Triple(newPosition, steps + 1, newLevel))
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

    private fun isOuterPosition(position: Coordinate): Boolean {
        return position.x <= 1 || position.x >= maxX - 1 || position.y <= 1 || position.y >= maxY - 1
    }
}

fun main() {
    println(Teleports().run(part2 = true))
}