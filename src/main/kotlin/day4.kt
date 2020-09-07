fun isValid(number: Int): Boolean {
    val numberString = number.toString()
    val sortedNumberString = numberString.toCharArray().sorted().joinToString("")
    if (numberString != sortedNumberString) {
        return false;
    }

    val tokens = mutableListOf<List<Char>>()
    var token: Char = sortedNumberString[0]
    var group = mutableListOf<Char>()
    for (ch in numberString) {
        if (ch != token) {
            tokens.add(group)
            token = ch
            group = mutableListOf()
        }

        group.add(ch)
    }
    tokens.add(group)

    val part2 = true
    return tokens.any { if (part2) it.size == 2 else it.size >= 2 }
}

fun main() {
    val (min, max) = readInputBlock("4").split("-")
    println(IntRange(min.toInt(), max.toInt()).filter(::isValid).size)
}