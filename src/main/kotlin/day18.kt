import utils.Coordinate
import utils.readInputLines
import utils.toStringRepresentation

class Tunnels : AdventOfCodeTask {

    private var shortestPath = Int.MAX_VALUE
    private lateinit var paths: Map<Char, Set<Path>>
    private val pathLengths = mutableMapOf<String, Int>().withDefault { Int.MAX_VALUE }

    private data class Path(val target: Char, val blockedBy: Set<Char>, var steps: Int)

    override fun run(part2: Boolean): Any {
        val map = mutableMapOf<Coordinate, Char>()
        readInputLines("18.txt").forEachIndexed { y, row -> row.forEachIndexed { x, c -> map[Coordinate(x, y)] = c } }
        paths = map.filterValues { it.isLowerCase() || it == '@' }.values.associateWith { discoverPaths(map, it) }
        checkPaths()
        println(map.toStringRepresentation(offsetCoordinates = true))

        findShortestPath()
        return shortestPath
    }

    private fun findShortestPath(
        position: Char = '@',
        path: MutableList<Pair<Char, Int>> = mutableListOf()
    ) {

        val keys = path.map { it.first }
        val pathLength = path.sumBy { it.second }

        if (path.size > 2) {
            val pathHash = keys.subList(0, keys.lastIndex).sorted().joinToString("") + keys.last()
            if (pathLengths.getValue(pathHash) <= pathLength) {
                return
            }

            pathLengths[pathHash] = pathLength
        }

        val availablePaths = findAvailablePaths(position, keys)

        if (path.size == paths.size - 1) {
            if (pathLength < shortestPath) {
                shortestPath = pathLength
            }
        }

        for ((key, steps) in availablePaths) {
            val newPath = path.toMutableList()
            newPath.add(key to steps)
            findShortestPath(key, newPath)
        }
    }

    private fun findAvailablePaths(
        source: Char,
        keys: List<Char>
    ): Map<Char, Int> {
        return paths.getValue(source)
            .filter { keys.containsAll(it.blockedBy) && !keys.contains(it.target) }
            .associateBy({ it.target }, { it.steps })
    }

    private fun discoverPaths(
        map: Map<Coordinate, Char>,
        source: Char
    ): Set<Path> {
        val found = mutableSetOf<Path>()
        val queue = mutableListOf<Triple<Coordinate, Int, MutableSet<Char>>>(
            Triple(
                map.entries.find { it.value == source }!!.key,
                0,
                mutableSetOf()
            )
        )
        val visited = mutableSetOf<Coordinate>()

        while (queue.isNotEmpty()) {
            val (position, steps, doors) = queue.removeFirst()
            val current = map.getValue(position)
            if (visited.contains(position)) {
                continue
            }
            visited.add(position)

            if (current == '#') {
                continue
            }

            if (current.isUpperCase()) {
                doors.add(current.toLowerCase())
            }

            if ((current.isLowerCase()) && current != source) {
                found.add(Path(current, doors.toMutableSet(), steps))
            }

            for (neighbour in position.adjacent(offset = true).values) {
                queue.add(Triple(neighbour, steps + 1, doors.toMutableSet()))
            }
        }


        return found
    }

    private fun checkPaths() {
        paths.keys.forEach { first ->
            paths.keys.forEach { second ->
                if (first != second && second != '@' && first != '@') {
                    val firstToSecond = paths.getValue(first).first { it.target == second }
                    val secondToFirst = paths.getValue(second).first { it.target == first }
                    if ((firstToSecond.steps != secondToFirst.steps)) {
                        println("Distance between $first and $second does not match (${firstToSecond.steps} vs ${secondToFirst.steps} steps)")
                    }
                }
            }
        }
    }
}

fun main() {
    println(Tunnels().run(part2 = false))
}