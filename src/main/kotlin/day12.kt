import kotlin.math.abs

/** [https://adventofcode.com/2019/day/12] */
class Moons : AdventOfCodeTask {
    private val regex = Regex("<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>")

    private data class Moon(var x: Int, var y: Int, var z: Int, var dx: Int = 0, var dy: Int = 0, var dz: Int = 0) {
        fun applyGravity(other: Moon) {
            fun delta(first: Int, second: Int) = when {
                first > second -> -1
                first < second -> 1
                else -> 0
            }
            dx += delta(x, other.x)
            dy += delta(y, other.y)
            dz += delta(z, other.z)
        }

        fun applyVelocity() {
            x += dx
            y += dy
            z += dz
        }

        fun energy(): Int {
            return (abs(x) + abs(y) + abs(z)) * (abs(dx) + abs(dy) + abs(dz))
        }
    }

    override fun run(part2: Boolean): Any {
        val moons = readInputLines("12.txt").map(regex::matchEntire).map { result ->
            val (x, y, z) = result!!.destructured
            Moon(x.toInt(), y.toInt(), z.toInt())
        }

        if (!part2) {
            repeat(1000) {
                performStep(moons)
            }

            return moons.sumBy(Moon::energy)
        }

        val originalX = moons.map { it.x to it.dx }
        val originalY = moons.map { it.y to it.dy }
        val originalZ = moons.map { it.z to it.dz }
        var stepsX: Long? = null
        var stepsY: Long? = null
        var stepsZ: Long? = null
        var steps: Long = 0
        do {
            steps++
            performStep(moons)
            if (stepsX == null && moons.map { it.x to it.dx } == originalX) {
                stepsX = steps
            }

            if (stepsY == null && moons.map { it.y to it.dy } == originalY) {
                stepsY = steps
            }

            if (stepsZ == null && moons.map { it.z to it.dz } == originalZ) {
                stepsZ = steps
            }
        } while (stepsX == null || stepsY == null || stepsZ == null)

        return lcm(lcm(stepsX, stepsY), stepsZ)
    }

    private fun lcm(a: Long, b: Long): Long {
        fun gcd(a: Long, b: Long): Long {

            if (a == 0L) return b

            return gcd(b % a, a)
        }

        return a * (b / gcd(a, b))
    }

    private fun performStep(moons: List<Moon>) {
        moons.forEach { moon -> moons.forEach { other -> moon.applyGravity(other) } }
        moons.forEach(Moon::applyVelocity)
    }
}

fun main() {
    println(Moons().run(part2 = true))
}