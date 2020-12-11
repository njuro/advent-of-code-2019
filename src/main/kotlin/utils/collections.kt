package utils

fun <T> List<T>.permutations(allLengths: Boolean = false): Set<List<T>> {
    if (isEmpty()) {
        return setOf()
    }

    if (size == 1) {
        return setOf(this)
    }

    val result = mutableSetOf<List<T>>()
    for (element in this) {
        val copy = toMutableList().apply { remove(element) }
        for (perm in copy.permutations(allLengths)) {
            result += listOf(element) + perm
            if (allLengths) {
                result += perm
            }
        }
    }

    return result
}

fun <T> Set<T>.permutations(allLengths: Boolean = false): Set<Set<T>> {
    if (isEmpty()) {
        return setOf()
    }

    if (size == 1) {
        return setOf(this)
    }

    val result = mutableSetOf<Set<T>>()
    for (element in this) {
        val copy = toMutableSet().apply { remove(element) }
        for (perm in copy.permutations(allLengths)) {
            result += setOf(element) + perm
            if (allLengths) {
                result += perm
            }
        }
    }

    return result
}
