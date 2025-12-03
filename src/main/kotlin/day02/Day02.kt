package day02

import java.io.File

fun main() {
    val data = parse("src/main/kotlin/day02/Day02.txt")

    println("🎄 Day 02 🎄")

    println()

    println("[Part 1]")
    println("Answer: ${part1(data)}")

    println()

    println("[Part 2]")
    println("Answer: ${part2(data)}")
}

private fun parse(path: String): List<LongRange> =
    File(path)
        .readText()
        .trim()
        .split(",")
        .map { it.toRange() }

private fun String.toRange(): LongRange {
    val (from, to) = this.split("-")
    return from.toLong()..to.toLong()
}

private fun part1(data: List<LongRange>): Long {
    var sum = 0L
    for (range in data) {
        for (value in range) {
            val digits = value.toString()
            val lhs = digits.take(digits.length / 2)
            val rhs = digits.substring(digits.length / 2)
            if (lhs == rhs) {
                sum += value
            }
        }
    }
    return sum
}

private fun part2(data: List<LongRange>): Long {
    var sum = 0L
    for (range in data) {
        outer@for (value in range) {
            val digits = value.toString()
            for (n in 1..digits.length / 2) {
                val chunks = digits.chunked(n)
                if (chunks.distinct().size == 1) {
                    sum += value
                    continue@outer
                }
            }
        }
    }
    return sum
}
