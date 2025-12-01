package day01

import java.io.File

fun main() {
    val data = parse("src/main/kotlin/day01/Day01.txt")

    println("🎄 Day 01 🎄")

    println()

    println("[Part 1]")
    println("Answer: ${part1(data)}")

    println()

    println("[Part 2]")
    println("Answer: ${part2(data)}")
}

private enum class Direction { Left, Right }

private data class Rotation(
    val direction: Direction,
    val distance: Int,
)

private fun parse(path: String): List<Rotation> =
    File(path)
        .readLines()
        .filter { it.isNotBlank() }
        .map { it.toRotation() }

private fun String.toRotation(): Rotation {
    val direction = when (this[0]) {
        'L' -> Direction.Left
        'R' -> Direction.Right
        else -> error("Invalid rotation: $this")
    }

    val distance = this.substring(1).toInt()
    return Rotation(direction, distance)
}

private fun part1(data: List<Rotation>): Int {
    var dial = 50
    var counter = 0
    for ((direction, distance) in data) {
        if (direction == Direction.Right) {
            dial += distance % 100
        } else {
            dial -= distance % 100
        }

        if (dial > 99) {
            dial -= 100
        } else if (dial < 0) {
            dial += 100
        }

        if (dial == 0) {
            counter += 1
        }
    }
    return counter
}

private fun part2(data: List<Rotation>): Int {
    var dial = 50
    var counter = 0
    for ((direction, distance) in data) {
        counter += distance / 100

        val prev = dial
        if (direction == Direction.Right) {
            dial += distance % 100
        } else {
            dial -= distance % 100
        }

        if (prev != 0 && dial <= 0) {
            counter += 1
        } else if (dial > 99) {
            counter += 1
        }

        if (dial > 99) {
            dial -= 100
        } else if (dial < 0) {
            dial += 100
        }
    }
    return counter
}
