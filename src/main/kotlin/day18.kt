import utils.Coordinate
import utils.readInputLines

/** [https://adventofcode.com/2019/day/18] */
class Tunnels : AdventOfCodeTask {

    private lateinit var paths: Map<Char, Set<Path>>
    private val pathLengths = mutableMapOf<String, Int>().withDefault { Int.MAX_VALUE }
    private var shortestPath = Int.MAX_VALUE
    private var keyCount = -1

    private data class Path(val target: Char, val blockedBy: Set<Char>, var steps: Int)

    override fun run(part2: Boolean): Any {
        val map = loadMap(part2)
        paths = map.filterValues { it.isLowerCase() || it.isDigit() }.values.associateWith { discoverPaths(map, it) }
        keyCount = map.count { it.value.isLowerCase() }
        val robots = map.filterValues(Char::isDigit).values.toMutableSet()

        findShortestPath(robots)
        return shortestPath
    }

    private fun findShortestPath(
        positions: MutableSet<Char>,
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

        if (path.size == keyCount) {
            if (pathLength < shortestPath) {
                shortestPath = pathLength
            }
        }

        val availablePaths = findAvailablePaths(positions, keys)

        for ((source, paths) in availablePaths) {
            for ((key, steps) in paths) {
                val newPositions = positions.toMutableSet()
                newPositions.remove(source)
                newPositions.add(key)
                val newPath = path.toMutableList()
                newPath.add(key to steps)
                findShortestPath(newPositions, newPath)
            }
        }
    }

    private fun findAvailablePaths(
        sources: Set<Char>,
        keys: List<Char>
    ): Map<Char, Map<Char, Int>> {
        return sources.associateWith {
            paths.getValue(it).filter { path -> keys.containsAll(path.blockedBy) && !keys.contains(path.target) }
                .associateBy({ path -> path.target }, { path -> path.steps })
        }
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

    private fun loadMap(
        multipleEntrances: Boolean
    ): MutableMap<Coordinate, Char> {
        val map = mutableMapOf<Coordinate, Char>()
        readInputLines("18.txt").forEachIndexed { y, row -> row.forEachIndexed { x, c -> map[Coordinate(x, y)] = c } }

        val entrancePosition = map.entries.find { it.value == '@' }!!.key
        if (!multipleEntrances) {
            map[entrancePosition] = '1'
            return map
        }

        map[entrancePosition] = '#'
        entrancePosition.adjacent().values.forEach { map[it] = '#' }
        map[entrancePosition.up(offset = true).left()] = '1'
        map[entrancePosition.up(offset = true).right()] = '2'
        map[entrancePosition.down(offset = true).left()] = '3'
        map[entrancePosition.down(offset = true).right()] = '4'

        return map
    }
}

fun main() {
    println(Tunnels().run(part2 = true))
}