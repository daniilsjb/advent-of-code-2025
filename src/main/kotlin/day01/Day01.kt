package day01

import java.io.File
import kotlin.math.absoluteValue

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

private fun parse(path: String): List<Int> =
    File(path)
        .readLines()
        .filter { it.isNotBlank() }
        .map { it.toRotation() }

private fun String.toRotation(): Int {
    val direction = if (this[0] == 'L') -1 else 1
    val distance = this.substring(1).toInt()
    return direction * distance
}

private fun part1(data: List<Int>): Int {
    var dial = 50
    var counter = 0
    for (rotation in data) {
        dial += rotation % 100

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

private fun part2(data: List<Int>): Int {
    var dial = 50
    var counter = 0
    for (rotation in data) {
        counter += rotation.absoluteValue / 100

        val prev = dial
        dial += rotation % 100

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
