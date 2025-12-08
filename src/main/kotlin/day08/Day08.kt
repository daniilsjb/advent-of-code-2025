package day08

import java.io.File
import java.util.TreeSet
import kotlin.math.sqrt

fun main() {
    val data = parse("src/main/kotlin/day08/Day08.txt")

    println("🎄 Day 08 🎄")

    println()

    println("[Part 1]")
    println("Answer: ${part1(data)}")

    println()

    println("[Part 2]")
    println("Answer: ${part2(data)}")
}

private data class Box(
    val x: Int,
    val y: Int,
    val z: Int,
)

private data class Boxes(
    val asElements: List<Box>,
    val asSortedPairs: List<Pair<Box, Box>>,
)

private fun parse(path: String): Boxes {
    val boxes = File(path)
        .readLines()
        .map { it.toBox() }

    val sorted = TreeSet<Pair<Box, Box>>(compareBy { (a, b) -> a.distanceTo(b) })
    for ((i, boxA) in boxes.withIndex()) {
        for ((j, boxB) in boxes.withIndex()) {
            if (j > i) {
                sorted.add(Pair(boxA, boxB))
            }
        }
    }

    return Boxes(
        asElements = boxes,
        asSortedPairs = sorted.toList()
    )
}

private fun String.toBox(): Box {
    val (x, y, z) = this.split(",")
    return Box(x.toInt(), y.toInt(), z.toInt())
}

private fun part1(boxes: Boxes): Int {
    val sortedPairs = boxes
        .asSortedPairs
        .toMutableList()

    val circuits = boxes
        .asElements
        .map { mutableSetOf(it) }
        .toMutableList()

    repeat(1000) {
        val (boxA, boxB) = sortedPairs.removeFirst()

        val circuitA = circuits.firstOrNull { boxA in it }
        val circuitB = circuits.firstOrNull { boxB in it }

        if (circuitA != null && circuitB != null && circuitA != circuitB) {
            circuitA.addAll(circuitB)
            circuits.remove(circuitB)
        }
    }

    return circuits
        .map { it.size }
        .sortedDescending()
        .take(3)
        .reduce(Int::times)
}

private fun part2(boxes: Boxes): Long {
    val sortedPairs = boxes
        .asSortedPairs
        .toMutableList()

    val circuits = boxes
        .asElements
        .map { mutableSetOf(it) }
        .toMutableList()

    while (true) {
        val (boxA, boxB) = sortedPairs.removeFirst()

        val circuitA = circuits.firstOrNull { boxA in it }
        val circuitB = circuits.firstOrNull { boxB in it }

        if (circuitA != null && circuitB != null && circuitA != circuitB) {
            circuitA.addAll(circuitB)
            circuits.remove(circuitB)
        }

        if (circuits.size == 1) {
            return boxA.x.toLong() * boxB.x.toLong()
        }
    }
}

private fun Box.distanceTo(other: Box): Double {
    val dx = (this.x - other.x).toDouble()
    val dy = (this.y - other.y).toDouble()
    val dz = (this.z - other.z).toDouble()
    return sqrt(dx * dx + dy * dy + dz * dz)
}
