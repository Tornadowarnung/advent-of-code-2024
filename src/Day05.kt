fun main() {

    fun part1(input: List<String>): Int {
        val rulesString = input.subList(0, input.indexOf(""))

        val necessaryPredecessorsByPage = rulesString.flatMap { rules ->
            return@flatMap rules.split(",").map { rule ->
                val ruleParts = rule.split("|")
                ruleParts[0].toInt() to ruleParts[1].toInt()
            }
        }.groupBy({ it.second }, { it.first })

        val printsString = input.subList(input.indexOf("") + 1, input.size)
        val prints = printsString.map { prints -> prints.split(",").map { it.toInt() } }
        return prints.filter { print ->
            print.all { page ->
                if (!necessaryPredecessorsByPage.containsKey(page)) {
                    return@all true
                }
                necessaryPredecessorsByPage[page]!!.all { necessaryPredecessor ->
                    print.indexOf(necessaryPredecessor) <
                            print.indexOf(page)
                }
            }
        }.sumOf { validPrint -> validPrint[validPrint.size / 2] }
    }

    fun part2(input: List<String>): Int {
        return -1
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
//    check(part2(testInput) == 9)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
