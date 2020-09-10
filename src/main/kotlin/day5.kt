/** [https://adventofcode.com/2019/day/5] */
class ExtendedIntCode : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val ops = parseOps(filename = "5.txt")
        return compute(ops, onInput = { if (part2) 5 else 1 })
    }

    fun parseOps(filename: String): MutableList<Int> {
        return readInputBlock(filename).split(",").map { it.toInt() }.toMutableList()
    }

    fun compute(ops: MutableList<Int>, onInput: () -> Int, onOutput: ((Int) -> Unit)? = null): Int {
        var diagnosticCode = -1
        var i = 0
        while (true) {
            val instruction = ops[i]
            val opCode = instruction % 100
            val parameterModes = (instruction / 100).toString().reversed()

            fun getParameterValue(position: Int): Int {
                val parameter = ops[i + position]
                val mode =
                    if (position <= parameterModes.length) Character.getNumericValue(parameterModes[position - 1]) else 0
                return if (mode == 1) parameter else ops[parameter]
            }

            when (opCode) {
                1 -> {
                    ops[ops[i + 3]] = getParameterValue(1) + getParameterValue(2)
                    i += 4
                }
                2 -> {
                    ops[ops[i + 3]] = getParameterValue(1) * getParameterValue(2)
                    i += 4
                }
                3 -> {
                    ops[ops[i + 1]] = onInput()
                    i += 2
                }
                4 -> {
                    diagnosticCode = getParameterValue(1)
                    if (onOutput != null) {
                        onOutput(diagnosticCode)
                    }
                    i += 2
                }
                5 -> {
                    i = if (getParameterValue(1) != 0) getParameterValue(2) else i + 3
                }
                6 -> {
                    i = if (getParameterValue(1) == 0) getParameterValue(2) else i + 3
                }
                7 -> {
                    ops[ops[i + 3]] = if (getParameterValue(1) < getParameterValue(2)) 1 else 0
                    i += 4
                }
                8 -> {
                    ops[ops[i + 3]] = if (getParameterValue(1) == getParameterValue(2)) 1 else 0
                    i += 4
                }
                99 -> break
                else -> throw IllegalStateException("Unknown OP code $opCode")
            }
        }

        return diagnosticCode
    }
}

fun main() {
    println(ExtendedIntCode().run(part2 = true))
}