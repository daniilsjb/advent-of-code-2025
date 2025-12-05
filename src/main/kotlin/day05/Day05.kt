package day05

import java.io.File

fun main() {
    val data = parse("src/main/kotlin/day05/Day05.txt")

    println("🎄 Day 05 🎄")

    println()

    println("[Part 1]")
    println("Answer: ${part1(data)}")

    println()

    println("[Part 2]")
    println("Answer: ${part2(data)}")
}

private data class Database(
    val ranges: List<LongRange>,
    val ingredients: List<Long>,
)

private fun parse(path: String): Database {
    val lines = File(path).readLines()

    val ranges = lines.takeWhile { it.isNotEmpty() }
        .map { it.split('-') }
        .map { (start, end) -> LongRange(start.toLong(), end.toLong()) }

    val ingredients = lines.takeLastWhile { it.isNotEmpty() }
        .map { it.toLong() }

    return Database(ranges, ingredients)
}

private fun part1(data: Database): Long {
    var counter = 0L
    outer@for (ingredient in data.ingredients) {
        for (range in data.ranges) {
            if (ingredient in range) {
                counter += 1
                continue@outer
            }
        }
    }
    return counter
}

private fun part2(data: Database): Long {
    val nonOverlappingRanges = mutableListOf<LongRange>()

    outer@for (range in data.ranges) {
        val covered = mutableSetOf<LongRange>()
        var clipped = range

        var done = false
        inner@while (!done) {
            for (other in nonOverlappingRanges) {
                if (clipped in other) {
                    // Discard the range entirely.
                    continue@outer
                }

                if (other in clipped) {
                    covered += other
                    continue
                }

                if (clipped.first in other) {
                    clipped = (other.last + 1)..clipped.last
                    continue@inner
                }

                if (clipped.last in other) {
                    clipped = clipped.first..(other.first - 1)
                    continue@inner
                }
            }

            done = true
        }

        nonOverlappingRanges.removeAll(covered)
        nonOverlappingRanges += clipped
    }

    return nonOverlappingRanges
        .sumOf { it.last - it.first + 1 }
}

operator fun LongRange.contains(other: LongRange): Boolean =
    other.first in this && other.last in this
