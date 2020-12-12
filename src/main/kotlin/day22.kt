import utils.readInputLines

/** [https://adventofcode.com/2019/day/22] */
class Cards : AdventOfCodeTask {

    override fun run(part2: Boolean): Any {
        var deck = (0..10_006).toList()
        val instructions = readInputLines("22.txt").map {
            val operation = when {
                it.startsWith("deal into") -> 'R'
                it.startsWith("cut") -> 'C'
                it.startsWith("deal with") -> 'I'
                else -> throw IllegalArgumentException(it)
            }
            val amount = it.split(" ").last().toIntOrNull() ?: 0
            operation to amount
        }

        fun shuffle() {
            instructions.forEach { (operation, amount) ->
                deck = when (operation) {
                    'R' -> deck.asReversed()
                    'C' -> {
                        if (amount > 0) {
                            deck.subList(amount, deck.size) + deck.subList(0, amount)
                        } else {
                            deck.subList(deck.size + amount, deck.size) + deck.subList(0, deck.size + amount)
                        }
                    }
                    'I' -> {
                        val newDeck = deck.toMutableList()
                        var position = 0
                        for (i in deck.indices) {
                            newDeck[position] = deck[i]
                            position = (position + amount) % newDeck.size
                        }
                        newDeck
                    }
                    else -> throw IllegalArgumentException("Illegal operation $operation")
                }
            }
        }

        shuffle()
        return deck.indexOf(2019)
    }
}

fun main() {
    println(Cards().run(part2 = false))
}