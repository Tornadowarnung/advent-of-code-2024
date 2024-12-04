enum class Direction(val direction: Pair<Int, Int>) {
    UP(Pair(0, -1)),
    DOWN(Pair(0, 1)),
    LEFT(Pair(-1, 0)),
    RIGHT(Pair(1, 0)),
    UP_LEFT(Pair(-1, -1)),
    UP_RIGHT(Pair(1, -1)),
    DOWN_LEFT(Pair(-1, 1)),
    DOWN_RIGHT(Pair(1, 1)),
}

fun main() {
    val xmas = "XMAS"
    val mas = "MAS"

    fun checkInDirection(input: List<String>, searchString: String, from: Pair<Int, Int>, direction: Direction): Boolean {
        searchString.forEachIndexed { index, character ->
            val row = from.first + index * direction.direction.first
            val column = from.second + index * direction.direction.second
            val isXInvalid = row < 0 || row >= input.size
            if (isXInvalid) {
                return false
            }
            val isYInvalid = column < 0 || column >= input[row].length
            if (isYInvalid) {
                return false
            }
            if (input[row][column] != character) {
                return false
            }
        }
        return true
    }

    fun part1(input: List<String>): Int {
        var count = 0
        for (row in input.indices) {
            for (column in input[row].indices) {
                for (direction in Direction.entries) {
                    if (checkInDirection(input, xmas, Pair(row, column), direction)) {
                        count++
                    }
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        // TODO: Oh, Aufgabenstellung falsch verstanden :D
        var count = 0
        for (row in input.indices) {
            for (column in input[row].indices) {
                for (direction in listOf(Direction.UP_LEFT, Direction.DOWN_LEFT, Direction.UP_RIGHT, Direction.DOWN_RIGHT)) {
                    if (checkInDirection(input, mas, Pair(row, column), direction)) {
                        count++
                    }
                }
            }
        }
        return count
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
