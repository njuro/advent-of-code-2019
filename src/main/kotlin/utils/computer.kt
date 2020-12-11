package utils

class IntcodeComputer {

    fun parseInstructions(filename: String): MutableList<Long> {
        return readInputBlock(filename).trim().split(",").map { it.toLong() }.toMutableList()
    }

    fun compute(
        instructions: MutableList<Long>,
        onInput: () -> Int = { throw UnsupportedOperationException() },
        onOutput: (Int) -> Unit = {}
    ): Int {
        return computeWithLongSupport(
            instructions,
            onInput = { onInput().toLong() },
            onOutput = { value: Long -> onOutput(value.toInt()) }).toInt()
    }

    fun computeWithLongSupport(
        instructions: MutableList<Long>,
        onInput: () -> Long = { throw UnsupportedOperationException() },
        onOutput: (Long) -> Unit = {}
    ): Long {
        instructions.expandMemory(4096)

        var relativeBase = 0L
        var diagnosticCode = -1L
        var pointer = 0
        while (true) {
            val instruction = instructions[pointer]
            val opCode = (instruction % 100).toInt()
            val parameterModes = (instruction / 100).toString().reversed()
            fun parameterMode(position: Int) =
                if (position <= parameterModes.length) Character.getNumericValue(parameterModes[position - 1]) else 0

            fun readFrom(parameterPosition: Int): Long {
                val mode = parameterMode(parameterPosition)
                val parameter = instructions[pointer + parameterPosition]
                return when (mode) {
                    0 -> instructions[parameter.toInt()]
                    1 -> parameter
                    2 -> instructions[(parameter + relativeBase).toInt()]
                    else -> throw IllegalStateException("Unknown parameter mode $mode")
                }
            }

            fun writeTo(parameterPosition: Int, value: Long) {
                val mode = parameterMode(parameterPosition)
                val parameter = instructions[pointer + parameterPosition]
                val address = when (parameterMode(parameterPosition)) {
                    0, 1 -> parameter.toInt()
                    2 -> (parameter + relativeBase).toInt()
                    else -> throw IllegalStateException("Unknown parameter mode $mode")
                }

                instructions[address] = value
            }

            when (opCode) {
                1 -> {
                    writeTo(3, readFrom(1) + readFrom(2))
                    pointer += 4
                }
                2 -> {
                    writeTo(3, readFrom(1) * readFrom(2))
                    pointer += 4
                }
                3 -> {
                    writeTo(1, onInput())
                    pointer += 2
                }
                4 -> {
                    diagnosticCode = readFrom(1)
                    onOutput(diagnosticCode)
                    pointer += 2
                }
                5 -> {
                    pointer = if (readFrom(1) != 0L) readFrom(2).toInt() else pointer + 3
                }
                6 -> {
                    pointer = if (readFrom(1) == 0L) readFrom(2).toInt() else pointer + 3
                }
                7 -> {
                    writeTo(3, if (readFrom(1) < readFrom(2)) 1 else 0)
                    pointer += 4
                }
                8 -> {
                    writeTo(3, if (readFrom(1) == readFrom(2)) 1 else 0)
                    pointer += 4
                }
                9 -> {
                    relativeBase += readFrom(1)
                    pointer += 2
                }
                99 -> break
                else -> throw IllegalStateException("Unknown OP code $opCode")
            }
        }

        return diagnosticCode
    }

    private fun MutableList<Long>.expandMemory(size: Int) {
        addAll(MutableList(size) { 0 })
    }
}
