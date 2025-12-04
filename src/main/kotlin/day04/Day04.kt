package day04

import java.io.File

fun main() {
    val data = parse("src/main/kotlin/day04/Day04.txt")

    println("🎄 Day 04 🎄")

    println()

    println("[Part 1]")
    println("Answer: ${part1(data)}")

    println()

    println("[Part 2]")
    println("Answer: ${part2(data)}")
}

private fun parse(path: String): List<String> =
    File(path).readLines()

private fun List<List<Char>>.accessible(): Set<Pair<Int, Int>> {
    val marked = mutableSetOf<Pair<Int, Int>>()

    for ((y, row) in this.withIndex()) {
        for ((x, col) in row.withIndex()) {
            if (col != '@') continue

            var adjacentRolls = 0
            for (dy in -1..1) {
                for (dx in -1..1) {
                    if (dx == 0 && dy == 0) continue

                    val ax = x + dx
                    val ay = y + dy

                    val adjacent = getOrNull(ay)?.getOrNull(ax)
                    if (adjacent == '@') {
                        adjacentRolls += 1
                    }
                }
            }

            if (adjacentRolls < 4) {
                marked += x to y
            }
        }
    }

    return marked
}

private fun part1(data: List<String>): Int {
    val grid = data.map { it.toList() }
    return grid.accessible().size
}

private fun part2(data: List<String>): Int {
    val grid = data.map { it.toMutableList() }

    var counter = 0
    var accessible = grid.accessible()
    while (accessible.isNotEmpty()) {
        counter += accessible.size

        for ((x, y) in accessible) {
            grid[y][x] = '.'
        }

        accessible = grid.accessible()
    }

    return counter
}
