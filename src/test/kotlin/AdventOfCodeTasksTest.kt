import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AdventOfCodeTasksTest {

    @Test
    fun day1() {
        runTaskTest(FuelCalculator(), 3246455, 4866824)
    }

    @Test
    fun day2() {
        runTaskTest(Intcode(), 7210630, 3892)
    }

    @Test
    fun day3() {
        runTaskTest(WireIntersection(), 375, 14746)
    }

    @Test
    fun day4() {
        runTaskTest(ContainerPassword(), 945, 617)
    }

    @Test
    fun day5() {
        runTaskTest(ExtendedIntCode(), 14155342, 8684145)
    }

    @Test
    fun day6() {
        runTaskTest(PlanetOrbits(), 308790, 472)
    }

    @Test
    fun day7() {
        runTaskTest(Amplifiers(), 118936, 57660948)
    }

    @Test
    fun day8() {
        val message = """
        ###  #  # #  # ###  #   #
        #  # # #  #  # #  # #   #
        #  # ##   #### #  #  # # 
        ###  # #  #  # ###    #  
        # #  # #  #  # # #    #  
        #  # #  # #  # #  #   #  """.trimIndent()
        runTaskTest(ImageFormat(), 2375, message)
    }

    @Test
    fun day9() {
        runTaskTest(BoostCode(), 2453265701L, 80805L)
    }

    private fun runTaskTest(task: AdventOfCodeTask, part1Result: Any, part2Result: Any) {
        assertEquals(part1Result, task.run())
        assertEquals(part2Result, task.run(part2 = true))
    }
}