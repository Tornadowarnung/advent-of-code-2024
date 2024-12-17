package day11

import readInput

fun main() {
    fun blink(pebbles: List<Long>): List<Long> {
        val result = mutableListOf<Long>()
        pebbles.forEach { pebble ->
            val pebbleString = pebble.toString()
            if (pebble == 0L) {
                result.add(1L)
            } else if (pebbleString.length % 2 == 0) {
                val firstHalf = pebbleString.substring(0, pebbleString.length / 2)
                val secondHalf = pebbleString.substring(pebbleString.length / 2)
                result.add(firstHalf.toLong())
                result.add(secondHalf.toLong())
            } else {
                result.add(pebble * 2024L)
            }
        }
        return result
    }

    fun part1(input: List<String>): Int {
        var pebbles = input.first().split(" ").map { it.toLong() }
        for (i in 1..25) {
            pebbles = blink(pebbles)
        }
        return pebbles.size
    }

    fun part2(input: List<String>): Int {
        return -1
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 55312)
//    check(part2(testInput) == 0)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}