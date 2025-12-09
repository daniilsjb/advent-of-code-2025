package day09

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val data = parse("src/main/kotlin/day09/Day09.txt")

    println("🎄 Day 09 🎄")

    println()

    println("[Part 1]")
    println("Answer: ${part1(data)}")

    println()

    println("[Part 2]")
    println("Answer: ${part2(data)}")
}

private data class Tile(
    val x: Long,
    val y: Long,
)

private fun parse(path: String): List<Tile> =
    File(path).readLines().map { it.toTile() }

private fun String.toTile(): Tile {
    val (x, y) = this.split(",")
    return Tile(x.toLong(), y.toLong())
}

private fun part1(data: List<Tile>): Long {
    var largest = 0L
    for ((i, tileA) in data.withIndex()) {
        for ((j, tileB) in data.withIndex()) {
            if (j > i) {
                val dx = abs(tileA.x - tileB.x) + 1
                val dy = abs(tileA.y - tileB.y) + 1
                val area = dx * dy
                if (area > largest) {
                    largest = area
                }
            }
        }
    }
    return largest
}

private typealias Edge = Pair<Tile, Tile>

private fun part2(data: List<Tile>): Long {
    val ys = data.map { it.y }
    val minY = ys.min()
    val maxY = ys.max()

    val table = mutableMapOf<Long, MutableList<Edge>>()
    for (edge in (data + data.first()).zipWithNext()) {
        val (t0, t1) = edge
        val y0 = min(t0.y, t1.y)
        val y1 = max(t0.y, t1.y)
        for (y in y0..y1) {
            table.getOrPut(y) { mutableListOf() } += edge
        }
    }

    val intervals = (minY..maxY).associateWith { y ->
        val pairs = table
            .getOrDefault(y, emptyList())
            .map { (a, b) -> (a.x to b.x).sorted() }
            .sortedBy { (t0, t1) -> t0 + t1 }

        val edges = pairs
            .flatMapIndexed { index, (x0, x1) ->
                buildList {
                    if (index != 0) add(x0)
                    if (index != pairs.lastIndex) add(x1)
                }
            }
            .zipWithNext()
            .filter { (x0, x1) -> x0 != x1 }

        buildList {
            var i = 0
            while (i < edges.size) {
                val (x0, x1) = edges[i]
                if (i + 1 < edges.size) {
                    val (x2, x3) = edges[i + 1]
                    if (x1 == x2) {
                        add(x0..x3)
                        i += 2
                        continue
                    }
                }

                add(x0..x1)
                i++
            }
        }
    }

    var largest = 0L
    for ((i, tileA) in data.withIndex()) {
        scan@for ((j, tileB) in data.withIndex()) {
            if (j > i) {
                val x0 = min(tileA.x, tileB.x)
                val x1 = max(tileA.x, tileB.x)
                val y0 = min(tileA.y, tileB.y)
                val y1 = max(tileA.y, tileB.y)

                for (y in y0..y1) {
                    val ranges = intervals.getOrDefault(y, listOf())
                    for (range in ranges) {
                        if (x0 !in range || x1 !in range) {
                            continue@scan
                        }
                    }
                }


                val dx = x1 - x0 + 1
                val dy = y1 - y0 + 1
                val area = dx * dy
                if (area > largest) {
                    largest = area
                }
            }
        }
    }

    return largest
}

private fun Pair<Long, Long>.sorted(): Pair<Long, Long> {
    val (a, b) = this
    return min(a, b) to max(a, b)
}
