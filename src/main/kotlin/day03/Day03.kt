package day03

import java.io.File

fun main() {
    val data = parse("src/main/kotlin/day03/Day03.txt")

    println("🎄 Day 03 🎄")

    println()

    println("[Part 1]")
    println("Answer: ${part1(data)}")

    println()

    println("[Part 2]")
    println("Answer: ${part2(data)}")
}

private fun parse(path: String): List<String> =
    File(path).readLines()

private fun solve(data: List<String>, n: Int): Long {
    return data.sumOf { bank ->
        var batteries = bank.map { it.digitToInt() }

        val turnedOn = mutableListOf<Int>()
        for (i in n downTo 1) {
            val index = batteries
                .dropLast(i - 1)
                .indices
                .maxBy { batteries[it] }

            turnedOn += batteries[index]
            batteries = batteries.drop(index + 1)
        }

        turnedOn.fold(0L) { acc, digit -> acc * 10 + digit }
    }
}

private fun part1(data: List<String>): Long =
    solve(data, 2)

private fun part2(data: List<String>): Long =
    solve(data, 12)
