package day01

import println
import readInput
import kotlin.math.absoluteValue

fun main() {
    fun splitIntoLeftAndRightLists(input: List<String>) = input.map { line ->
        val nums = line.split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        nums.first() to nums.last()
    }.unzip()

    fun part1(input: List<String>): Int {
        val (left, right) = splitIntoLeftAndRightLists(input)

        return left.sorted().zip(right.sorted()).sumOf { (it.first - it.second).absoluteValue }
    }

    fun part2(input: List<String>): Int {
        val (left, right) = splitIntoLeftAndRightLists(input)

        return left.sumOf { it.absoluteValue * right.count { r -> r == it } }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
