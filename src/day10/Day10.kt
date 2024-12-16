package day10

import readInput

data class Position(val x: Int, val y: Int) {
    fun getNorth() = Position(x, y - 1)
    fun getSouth() = Position(x, y + 1)
    fun getEast() = Position(x + 1, y)
    fun getWest() = Position(x - 1, y)
}

fun main() {
    fun isValidPosition(map: List<String>, position: Position): Boolean =
        !(position.x < 0 || position.y < 0 || position.y >= map.size || position.x >= map[0].length)

    fun findNextStep(map: List<String>, position: Position): List<Position> {
        val result = mutableListOf<Position>()
        val height = map[position.y][position.x].digitToInt()
        val west = position.getWest()
        if (map[position.y][position.x] == '9') {
            result.add(position)
            return result
        }
        if (isValidPosition(map, west) && map[west.y][west.x] == (height + 1).digitToChar()) {
            result.addAll(findNextStep(map, west))
        }
        val north = position.getNorth()
        if (isValidPosition(map, north) && map[north.y][north.x] == (height + 1).digitToChar()) {
            result.addAll(findNextStep(map, north))
        }
        val south = position.getSouth()
        if (isValidPosition(map, south) && map[south.y][south.x] == (height + 1).digitToChar()) {
            result.addAll(findNextStep(map, south))
        }
        val east = position.getEast()
        if (isValidPosition(map, east) && map[east.y][east.x] == (height + 1).digitToChar()) {
            result.addAll(findNextStep(map, east))
        }
        return result
    }

    fun part1(input: List<String>): Int {
        val trailHeads = mutableListOf<Position>()
        input.forEachIndexed { lineIndex, line ->
            line.forEachIndexed { rowIndex, character ->
                if (character == '0') trailHeads.add(Position(rowIndex, lineIndex))
            }
        }

        val result = trailHeads.map { position ->
            findNextStep(input, position).toSet().size
        }

        return result.sum()
    }

    fun part2(input: List<String>): Int {
        return -1
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
//    check(part2(testInput) == 0)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}