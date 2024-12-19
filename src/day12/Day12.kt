package day12

import readInput

data class Position(val row: Int, val column: Int)

data class Area(val character: Char, val positions: MutableSet<Position>) {
    fun getSize(): Int = positions.size

    fun calculatePerimeter(input: List<String>): Int {
        var perimeter = 0
        val minPosition = Position(
            positions.minOf { it.row },
            positions.minOf { it.column }
        )
        val maxPosition = Position(
            positions.maxOf { it.row },
            positions.maxOf { it.column }
        )
        input.forEachIndexed { rowNumber, line ->
            for (columnNumber in line.indices) {
                if (rowNumber < minPosition.row || columnNumber < minPosition.column ||
                    rowNumber > maxPosition.row || columnNumber > maxPosition.column ||
                    input[rowNumber][columnNumber] != character
                ) {
                    continue
                }

                perimeter += 4

                if (columnNumber - 1 > 0 && input[rowNumber][columnNumber - 1] == character) perimeter -= 1
                if (columnNumber + 1 < input[0].length && input[rowNumber][columnNumber + 1] == character) perimeter -= 1
                if (rowNumber - 1 > 0 && input[rowNumber - 1][columnNumber] == character) perimeter -= 1
                if (rowNumber + 1 < input.size && input[rowNumber + 1][columnNumber] == character) perimeter -= 1
            }
        }
        return perimeter
    }
}

fun main() {
    fun findAllAdjacent(
        input: List<String>,
        position: Position,
        character: Char,
        visited: MutableSet<Position>
    ): Set<Position> {
        if (visited.contains(position)) {
            return emptySet()
        }

        val result = mutableSetOf<Position>()
        if (position.column - 1 > 0 && input[position.row][position.column - 1] == character) {
            visited.add(position)
            result.add(position)
            result.addAll(findAllAdjacent(input, position.copy(column = position.column - 1), character, visited))
        }
        if (position.column + 1 < input[0].length && input[position.row][position.column + 1] == character) {
            visited.add(position)
            result.add(position)
            result.addAll(findAllAdjacent(input, position.copy(column = position.column + 1), character, visited))
        }
        if (position.row - 1 > 0 && input[position.row - 1][position.column] == character) {
            visited.add(position)
            result.add(position)
            result.addAll(findAllAdjacent(input, position.copy(row = position.row - 1), character, visited))
        }
        if (position.row + 1 < input.size && input[position.row + 1][position.column] == character) {
            visited.add(position)
            result.add(position)
            result.addAll(findAllAdjacent(input, position.copy(row = position.row + 1), character, visited))
        }
        return result
    }

    fun parseAreas(input: List<String>): List<Area> {
        val areas = mutableListOf<Area>()
        val visited = mutableSetOf<Position>()
        input.forEachIndexed { lineNumber, line ->
            for ((columnNumber, character) in line.withIndex()) {
                if (visited.contains(Position(lineNumber, columnNumber))) {
                    continue
                }

                val currentArea = Area(character, mutableSetOf(Position(lineNumber, columnNumber)))

                val allAdjacentPositionsWithChar =
                    findAllAdjacent(input, Position(lineNumber, columnNumber), character, visited)
                currentArea.positions.addAll(allAdjacentPositionsWithChar)
                visited.addAll(allAdjacentPositionsWithChar)

                areas.add(currentArea)
                visited.add(Position(lineNumber, columnNumber))
            }
        }
        return areas
    }

    fun part1(input: List<String>): Int {
        val areas = parseAreas(input)
        val result = areas.sumOf { area ->
            area.calculatePerimeter(input) * area.getSize()
        }
        return result
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 1930)
    check(part2(testInput) == 0)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}