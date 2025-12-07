package day07

import java.io.File

fun main() {
    val data = parse("src/main/kotlin/day07/Day07.txt")

    println("🎄 Day 07 🎄")

    println()

    println("[Part 1]")
    println("Answer: ${part1(data)}")

    println()

    println("[Part 2]")
    println("Answer: ${part2(data)}")
}

private data class Location(
    val x: Int,
    val y: Int,
)

private data class Manifold(
    val cells: List<String>,
    val start: Location,
)

private fun parse(path: String): Manifold {
    val cells = File(path).readLines()

    val startY = 0
    val startX = cells[startY]
        .indexOfFirst { it == 'S' }

    return Manifold(
        cells = cells,
        start = Location(startX, startY),
    )
}

private fun part1(data: Manifold): Int {
    val (grid, start) = data

    val visited = mutableSetOf<Location>()
    val queue = ArrayDeque<Location>()
    queue += start

    val splitters = mutableSetOf<Location>()
    while (queue.isNotEmpty()) {
        val beam = queue.removeFirst()
        visited += beam

        val splitter = grid.findSplitter(beam.x, beam.y)
        if (splitter != null) {
            splitters += splitter

            val leftBeam = Location(splitter.x - 1, splitter.y)
            if (leftBeam !in visited) {
                queue += leftBeam
            }

            val rightBeam = Location(splitter.x + 1, splitter.y)
            if (rightBeam !in visited) {
                queue += rightBeam
            }
        }
    }

    return splitters.size
}

private fun part2(data: Manifold): Long {
    val (grid, start) = data

    val cache = mutableMapOf<Location, Long>()

    val bottomY = grid.lastIndex - 1
    for ((bottomX, cell) in grid[bottomY].withIndex()) {
        if (cell == '^') {
            cache[Location(bottomX, bottomY)] = 2L
        }
    }

    for (y in bottomY - 1 downTo 0) {
        for ((x, cell) in grid[y].withIndex()) {
            if (cell != '.') {
                val lhs = grid.findSplitter(x - 1, y)?.let { cache.getValue(it) } ?: 1
                val rhs = grid.findSplitter(x + 1, y)?.let { cache.getValue(it) } ?: 1
                cache[Location(x, y)] = lhs + rhs
            }
        }
    }

    return cache.getValue(start)
}

private fun List<String>.findSplitter(x: Int, sy: Int): Location? {
    for (y in sy..lastIndex) {
        if (this[y][x] == '^') {
            return Location(x, y)
        }
    }
    return null
}
