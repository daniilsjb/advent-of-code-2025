package day10

import java.io.File

fun main() {
    val data = parse("src/main/kotlin/day10/Day10.txt")

    println("🎄 Day 10 🎄")

    println()

    println("[Part 1]")
    println("Answer: ${part1(data)}")

    println()

    println("[Part 2]")
    println("Answer: ${part2(data)}")
}

private data class Schematic(
    val lights: List<Boolean>,
    val buttons: List<List<Int>>,
    val requirements: List<Int>,
)

private fun parse(path: String): List<Schematic> =
    File(path).readLines().map { it.toSchematic() }

private fun String.toSchematic(): Schematic {
    val parts = this.split(' ')

    val lights = parts.first()
        .removePrefix("[")
        .removeSuffix("]")
        .map { it == '#' }

    val buttons = parts.subList(1, parts.lastIndex).map { part -> part
        .removePrefix("(")
        .removeSuffix(")")
        .split(",")
        .map { it.toInt() }
    }

    val requirements = parts.last()
        .removePrefix("{")
        .removeSuffix("}")
        .split(',')
        .map { it.toInt() }

    return Schematic(
        lights = lights,
        buttons = buttons,
        requirements = requirements,
    )
}

private fun part1(schematics: List<Schematic>): Int {
    data class Node(
        val lights: List<Boolean>,
        val distance: Int,
        val buttonIndex: Int,
    )

    var sum = 0
    outer@for ((lights, buttons) in schematics) {
        val startingLights = lights.map { false }
        val startingNode = Node(
            lights = startingLights,
            distance = 0,
            buttonIndex = -1,
        )

        val queue = ArrayDeque<Node>()
        queue.add(startingNode)

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            if (node.lights == lights) {
                sum += node.distance
                continue@outer
            }

            for ((index, button) in buttons.withIndex()) {
                if (index != node.buttonIndex) {
                    val updatedLights = node.lights.toMutableList()
                    for (lightIndex in button) {
                        updatedLights[lightIndex] = !updatedLights[lightIndex]
                    }

                    queue += Node(
                        lights = updatedLights,
                        distance = node.distance + 1,
                        buttonIndex = index,
                    )
                }
            }
        }
    }

    return sum
}

private fun part2(schematics: List<Schematic>): String {
    return "Please, run `py Day10.py` instead :^)"
}
