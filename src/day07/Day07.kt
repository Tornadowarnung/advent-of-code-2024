package day07

import println
import readInput

sealed class ArithmeticOperation {
    abstract val value: Long
    data class Add(override val value: Long): ArithmeticOperation()
    data class Multiply(override val value: Long): ArithmeticOperation()
}

fun main() {

    data class Calculation(val result: Long, val values: List<Long>)

    fun generateCombination(numbers: List<Long>): List<List<ArithmeticOperation>> {
        if(numbers.size < 2) {
            return listOf(numbers.map { ArithmeticOperation.Add(it) }, numbers.map { ArithmeticOperation.Multiply(it) })
        }

        val first = numbers.first()
        val rest = numbers.drop(1)

        val subResults = generateCombination(rest)

        val result = mutableListOf<List<ArithmeticOperation>>()

        for(subResult in subResults) {
            result.add(listOf(ArithmeticOperation.Add(first)) + subResult)
            result.add(listOf(ArithmeticOperation.Multiply(first)) + subResult)
        }

        return result
    }

    fun part1(input: List<String>): Long {
        val calculations = input.map { string ->
            val result = string.substringBefore(":").toLong()
            val values = string.substringAfter(":").split(" ").filter { it != "" }.map { it.toLong() }
            Calculation(result, values)
        }

        val sumOfValidCalculationResults = calculations.filter { calculation ->
            generateCombination(calculation.values).any { operations ->
                operations.reduce { acc, arithmeticOperation ->
                    when (arithmeticOperation) {
                        is ArithmeticOperation.Add -> ArithmeticOperation.Add(acc.value + arithmeticOperation.value)
                        is ArithmeticOperation.Multiply -> ArithmeticOperation.Add(acc.value * arithmeticOperation.value)
                    }
                }.value == calculation.result
            }
        }.sumOf { it.result }

        return sumOfValidCalculationResults
    }

    fun part2(input: List<String>): Long {
        TODO()
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    // check(part2(testInput) == 4)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
