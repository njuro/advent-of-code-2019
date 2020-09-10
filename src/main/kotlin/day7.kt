import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/** [https://adventofcode.com/2019/day/7] */
class Amplifiers : AdventOfCodeTask {
    val computer = ExtendedIntCode()

    override fun run(part2: Boolean): Any {
        val ops = computer.parseOps("7.txt")
        val phaseList = (if (part2) 5..9 else 0..4).toList()
        var maxOutputSignal = Int.MIN_VALUE

        for ((a, b, c, d, e) in permutations(phaseList)) {
            val outputSignal =
                if (part2) computeTotalSignalAsync(ops, a, b, c, d, e) else computeTotalSignal(ops, a, b, c, d, e)
            if (outputSignal > maxOutputSignal) {
                maxOutputSignal = outputSignal
            }
        }

        return maxOutputSignal
    }

    private fun <T> permutations(list: List<T>): Set<List<T>> {
        if (list.isEmpty()) {
            return setOf()
        }

        if (list.size == 1) {
            return setOf(list)
        }

        val result = mutableSetOf<List<T>>()
        for (element in list) {
            val copy = list.toMutableList().also { it.remove(element) }
            for (perms in permutations(copy)) {
                result += mutableListOf(element) + perms
            }
        }

        return result
    }

    private fun computeTotalSignal(
        ops: MutableList<Int>,
        aPhase: Int,
        bPhase: Int,
        cPhase: Int,
        dPhase: Int,
        ePhase: Int,
    ): Int {
        val aSignal = computeAmplifierSignal(ops, aPhase, 0)
        val bSignal = computeAmplifierSignal(ops, bPhase, aSignal)
        val cSignal = computeAmplifierSignal(ops, cPhase, bSignal)
        val dSignal = computeAmplifierSignal(ops, dPhase, cSignal)

        return computeAmplifierSignal(ops, ePhase, dSignal)
    }

    private fun computeAmplifierSignal(ops: MutableList<Int>, phaseSetting: Int, inputSignal: Int): Int {
        val inputs = mutableListOf(phaseSetting, inputSignal)
        return computer.compute(
            ops.toMutableList(),
            onInput = { inputs.removeFirst() })
    }

    private fun computeTotalSignalAsync(
        ops: MutableList<Int>,
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


            computeAmplifierSignalAsync(ops, aChannel, bChannel)
            computeAmplifierSignalAsync(ops, bChannel, cChannel)
            computeAmplifierSignalAsync(ops, cChannel, dChannel)
            computeAmplifierSignalAsync(ops, dChannel, eChannel)
            computeAmplifierSignalAsync(ops, eChannel, aChannel).join()

            totalSignal = aChannel.receive()
        }

        return totalSignal
    }

    private fun computeAmplifierSignalAsync(
        ops: MutableList<Int>,
        inputChannel: Channel<Int>,
        outputChannel: Channel<Int>
    ): Job {
        return GlobalScope.launch {
            computer.compute(
                ops.toMutableList(),
                onInput = { runBlocking { inputChannel.receive() } },
                onOutput = { runBlocking { outputChannel.send(it) } }
            )
        }
    }
}

fun main() {
    println(Amplifiers().run(part2 = true))
}

