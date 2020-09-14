import kotlin.math.ceil

/** [https://adventofcode.com/2019/day/14] */
class ChemicalReactions : AdventOfCodeTask {
    private data class Ingredient(val name: String) {
        var quantity = 0L

        constructor(name: String, quantity: Long) : this(name) {
            this.quantity = quantity
        }
    }

    private data class Reaction(val ingredients: Set<Ingredient>, val result: Ingredient)

    override fun run(part2: Boolean): Any {
        val reactions = readInputLines("14.txt").map(::parseReaction).toSet()
        val orePerFuel = createIngredient(Ingredient("FUEL", 1), reactions)
        if (!part2) return orePerFuel

        val cargo = 1_000_000_000_000
        var lowerBound = (cargo / orePerFuel)
        var upperBound = 10 * lowerBound
        while (upperBound - lowerBound > 1) {
            val quantity = (upperBound + lowerBound) / 2
            val totalOres = createIngredient(Ingredient("FUEL", quantity), reactions)
            if (totalOres > cargo) {
                upperBound = quantity
            } else {
                lowerBound = quantity
            }
        }

        return lowerBound
    }

    private fun createIngredient(
        ingredient: Ingredient,
        reactions: Set<Reaction>,
        stock: MutableSet<Ingredient> = mutableSetOf()
    ): Long {
        if (ingredient.name == "ORE") {
            return ingredient.quantity
        }

        val reaction = reactions.first { it.result.name == ingredient.name }
        val stocked = stock.firstOrNull { it == ingredient } ?: Ingredient(ingredient.name)
        val multiplier = ceil((ingredient.quantity - stocked.quantity) / reaction.result.quantity.toDouble()).toInt()

        val remaining = reaction.result.quantity * multiplier - ingredient.quantity
        stocked.quantity += remaining
        stock.add(stocked)

        var ores = 0L
        for (subIngredient in reaction.ingredients) {
            ores += createIngredient(
                Ingredient(subIngredient.name, subIngredient.quantity * multiplier),
                reactions,
                stock
            )
        }

        return ores
    }

    private fun parseReaction(input: String): Reaction {
        val (ingredients, result) = input.split(" => ")
        return Reaction(ingredients.split(", ").map(::parseIngredient).toSet(), parseIngredient(result))
    }

    private fun parseIngredient(input: String): Ingredient {
        val (quantity, name) = input.split(" ")
        return Ingredient(name, quantity.toLong())
    }
}

fun main() {
    println(ChemicalReactions().run(part2 = true))
}
