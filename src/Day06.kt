data class Position6(val x: Int, val y: Int) {
    operator fun plus(other: Position6) = Position6(x + other.x, y + other.y)
    operator fun plus(other: GuardDirection) = this + other.position
}

enum class GuardDirection(val position: Position6) {
    UP(Position6(0, -1)),
    DOWN(Position6(0, 1)),
    LEFT(Position6(-1, 0)),
    RIGHT(Position6(1, 0));
}

fun main() {
    data class Guard(var position: Position6, private var direction: GuardDirection) {
        fun getDirection() = direction
        fun getNextPosition() = position + direction
        fun turnRight() {
            direction = when (direction) {
                GuardDirection.UP -> GuardDirection.RIGHT
                GuardDirection.DOWN -> GuardDirection.LEFT
                GuardDirection.LEFT -> GuardDirection.UP
                GuardDirection.RIGHT -> GuardDirection.DOWN
            }
        }

        fun walkForward() {
            position = getNextPosition()
        }
    }

    data class Obstacle(val position: Position6)

    data class ParsedMap(val guard: Guard, val obstacles: Set<Obstacle>, val maxPosition: Position6)

    data class VisitedPoint(val position: Position6, val direction: GuardDirection)

    fun parseMap(input: List<String>): ParsedMap {
        var guard: Guard? = null
        var maxPosition: Position6? = null
        val obstacles: MutableSet<Obstacle> = mutableSetOf()

        input.forEachIndexed { y, string ->
            string.forEachIndexed { x, character ->
                if (character == '#') {
                    obstacles.add(Obstacle(Position6(x, y)))
                }
                if (character == '^') {
                    guard = Guard(Position6(x, y), GuardDirection.UP)
                }
                maxPosition = Position6(x, y)
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
    ): MutableSet<VisitedPoint> {
        val visited = mutableSetOf<VisitedPoint>()
        while (guard.position.x < map.maxPosition.x && guard.position.y < map.maxPosition.y) {
            if (map.obstacles.any { obstacle -> obstacle.position == guard.getNextPosition() }) {
                guard.turnRight()
            } else {
                visited.add(VisitedPoint(guard.position, guard.getDirection()))
                guard.walkForward()
            }
        }
        visited.add(VisitedPoint(guard.position, guard.getDirection()))
        return visited
    }

    fun part1(input: List<String>): Int {
        val map = parseMap(input)

        // simulate movement
        val guard = map.guard
        val visited = simulateMovement(map, guard)
        return visited.map { it.position }.toSet().size
    }

    fun part2(input: List<String>): Int {
        val map = parseMap(input)



        return -1
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
//    check(part2(testInput) == 6)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
