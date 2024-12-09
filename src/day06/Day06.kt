package day06

import println
import readInput

data class Position(val x: Int, val y: Int) {
    operator fun plus(other: Position) = Position(x + other.x, y + other.y)
    operator fun plus(other: Direction) = this + other.position
}

enum class Direction(val position: Position) {
    UP(Position(0, -1)),
    DOWN(Position(0, 1)),
    LEFT(Position(-1, 0)),
    RIGHT(Position(1, 0));
}

data class VisitedPoint(val position: Position, val direction: Direction)

data class Guard(var position: Position, private var direction: Direction) {
    fun getDirection() = direction
    fun getNextPosition() = position + direction
    fun turnRight() {
        direction = when (direction) {
            Direction.UP -> Direction.RIGHT
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
            Direction.RIGHT -> Direction.DOWN
        }
    }

    fun walkForward() {
        position = getNextPosition()
    }
}

data class Obstacle(val position: Position)

data class ParsedMap(val guard: Guard, val obstacles: Set<Obstacle>, val maxPosition: Position)

sealed class MovementResult {
    data class Result(val visitedPoints: MutableSet<VisitedPoint>) : MovementResult()
    data object Loop : MovementResult()
}

fun main() {
    fun parseMap(input: List<String>): ParsedMap {
        var guard: Guard? = null
        var maxPosition: Position? = null
        val obstacles: MutableSet<Obstacle> = mutableSetOf()

        input.forEachIndexed { y, string ->
            string.forEachIndexed { x, character ->
                if (character == '#') {
                    obstacles.add(Obstacle(Position(x, y)))
                }
                if (character == '^') {
                    guard = Guard(Position(x, y), Direction.UP)
                }
                maxPosition = Position(x, y)
            }
        }

        if (guard == null) {
            throw IllegalStateException("Could not find guard or maxPosition")
        }

        return ParsedMap(guard!!, obstacles, maxPosition!!)
    }

    fun simulateMovement(
        map: ParsedMap,
        guard: Guard,
    ): MovementResult {
        val visited = mutableSetOf<VisitedPoint>()
        while (guard.position.x < map.maxPosition.x && guard.position.y < map.maxPosition.y && guard.position.y >= 0 && guard.position.x >= 0) {
            if (map.obstacles.any { obstacle -> obstacle.position == guard.getNextPosition() }) {
                visited.add(VisitedPoint(guard.position, guard.getDirection()))
                guard.turnRight()
            } else {
                val currentlyVisitedPoint = VisitedPoint(guard.position, guard.getDirection())
                if (visited.contains(currentlyVisitedPoint)) {
                    return MovementResult.Loop
                }
                visited.add(currentlyVisitedPoint)
                guard.walkForward()
            }
        }
        visited.add(VisitedPoint(guard.position, guard.getDirection()))
        return MovementResult.Result(visited.toMutableSet())
    }

    fun part1(input: List<String>): Int {
        val map = parseMap(input)

        val guard = map.guard
        when (val movementResult = simulateMovement(map, guard)) {
            is MovementResult.Result -> return movementResult.visitedPoints.map { it.position }.toSet().size
            is MovementResult.Loop -> throw IllegalStateException("Encountered impossible loop in part 1")
        }
    }

    fun part2(input: List<String>): Int {
        val map = parseMap(input)

        val guard = map.guard
        val initialGuardPosition = guard.position
        val normalRoute = simulateMovement(map, guard.copy())
        check(normalRoute is MovementResult.Result, { "Encountered impossible loop in initial parsing of the map" })

        var foundLoops = 0
        normalRoute.visitedPoints.distinctBy { it.position }.forEachIndexed { index, visitedPoint ->
            if (visitedPoint.position == initialGuardPosition) return@forEachIndexed

            println("checking position ${index + 1} of ${normalRoute.visitedPoints.size} for loops")

            when (simulateMovement(map.copy(obstacles = map.obstacles.plus(Obstacle(visitedPoint.position))), guard.copy())) {
                is MovementResult.Result -> return@forEachIndexed
                is MovementResult.Loop -> foundLoops++
            }
        }

        return foundLoops
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
