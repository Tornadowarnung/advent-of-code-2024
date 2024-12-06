data class Position4(val row: Int, val col: Int)

enum class Direction4(val direction: Position4) {
    UP(Position4(0, -1)),
    DOWN(Position4(0, 1)),
    LEFT(Position4(-1, 0)),
    RIGHT(Position4(1, 0)),
    UP_LEFT(Position4(-1, -1)),
    UP_RIGHT(Position4(1, -1)),
    DOWN_LEFT(Position4(-1, 1)),
    DOWN_RIGHT(Position4(1, 1));

    fun flip(): Direction4 = entries.first { it.direction == Position4(-direction.row, -direction.col) }
}

val diagonalDirections = listOf(Direction4.UP_LEFT, Direction4.UP_RIGHT, Direction4.DOWN_LEFT, Direction4.DOWN_RIGHT)

fun main() {
    val xmas = "XMAS"

    fun checkInDirection(input: List<String>, searchString: String, from: Position4, direction: Direction4): Boolean {
        searchString.forEachIndexed { index, character ->
            val row = from.row + index * direction.direction.row
            val column = from.col + index * direction.direction.col
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
                for (direction in Direction4.entries) {
                    if (checkInDirection(input, xmas, Position4(row, column), direction)) {
                        count++
                    }
                }
            }
        }
        return count
    }

    fun getSurroundingDirectionsFor(input: List<String>, position: Position4, searchChar: Char): Set<Direction4> {
        val result = mutableSetOf<Direction4>()
        diagonalDirections.forEach { direction ->
            val row = position.row + direction.direction.row
            val column = position.col + direction.direction.col
            val isXInvalid = row < 0 || row >= input.size
            if (isXInvalid) {
                return@forEach
            }
            val isYInvalid = column < 0 || column >= input[row].length
            if (isYInvalid) {
                return@forEach
            }
            if (input[row][column] == searchChar) {
                result.add(direction)
            }
        }
        return result
    }

    fun part2(input: List<String>): Int {
        var count = 0
        for (row in input.indices) {
            for (column in input[row].indices) {
                if (input[row][column] != 'A') {
                    continue
                }
                val mDirections = getSurroundingDirectionsFor(input, Position4(row, column), 'M')
                if (mDirections.size != 2 || mDirections.first().flip() == mDirections.last()) {
                    continue
                }
                if (getSurroundingDirectionsFor(input, Position4(row, column), 'S') ==
                    mDirections.map { it.flip() }.toSet()
                ) {
                    count++
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
