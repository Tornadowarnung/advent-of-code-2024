package day07

import println
import readInput

sealed class ArithmeticOperation {
    abstract val value: Long

    data class Add(override val value: Long) : ArithmeticOperation()
    data class Multiply(override val value: Long) : ArithmeticOperation()
    data class Concat(override val value: Long) : ArithmeticOperation()
}

fun main() {

    data class Calculation(val result: Long, val values: List<Long>)

    fun generateCombination(
        numbers: List<Long>,
        operations: List<(Long) -> ArithmeticOperation>
    ): List<List<ArithmeticOperation>> {
        if (numbers.size < 2) {
            return operations.map { operation -> numbers.map { number -> operation(number) } }
        }

        val first = numbers.first()
        val rest = numbers.drop(1)

        val subResults = generateCombination(rest, operations)

        val result = mutableListOf<List<ArithmeticOperation>>()

        for (subResult in subResults) {
            for (operation in operations) {
                result.add(listOf(operation(first)) + subResult)
            }
        }

        return result
    }

    fun parseOperations(input: List<String>) = input.map { string ->
        val result = string.substringBefore(":").toLong()
        val values = string.substringAfter(":").split(" ").filter { it != "" }.map { it.toLong() }
        Calculation(result, values)
    }

    fun getSumOfValidCalculationResults(
        calculations: List<Calculation>,
        operations: List<(Long) -> ArithmeticOperation>
    ) = calculations.filter { calculation ->
        generateCombination(
            calculation.values,
            operations
        ).any { operations ->
            operations.reduce { acc, arithmeticOperation ->
                when (arithmeticOperation) {
                    is ArithmeticOperation.Add -> ArithmeticOperation.Add(acc.value + arithmeticOperation.value)
                    is ArithmeticOperation.Multiply -> ArithmeticOperation.Add(acc.value * arithmeticOperation.value)
                    is ArithmeticOperation.Concat -> ArithmeticOperation.Add((acc.value.toString() + arithmeticOperation.value.toString()).toLong())
                }
            }.value == calculation.result
        }
    }.sumOf { it.result }

    fun part1(input: List<String>): Long {
        val calculations = parseOperations(input)

        val operations = listOf<(Long) -> ArithmeticOperation>(
            { long -> ArithmeticOperation.Add(long) },
            { long -> ArithmeticOperation.Multiply(long) })

        return getSumOfValidCalculationResults(calculations, operations)
    }

    fun part2(input: List<String>): Long {
        val calculations = parseOperations(input)

        val operations = listOf<(Long) -> ArithmeticOperation>(
            { long -> ArithmeticOperation.Add(long) },
            { long -> ArithmeticOperation.Multiply(long) },
            { long -> ArithmeticOperation.Concat(long) })

        return getSumOfValidCalculationResults(calculations, operations)
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
