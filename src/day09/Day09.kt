package day09

import readInput
import java.util.*

sealed class DiskMapEntry() {
    data class File(val id: Int) : DiskMapEntry()
    data object Free: DiskMapEntry()
}

fun List<DiskMapEntry>.lastIndexOfFile(): Int = this.indexOfLast { it is DiskMapEntry.File }
fun List<DiskMapEntry>.firstIndexOfFree(): Int = this.indexOfFirst { it is DiskMapEntry.Free }
fun List<DiskMapEntry>.isCompact(): Boolean = this.lastIndexOfFile() < this.firstIndexOfFree()

fun main() {
    fun parseDiskMap(
        input: List<String>
    ): List<DiskMapEntry> {
        var fileIndex = 0
        var isFile = true
        return input.first().flatMap { character ->
            val number = character.digitToInt()
            val returnValue = if (isFile) {
                List(number) { DiskMapEntry.File(fileIndex) }
            } else {
                List(number) { DiskMapEntry.Free }
            }
            if (isFile) fileIndex++
            isFile = !isFile
            returnValue
        }
    }

    fun part1(input: List<String>): Int {
        val diskMap = parseDiskMap(input).toMutableList()

        while (!diskMap.isCompact()) {
            Collections.swap(diskMap, diskMap.firstIndexOfFree(), diskMap.lastIndexOfFile())
        }

        return diskMap.filterIsInstance<DiskMapEntry.File>().mapIndexed { index, diskMapEntry ->
            index * diskMapEntry.id
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return -1
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 1928)
//    check(part2(testInput) == 0)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}