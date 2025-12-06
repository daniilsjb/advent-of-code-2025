package day06

import java.io.File

fun main() {
    val data = parse("src/main/kotlin/day06/Day06.txt")

    println("🎄 Day 06 🎄")

    println()

    println("[Part 1]")
    println("Answer: ${part1(data)}")

    println()

    println("[Part 2]")
    println("Answer: ${part2(data)}")
}

typealias Operator = (Long, Long) -> Long

private data class Problem(
    val numbers: List<String>,
    val operator: Operator,
)

private fun parse(path: String): List<Problem> {
    val text = File(path).readText()
    val rows = text
        .lines()
        .filter { it.isNotBlank() }

    val separators = mutableListOf<Int>()
    for (index in text.indices) {
        val isEmptyColumn = rows.all { it.getOrNull(index) == ' ' }
        if (isEmptyColumn) {
            separators.add(index)
        }
    }

    val columns = MutableList(separators.size + 1) { mutableListOf<String>() }
    for (row in rows) {
        val ranges = listOf(-1) + separators + listOf(row.length)
        for ((index, range) in ranges.zipWithNext().withIndex()) {
            val (start, end) = range
            columns[index] += row.substring(start + 1, end)
        }
    }

    return columns.map { column ->
        val numbers = column.dropLast(1)

        val operator: Operator = when (val opcode = column.last().trim()) {
            "+" -> Long::plus
            "*" -> Long::times
            else -> error("Unknown operator '$opcode'")
        }

        Problem(numbers, operator)
    }
}

private fun part1(data: List<Problem>): Long =
    data.sumOf { problem ->
        problem.numbers
            .map { it.trim().toLong() }
            .reduce(problem.operator)
    }

private fun part2(data: List<Problem>): Long =
    data.sumOf { problem ->
        val columnIndices = problem.numbers[0].indices
        val columnNumbers = columnIndices.map { columnIndex ->
            problem.numbers
                .map { it.getOrNull(columnIndex) ?: ' ' }
                .joinToString("")
                .trim()
                .toLong()
        }

        columnNumbers.reduce(problem.operator)
    }
