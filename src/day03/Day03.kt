package day03

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val line = input.joinToString("")
        val validEntries = Regex("mul\\(\\d{1,3},\\d{1,3}\\)").findAll(line).map { it.value }
        return validEntries.map {
            Pair(
                it.substring(it.indexOf("(") + 1, it.indexOf(",")),
                it.substring(it.indexOf(",") + 1, it.indexOf(")"))
            )
        }
            .sumOf { it.first.toInt() * it.second.toInt() }
    }

    fun part2(input: List<String>): Int {
        val line = input.joinToString("")
        val validEntries = Regex("(mul\\(\\d{1,3},\\d{1,3}\\)|do\\(\\)|don't\\(\\))").findAll(line).map { it.value }
        var addOperation = true
        val sanitizedValidEntries = mutableListOf<String>()
        validEntries.forEach { entry ->
            if (entry == "don't()") {
                addOperation = false
            } else if (entry == "do()") {
                addOperation = true
            }
            if (addOperation && entry.startsWith("mul")) {
                sanitizedValidEntries.add(entry)
            }
        }
        return sanitizedValidEntries.map {
            Pair(
                it.substring(it.indexOf("(") + 1, it.indexOf(",")),
                it.substring(it.indexOf(",") + 1, it.indexOf(")"))
            )
        }.sumOf { it.first.toInt() * it.second.toInt() }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)
    check(part2(testInput) == 48)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
