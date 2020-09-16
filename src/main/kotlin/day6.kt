import utils.readInputLines

/** [https://adventofcode.com/2019/day/6] */
class PlanetOrbits : AdventOfCodeTask {
    private data class Planet(val name: String) {
        var orbiting: Planet? = null
        val orbitedBy: MutableList<Planet> = mutableListOf()

        fun countOrbits(): Int {
            return if (orbiting != null) 1 + orbiting!!.countOrbits() else 0
        }

        fun getNeighbours(): List<Planet> {
            return if (orbiting != null) listOf(orbiting!!) + orbitedBy else orbitedBy
        }
    }

    override fun run(part2: Boolean): Any {
        val planets = mutableSetOf<Planet>()
        fun getOrCreatePlanet(name: String): Planet {
            val planet = planets.firstOrNull { it.name == name } ?: Planet(name)
            planets.add(planet)
            return planet
        }

        for (data in readInputLines("6.txt")) {
            val (innerPlanetName, outterPlanetName) = data.split(")")
            val innerPlanet = getOrCreatePlanet(innerPlanetName)
            val outerPlanet = getOrCreatePlanet(outterPlanetName)
            outerPlanet.orbiting = innerPlanet
            innerPlanet.orbitedBy += outerPlanet
        }

        if (!part2) {
            return planets.sumBy(Planet::countOrbits)
        }

        return findShortestPath(ArrayDeque(listOf(getOrCreatePlanet("YOU").orbiting!!)), Planet("SAN"))
    }

    private fun findShortestPath(
        path: ArrayDeque<Planet>?,
        target: Planet,
        visited: MutableSet<Planet> = mutableSetOf()
    ): Int {
        if (path == null || path.isEmpty() || visited.contains(path.first())) return -1

        val current = path.first()
        if (current.orbitedBy.contains(target)) {
            return path.size - 1
        }

        visited.add(current)
        for (neighbour in current.getNeighbours()) {
            val pathLength = findShortestPath(ArrayDeque(listOf(neighbour) + path), target, visited)
            if (pathLength != -1) {
                return pathLength
            }
        }

        return -1
    }
}

fun main() {
    println(PlanetOrbits().run(part2 = true))
}