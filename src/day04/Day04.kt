package day04

import println
import readInput

data class Position(val row: Int, val col: Int)

enum class Direction(val direction: Position) {
    UP(Position(0, -1)),
    DOWN(Position(0, 1)),
    LEFT(Position(-1, 0)),
    RIGHT(Position(1, 0)),
    UP_LEFT(Position(-1, -1)),
    UP_RIGHT(Position(1, -1)),
    DOWN_LEFT(Position(-1, 1)),
    DOWN_RIGHT(Position(1, 1));

    fun flip(): Direction = entries.first { it.direction == Position(-direction.row, -direction.col) }
}

val diagonalDirections = listOf(Direction.UP_LEFT, Direction.UP_RIGHT, Direction.DOWN_LEFT, Direction.DOWN_RIGHT)

fun main() {
    val xmas = "XMAS"

    fun checkInDirection(input: List<String>, searchString: String, from: Position, direction: Direction): Boolean {
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
                for (direction in Direction.entries) {
                    if (checkInDirection(input, xmas, Position(row, column), direction)) {
                        count++
                    }
                }
            }
        }
        return count
    }

    fun getSurroundingDirectionsFor(input: List<String>, position: Position, searchChar: Char): Set<Direction> {
        val result = mutableSetOf<Direction>()
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
                val mDirections = getSurroundingDirectionsFor(input, Position(row, column), 'M')
                if (mDirections.size != 2 || mDirections.first().flip() == mDirections.last()) {
                    continue
                }
                if (getSurroundingDirectionsFor(input, Position(row, column), 'S') ==
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
