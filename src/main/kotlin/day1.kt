import kotlin.math.floor

fun calculateFuel(mass: Int, part2: Boolean): Int {
    val fuel = (floor(mass / 3.0) - 2).toInt()
    if (!part2) return fuel

    return if (fuel > 0) fuel + calculateFuel(fuel, part2) else 0
}

fun main() {
    println(readInputLines("1").map { it.toInt() }.map { calculateFuel(it, part2 = true) }.sum())
}