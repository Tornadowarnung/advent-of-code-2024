package day11

import readInput

fun main() {
    fun blink(pebble: Long, blinkCacheByPebble: MutableMap<Long, List<Long>>): List<Long> {
        val pebbleString = pebble.toString()
        if (pebble == 0L) {
            return listOf(1L)
        } else if (pebbleString.length % 2 == 0) {
            if (blinkCacheByPebble.containsKey(pebble)) {
                return blinkCacheByPebble[pebble]!!
            }
            val left = pebbleString.substring(0, pebbleString.length / 2).toLong()
            val right = pebbleString.substring(pebbleString.length / 2).toLong()
            blinkCacheByPebble[pebble] = listOf(left, right)
            return blinkCacheByPebble[pebble]!!
        } else {
            return listOf(pebble * 2024)
        }
    }

    fun solve(pebbles: List<Long>, maxBlinks: Long): Long {
        val blinkCacheByStone = mutableMapOf<Long, List<Long>>()
        var countByStoneCache = mutableMapOf<Long, Long>()
        pebbles.forEach { pebble ->
            countByStoneCache[pebble] = countByStoneCache.getOrDefault(pebble, 0) + 1
        }

        for (i in 1..maxBlinks) {
            val updatedCountByStone = mutableMapOf<Long, Long>()
            countByStoneCache.forEach { (pebble, count) ->
                val updatedPebbles = blink(pebble, blinkCacheByStone)
                updatedPebbles.forEach { updatedPebble ->
                    updatedCountByStone[updatedPebble] = updatedCountByStone.getOrDefault(updatedPebble, 0) + count
                }
            }
            countByStoneCache = updatedCountByStone
        }

        var result = 0L
        countByStoneCache.values.forEach { result += it }
        return result
    }

    fun part1(input: List<String>): Long {
        val pebbles = input.first().split(" ").map { it.toLong() }
        val result = solve(pebbles, 25)
        return result
    }

    // Shamelessly copied the following solution: https://www.reddit.com/r/adventofcode/comments/1hbm0al/comment/m2q57x3/?utm_source=share&utm_medium=web3x&utm_name=web3xcss&utm_term=1&utm_content=share_button
    fun part2(input: List<String>): Long {
        val pebbles = input.first().split(" ").map { it.toLong() }
        val result = solve(pebbles, 75)
        return result
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 55312L)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}