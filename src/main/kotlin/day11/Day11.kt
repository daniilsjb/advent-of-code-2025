package day11

import java.io.File

fun main() {
    val data = parse("src/main/kotlin/day11/Day11.txt")

    println("🎄 Day 11 🎄")

    println()

    println("[Part 1]")
    println("Answer: ${part1(data)}")

    println()

    println("[Part 2]")
    println("Answer: ${part2(data)}")
}

private data class Device(
    val name: String,
    val connections: List<String>
)

private fun parse(path: String): List<Device> =
    File(path).readLines().map { it.toDevice() }

private fun String.toDevice(): Device {
    val (name, connections) = this.split(": ")
    return Device(
        name = name,
        connections = connections.split(' '),
    )
}

private fun part1(data: List<Device>): Long {
    val queue = ArrayDeque<String>()
    queue += "you"

    var answer = 0L
    while (queue.isNotEmpty()) {
        val name = queue.removeFirst()
        val connections = data.find { it.name == name }
            ?.connections
            ?: error("Device '$name' not found")

        for (connection in connections) {
            if (connection == "out") {
                answer += 1
            } else {
                queue.add(connection)
            }

        }
    }

    return answer
}

private fun part2(data: List<Device>): Long {
    data class State(
        val name: String,
        val foundDAC: Boolean = false,
        val foundFFT: Boolean = false,
    )

    val cache = mutableMapOf<State, Long>()

    fun solve(state: State): Long {
        val cached = cache[state]
        if (cached != null) {
            return cached
        }

        val connections = data.find { it.name == state.name }
            ?.connections
            ?: error("Device '${state.name}' not found")

        val result = connections.sumOf { connection ->
            val foundDAC = state.foundDAC || connection == "dac"
            val foundFFT = state.foundFFT || connection == "fft"

            if (connection == "out") {
                return@sumOf if (foundDAC && foundFFT) 1 else 0
            }

            val next = State(
                connection,
                foundDAC = foundDAC,
                foundFFT = foundFFT,
            )

            solve(next)
        }

        return result.also { cache[state] = it }
    }

    return solve(State("svr"))
}
