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

    @Test
    fun day10() {
        runTaskTest(Asteroids(), 286, 504)
    }

    @Test
    fun day11() {
        val message = """
        # # # # . # # # . . # # # # . # # # . . # . . # . # # # # . # # # # . # # # . 
        . . . # . # . . # . . . . # . # . . # . # . # . . # . . . . . . . # . # . . # 
        . . # . . # . . # . . . # . . # . . # . # # . . . # # # . . . . # . . # . . # 
        . # . . . # # # . . . # . . . # # # . . # . # . . # . . . . . # . . . # # # . 
        # . . . . # . # . . # . . . . # . . . . # . # . . # . . . . # . . . . # . # . 
        # # # # . # . . # . # # # # . # . . . . # . . # . # # # # . # # # # . # . . #""".trimIndent()
        runTaskTest(PaintingRobot(), 2041, message)
    }

    @Test
    fun day12() {
        runTaskTest(Moons(), 7928, 518311327635164L)
    }

    @Test
    fun day13() {
        runTaskTest(ArcadeGame(), 236, 11040)
    }

    @Test
    fun day14() {
        runTaskTest(ChemicalReactions(), 843220L, 2169535L)
    }

    @Test
    fun day15() {
        runTaskTest(RepairDroid(), 254, 268)
    }

    @Test
    fun day17() {
        runTaskTest(Scaffolding(), 6680, 1103905)
    }

    @Test
    fun day18() {
        runTaskTest(Tunnels(), 4954, 2334)
    }

    @Test
    fun day19() {
        runTaskTest(TractorBeam(), 199, 10180726)
    }

    @Test
    fun day20() {
        runTaskTest(Teleports(), 618, 7152)
    }

    @Test
    fun day24() {
        runTaskTest(Bugs(), 3186366, 2031)
    }

    @Test
    fun day25() {
        runTaskTest(Adventure(), 8401920, 8401920)
    }

    private fun runTaskTest(task: AdventOfCodeTask, part1Result: Any, part2Result: Any) {
        assertEquals(part1Result, task.run())
        assertEquals(part2Result, task.run(part2 = true))
    }
}
