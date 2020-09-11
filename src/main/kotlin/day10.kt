import java.util.Collections.sort
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

/** [https://adventofcode.com/2019/day/10] */
class Asteroids : AdventOfCodeTask {
    private data class Asteroid(val x: Int, val y: Int) {
        fun distanceTo(other: Asteroid): Int {
            return abs(x - other.x) + abs(y - other.y)
        }
    }

    override fun run(part2: Boolean): Any {
        val asteroids = mutableListOf<Asteroid>()
        readInputLines("10.txt").forEachIndexed { y, row ->
            row.forEachIndexed { x, field ->
                if (field == '#') asteroids.add(
                    Asteroid(x, y)
                )
            }
        }

        val station =
            asteroids.map { neighboursByLineOfSight(it, asteroids) }.maxByOrNull { it.values.size }!!

        if (!part2) {
            return station.values.size
        }

        var neighboursIterator = station.iterator()
        var vaporizedCount = 0
        while (vaporizedCount < asteroids.size - 1) {
            if (!neighboursIterator.hasNext()) {
                neighboursIterator = station.iterator()
            }
            val line = neighboursIterator.next().value as MutableList<Asteroid>
            if (line.isEmpty()) {
                continue
            }
            val vaporized = line.removeFirst()
            if (++vaporizedCount == 200) {
                return vaporized.x * 100 + vaporized.y
            }
        }

        throw IllegalStateException()
    }

    private fun neighboursByLineOfSight(source: Asteroid, asteroids: List<Asteroid>): Map<Double, List<Asteroid>> {
        return asteroids.filter { it != source }
            .groupBy { other ->
                (atan2(
                    (other.y - source.y).toDouble(),
                    (other.x - source.x).toDouble()
                ) * 180 / PI + 90).let { if (it < 0) it + 360 else it }
            }.toSortedMap()
            .also { it.forEach { (_, list) -> sort(list, compareBy { other -> source.distanceTo(other) }) } }
    }
}

fun main() {
    println(Asteroids().run(part2 = true))
}