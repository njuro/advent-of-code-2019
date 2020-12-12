import utils.IntcodeComputer
import utils.permutations

/** [https://adventofcode.com/2019/day/25] */
class Adventure : AdventOfCodeTask {

    override fun run(part2: Boolean): Any {
        val computer = IntcodeComputer()
        val instructions = computer.parseInstructions("25.txt")

        val commands = listOf(
            "north",
            "west",
            "take planetoid",
            "west",
            "take spool of cat6",
            "east",
            "east",
            "south",
            "west",
            "north",
            "take dark matter",
            "south",
            "east",
            "east",
            "north",
            "take sand",
            "west",
            "take coin",
            "north",
            "take jam",
            "south",
            "west",
            "south",
            "take wreath",
            "west",
            "take fuel cell",
            "east",
            "north",
            "north",
            "west"
        )
        val inventory = setOf("planetoid", "spool of cat6", "dark matter", "sand", "coin", "jam", "wreath", "fuel cell")
        val output = StringBuilder()

        val queue = commands.compile()
        queue.addAll(dropItems(inventory))
        inventory.permutations(allLengths = true).forEach {
            queue.addAll(tryItems(it))
        }

        computer.compute(instructions, onInput = { queue.removeFirst() }, onOutput = { v -> output.append(v.toChar()) })

        val pattern = Regex(".*get in by typing (\\d+) on the keypad.*", RegexOption.DOT_MATCHES_ALL)
        val (password) = pattern.matchEntire(output.toString())!!.destructured
        return password.toInt()
    }

    private fun dropItems(items: Collection<String>): MutableList<Int> {
        return items.map { "drop $it" }.compile()
    }

    private fun tryItems(items: Collection<String>): MutableList<Int> {
        val command = items.map { "take $it" }.toMutableList()
        command.add("south")
        return (command.compile() + dropItems(items)).toMutableList()
    }

    private fun String.compile(): MutableList<Int> {
        return (this + "\n").map { it.toInt() }.toMutableList()
    }

    private fun Collection<String>.compile(): MutableList<Int> {
        return flatMap { it.compile() }.toMutableList()
    }
}

fun main() {
    println(Adventure().run(part2 = false))
}
