package day12

import java.io.File

fun main() {
    val data = parse("src/main/kotlin/day12/Day12.txt")

    println("🎄 Day 12 🎄")

    println()

    println("[Part 1]")
    println("Answer: ${part1(data)}")
}

private data class Region(
    val w: Int,
    val h: Int,
    val quantities: List<Int>,
)

private fun parse(path: String): List<Region> =
    File(path).readLines()
        .filter { it.contains("x") }
        .map { it.toRegion() }

private fun String.toRegion(): Region {
    val (lhs, rhs) = this.split(": ")
    val (w, h) = lhs.split("x").map { it.toInt() }
    val quantities = rhs.split(" ").map { it.toInt() }
    return Region(w = w, h = h, quantities)
}

private fun part1(data: List<Region>): Long {
    var counter = 0L
    for ((w, h, quantities) in data) {
        if (w * h >= quantities.sum() * 9) {
            counter += 1
        }
    }
    return counter
}
