import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import utils.IntcodeComputer
import utils.permutations

/** [https://adventofcode.com/2019/day/7] */
class Amplifiers : AdventOfCodeTask {
    val computer = IntcodeComputer()

    override fun run(part2: Boolean): Any {
        val instructions = computer.parseInstructions("7.txt")
        val phaseList = (if (part2) 5..9 else 0..4).toList()
        var maxOutputSignal = Int.MIN_VALUE

        for ((a, b, c, d, e) in phaseList.permutations()) {
            val outputSignal =
                if (part2) computeTotalSignalAsync(instructions, a, b, c, d, e) else
                    computeTotalSignal(instructions, a, b, c, d, e)
            if (outputSignal > maxOutputSignal) {
                maxOutputSignal = outputSignal
            }
        }

        return maxOutputSignal
    }

    private fun computeTotalSignal(
        instructions: MutableList<Long>,
        aPhase: Int,
        bPhase: Int,
        cPhase: Int,
        dPhase: Int,
        ePhase: Int,
    ): Int {
        val aSignal = computeAmplifierSignal(instructions, aPhase, 0)
        val bSignal = computeAmplifierSignal(instructions, bPhase, aSignal)
        val cSignal = computeAmplifierSignal(instructions, cPhase, bSignal)
        val dSignal = computeAmplifierSignal(instructions, dPhase, cSignal)

        return computeAmplifierSignal(instructions, ePhase, dSignal)
    }

    private fun computeAmplifierSignal(instructions: MutableList<Long>, phaseSetting: Int, inputSignal: Int): Int {
        val inputs = mutableListOf(phaseSetting, inputSignal)
        return computer.compute(instructions.toMutableList(), onInput = { inputs.removeFirst() })
    }

    private fun computeTotalSignalAsync(
        instructions: MutableList<Long>,
        aPhase: Int,
        bPhase: Int,
        cPhase: Int,
        dPhase: Int,
        ePhase: Int
    ): Int {
        var totalSignal: Int
        runBlocking {

            val aChannel = Channel<Int>(UNLIMITED).also { it.send(aPhase); it.send(0) }
            val bChannel = Channel<Int>(UNLIMITED).also { it.send(bPhase) }
            val cChannel = Channel<Int>(UNLIMITED).also { it.send(cPhase) }
            val dChannel = Channel<Int>(UNLIMITED).also { it.send(dPhase) }
            val eChannel = Channel<Int>(UNLIMITED).also { it.send(ePhase) }


            computeAmplifierSignalAsync(instructions, aChannel, bChannel)
            computeAmplifierSignalAsync(instructions, bChannel, cChannel)
            computeAmplifierSignalAsync(instructions, cChannel, dChannel)
            computeAmplifierSignalAsync(instructions, dChannel, eChannel)
            computeAmplifierSignalAsync(instructions, eChannel, aChannel).join()

            totalSignal = aChannel.receive()
        }

        return totalSignal
    }

    private fun computeAmplifierSignalAsync(
        instructions: MutableList<Long>,
        inputChannel: Channel<Int>,
        outputChannel: Channel<Int>
    ): Job {
        return GlobalScope.launch {
            computer.compute(
                instructions.toMutableList(),
                onInput = { runBlocking { inputChannel.receive() } },
                onOutput = { runBlocking { outputChannel.send(it) } }
            )
        }
    }
}

fun main() {
    println(Amplifiers().run(part2 = true))
}

