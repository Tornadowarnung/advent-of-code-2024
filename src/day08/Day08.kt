package day08

import println
import readInput

data class Position(val x: Int, val y: Int) {
    operator fun plus(other: Position) = Position(x + other.x, y + other.y)
    operator fun minus(other: Position) = Position(x - other.x, y - other.y)
    fun isBetween(first: Position, second: Position) =
        first.x <= x && x <= second.x && first.y <= y && y <= second.y
}

operator fun Int.times(position: Position) = Position(this * position.x, this * position.y)

fun main() {

    data class Limits(val min: Position, val max: Position)

    data class Antenna(val character: Char, val position: Position)

    fun getLimits(input: List<String>): Limits {
        return Limits(
            min = Position(input.first().indices.first(), input.indices.first()),
            max = Position(input.last().indices.last(), input.indices.last())
        )
    }

    fun parseAntennaByCharacter(input: List<String>) = input.flatMapIndexed { lineNum, line ->
        line.mapIndexed { colNum, character ->
            Antenna(character, Position(colNum, lineNum))
        }
    }.filter {
        it.character != '.'
    }.groupBy { it.character }

    fun findAllSingularAntinodes(antennas: List<Antenna>, limits: Limits): Set<Position> {
        return antennas.flatMap { a ->
            antennas.flatMap { b ->
                if (a == b) return@flatMap emptySet()
                val aToB = b.position - a.position
                val bToA = a.position - b.position
                setOf(b.position - bToA, a.position - aToB)
            }.toSet()
        }.toSet().filter {
            it.isBetween(limits.min, limits.max)
        }.toSet()
    }

    fun findAllLineAntinodes(antennas: List<Antenna>, limits: Limits): Set<Position> {
        return antennas.flatMap { a ->
            antennas.flatMap { b ->
                if (a == b) return@flatMap emptySet()
                val returnSet = mutableSetOf<Position>()
                val bToA = b.position - a.position
                val aToB = a.position - b.position
                var currentPosition = b.position - aToB
                var currentIteration = 1
                while (currentPosition.isBetween(limits.min, limits.max)) {
                    returnSet.add(currentPosition)
                    currentIteration++
                    currentPosition = b.position - currentIteration * aToB
                }
                currentPosition = a.position - bToA
                currentIteration = 1
                while (currentPosition.isBetween(limits.min, limits.max)) {
                    returnSet.add(currentPosition)
                    currentIteration++
                    currentPosition = a.position - currentIteration * bToA
                }
                returnSet
            }.toSet()
        }.toSet().filter {
            it.isBetween(limits.min, limits.max)
        }.toSet()
    }

    fun part1(input: List<String>): Int {
        val limits = getLimits(input)
        val allAntennaByCharacter = parseAntennaByCharacter(input)

        val allAntinodes = allAntennaByCharacter.entries.map { it.value }.flatMap { allAntenna ->
            findAllSingularAntinodes(allAntenna, limits)
        }.toSet()

        return allAntinodes.size
    }

    fun part2(input: List<String>): Int {
        val limits = getLimits(input)
        val allAntennaByCharacter = parseAntennaByCharacter(input)

        val allAntinodes = allAntennaByCharacter.entries.map { it.value }.flatMap { allAntenna ->
            findAllLineAntinodes(allAntenna, limits)
        }.toSet()

        val checkList = input.toMutableList()
        allAntinodes.forEach {
            val line = StringBuilder(checkList[it.y])
            line.setCharAt(it.x, '#')
            checkList[it.y] = line.toString()
        }

        println(checkList.joinToString("\n"))
        println(allAntinodes.size)

        return allAntinodes.size
    }

    val testInput2 = readInput("Day08_test2")
    check(part2(testInput2) == 9)

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
