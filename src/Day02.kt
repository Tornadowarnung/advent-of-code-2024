import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        return input.count { reportString ->
            val report = reportString.split(" ").map { it.toInt() }
            val differences = mutableListOf<Int>()
            report.forEachIndexed { index, value ->
                if (index == 0) {
                    return@forEachIndexed
                }
                differences.add(report[index - 1] - value)
            }
            val isSameIncrease = differences.all { it < 0 } || differences.all { it > 0 }
            val isNotTooHighOrTooLow = differences.map { it.absoluteValue }.none { it > 3 || it < 1 }
            isSameIncrease && isNotTooHighOrTooLow
        }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
//    check(part2(testInput) == 31)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
