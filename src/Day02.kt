import kotlin.math.absoluteValue

fun main() {
    fun checkSingleReport(report: List<Int>): Boolean {
        val differences = mutableListOf<Int>()
        report.forEachIndexed { index, value ->
            if (index == 0) {
                return@forEachIndexed
            }
            differences.add(report[index - 1] - value)
        }
        val isSameIncrease = differences.all { it < 0 } || differences.all { it > 0 }
        val isNotTooHighOrTooLow = differences.map { it.absoluteValue }.none { it > 3 || it < 1 }
        return isSameIncrease && isNotTooHighOrTooLow
    }

    fun part1(input: List<String>): Int {
        return input.count { reportString ->
            val report = reportString.split(" ").map { it.toInt() }
            checkSingleReport(report)
        }
    }

    fun part2(input: List<String>): Int {
        return input.count { reportString ->
            val report = reportString.split(" ").map { it.toInt() }
            val variants = mutableListOf(report)
            for(i in report.indices) {
                variants.add(report.subList(0, i) + report.subList(i + 1, report.size))
            }
            variants.any { checkSingleReport(it) }
        }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
