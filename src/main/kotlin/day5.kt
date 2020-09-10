/** [https://adventofcode.com/2019/day/5] */
class ExtendedIntCode : AdventOfCodeTask {
    override fun run(part2: Boolean): Any {
        val ops = parseOps(filename = "5.txt")
        return compute(ops, onInput = { if (part2) 5 else 1 }).toInt()
    }

    fun parseOps(filename: String): MutableList<Long> {
        return readInputBlock(filename).split(",").map { it.toLong() }.toMutableList()
    }

    fun compute(ops: MutableList<Long>, onInput: () -> Long, onOutput: ((Long) -> Unit)? = null): Long {
        ops.addAll(MutableList(4096) { 0 })
        var relativeBase = 0L
        var diagnosticCode: Long = -1
        var i = 0
        while (true) {
            val instruction = ops[i]
            val opCode = (instruction % 100).toInt()
            val parameterModes = (instruction / 100).toString().reversed()
            fun parameterMode(position: Int) =
                if (position <= parameterModes.length) Character.getNumericValue(parameterModes[position - 1]) else 0

            fun parameterReadValue(position: Int): Long {
                val mode = parameterMode(position)
                val parameter = ops[i + position]
                return when (mode) {
                    0 -> ops[parameter.toInt()]
                    1 -> parameter
                    2 -> ops[(parameter + relativeBase).toInt()]
                    else -> throw IllegalStateException("Unknown parameter mode $mode")
                }
            }

            fun parameterWriteValue(position: Int): Int {
                val mode = parameterMode(position)
                val parameter = ops[i + position]
                return when (parameterMode(position)) {
                    0, 1 -> parameter.toInt()
                    2 -> (parameter + relativeBase).toInt()
                    else -> throw IllegalStateException("Unknown parameter mode $mode")
                }
            }

            when (opCode) {
                1 -> {
                    ops[parameterWriteValue(3)] = parameterReadValue(1) + parameterReadValue(2)
                    i += 4
                }
                2 -> {
                    ops[parameterWriteValue(3)] = parameterReadValue(1) * parameterReadValue(2)
                    i += 4
                }
                3 -> {
                    ops[parameterWriteValue(1)] = onInput()
                    i += 2
                }
                4 -> {
                    diagnosticCode = parameterReadValue(1)
                    if (onOutput != null) {
                        onOutput(diagnosticCode)
                    }
                    i += 2
                }
                5 -> {
                    i = if (parameterReadValue(1) != 0L) parameterReadValue(2).toInt() else i + 3
                }
                6 -> {
                    i = if (parameterReadValue(1) == 0L) parameterReadValue(2).toInt() else i + 3
                }
                7 -> {
                    ops[parameterWriteValue(3)] =
                        if (parameterReadValue(1) < parameterReadValue(2)) 1 else 0
                    i += 4
                }
                8 -> {
                    ops[parameterWriteValue(3)] =
                        if (parameterReadValue(1) == parameterReadValue(2)) 1 else 0
                    i += 4
                }
                9 -> {
                    relativeBase += parameterReadValue(1)
                    i += 2
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