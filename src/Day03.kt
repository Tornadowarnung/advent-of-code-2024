import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val line = input.joinToString("")
        val validEntries = Regex("mul\\(\\d{1,3},\\d{1,3}\\)").findAll(line).map { it.value }
        return validEntries.map { Pair(
            it.substring(it.indexOf("(") + 1, it.indexOf(",")),
            it.substring(it.indexOf(",") + 1, it.indexOf(")"))
        ) }
            .map { it.first.toInt() * it.second.toInt() }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)
//    check(part2(testInput) == 4)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
