import kotlin.math.floor

/** [https://adventofcode.com/2019/day/1] */
class FuelCalculator : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        return readInputLines("1.txt").map { it.toInt() }.map { calculateFuel(it, fuelNeedsFuel = part2) }.sum()
    }

    private fun calculateFuel(mass: Int, fuelNeedsFuel: Boolean): Int {
        val fuel = (floor(mass / 3.0) - 2).toInt()
        if (!fuelNeedsFuel) return fuel

        return if (fuel > 0) fuel + calculateFuel(fuel, fuelNeedsFuel) else 0
    }
}

fun main() {
    println(FuelCalculator().run(part2 = true))
}