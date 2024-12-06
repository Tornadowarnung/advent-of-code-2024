data class Position6(val x: Int, val y: Int) {
    operator fun plus(other: Position6) = Position6(x + other.x, y + other.y)
    operator fun plus(other: Direction6) = this + other.position
}

enum class Direction6(val position: Position6) {
    UP(Position6(0, -1)),
    DOWN(Position6(0, 1)),
    LEFT(Position6(-1, 0)),
    RIGHT(Position6(1, 0));
}

fun main() {
    data class Guard(var position: Position6, private var direction: Direction6) {
        fun getNextPosition() = position + direction
        fun turnRight() {
            direction = when (direction) {
                Direction6.UP -> Direction6.RIGHT
                Direction6.DOWN -> Direction6.LEFT
                Direction6.LEFT -> Direction6.UP
                Direction6.RIGHT -> Direction6.DOWN
            }
        }

        fun walkForward() {
            position = getNextPosition()
        }
    }

    data class Obstacle(val position: Position6)

    fun part1(input: List<String>): Int {
        // parse positions
        val obstacles = mutableListOf<Obstacle>()
        var eventualGuard: Guard? = null
        var eventualMaxPosition: Position6? = null

        input.forEachIndexed { y, string ->
            string.forEachIndexed { x, character ->
                if (character == '#') {
                    obstacles.add(Obstacle(Position6(x, y)))
                }
                if (character == '^') {
                    eventualGuard = Guard(Position6(x, y), Direction6.UP)
                }
                eventualMaxPosition = Position6(x, y)
            }
        }

        // simulate movement
        val visited = mutableSetOf<Position6>()
        val guard = eventualGuard!!
        val maxPosition = eventualMaxPosition!!
        while (guard.position.x < maxPosition.x && guard.position.y < maxPosition.y) {
            if (obstacles.any { obstacle -> obstacle.position == guard.getNextPosition() }) {
                guard.turnRight()
            } else {
                visited.add(guard.position)
                guard.walkForward()
            }
        }
        visited.add(guard.position)
        return visited.size
    }

    fun part2(input: List<String>): Int {
        return -1
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
//    check(part2(testInput) == 123)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
