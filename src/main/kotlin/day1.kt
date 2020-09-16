import utils.readInputLines
import kotlin.math.floor

/** [https://adventofcode.com/2019/day/1] */
class FuelCalculator : AdventOfCodeTask {
    private var fuelNeedsFuel = false

    override fun run(part2: Boolean): Any {
        fuelNeedsFuel = part2
        return readInputLines("1.txt").map { it.toInt() }.map(this::calculateFuel).sum()
    }

    private fun calculateFuel(mass: Int): Int {
        val fuel = (floor(mass / 3.0) - 2).toInt()
        if (!fuelNeedsFuel) return fuel

        return if (fuel > 0) fuel + calculateFuel(fuel) else 0
    }
}

fun main() {
    println(FuelCalculator().run(part2 = true))
}