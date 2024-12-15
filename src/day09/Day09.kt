package day09

import readInput
import java.util.*

sealed class DiskMapEntry() {
    data class File(val id: Int) : DiskMapEntry()
    data object Free : DiskMapEntry()
}

fun List<DiskMapEntry>.lastIndexOfFileInDiskMap(): Int = this.indexOfLast { it is DiskMapEntry.File }
fun List<DiskMapEntry>.firstIndexOfFreeInDiskMap(): Int = this.indexOfFirst { it is DiskMapEntry.Free }
fun List<DiskMapEntry>.isDiskMapCompact(): Boolean = this.lastIndexOfFileInDiskMap() < this.firstIndexOfFreeInDiskMap()
fun List<DiskMapEntry>.toBlockedDiskmap(): List<BlockedDiskMapEntry> {
    val result = mutableListOf<BlockedDiskMapEntry>()
    var currentEntry: BlockedDiskMapEntry? = null
    this.forEachIndexed { index, entry ->
        if (currentEntry == null) {
            if (entry is DiskMapEntry.Free) {
                currentEntry = BlockedDiskMapEntry.Free(1)
            } else if (entry is DiskMapEntry.File) {
                currentEntry = BlockedDiskMapEntry.File(entry.id, 1)
            }
        } else if (currentEntry is BlockedDiskMapEntry.File && entry is DiskMapEntry.File) {
            val currentFile = (currentEntry as BlockedDiskMapEntry.File)
            if (currentFile.id == entry.id) {
                currentEntry = currentFile.copy(size = currentFile.size + 1)
            } else {
                result.add(currentFile)
                currentEntry = BlockedDiskMapEntry.File(entry.id, 1)
            }
        } else if (currentEntry is BlockedDiskMapEntry.Free && entry is DiskMapEntry.Free) {
            val currentFree = (currentEntry as BlockedDiskMapEntry.Free)
            currentEntry = currentFree.copy(size = currentFree.size + 1)
        } else if (entry is DiskMapEntry.File) {
            result.add(currentEntry!!)
            currentEntry = BlockedDiskMapEntry.File(entry.id, 1)
        } else if (entry is DiskMapEntry.Free) {
            result.add(currentEntry!!)
            currentEntry = BlockedDiskMapEntry.Free(1)
        }

        if (index + 1 == this.size) {
            result.add(currentEntry!!)
        }
    }
    return result
}

fun List<DiskMapEntry>.calculateCheckSum() =
    this.filterIsInstance<DiskMapEntry.File>().mapIndexed { index, diskMapEntry ->
        index.toLong() * diskMapEntry.id.toLong()
    }.sum()

sealed class BlockedDiskMapEntry(open val size: Int = 1) {
    data class File(val id: Int, override val size: Int) : BlockedDiskMapEntry(size)
    data class Free(override val size: Int) : BlockedDiskMapEntry(size)
}

fun List<BlockedDiskMapEntry>.lastIndexOfFileInBlockedDiskMap(size: Int): Int =
    this.indexOfLast { it is BlockedDiskMapEntry.File && it.size == size }

fun List<BlockedDiskMapEntry>.firstIndexOfFreeInBlockedDiskMap(size: Int): Int =
    this.indexOfFirst { it is BlockedDiskMapEntry.Free && it.size >= size }

fun List<BlockedDiskMapEntry>.fileSizes(): Set<Int> =
    this.filterIsInstance<BlockedDiskMapEntry.File>().map { it.size }.toSet()

fun List<BlockedDiskMapEntry>.isBlockedDiskMapCompact(): Boolean =
    fileSizes().all { firstIndexOfFreeInBlockedDiskMap(it) > lastIndexOfFileInBlockedDiskMap(it) }

fun List<BlockedDiskMapEntry>.mergeFreeItems(): MutableList<BlockedDiskMapEntry> {
    val result = mutableListOf<BlockedDiskMapEntry>()
    var currentFreeSum = 0

    for (entry in this) {
        when (entry) {
            is BlockedDiskMapEntry.Free -> currentFreeSum += entry.size
            is BlockedDiskMapEntry.File -> {
                if (currentFreeSum > 0) {
                    result.add(BlockedDiskMapEntry.Free(currentFreeSum))
                    currentFreeSum = 0
                }
                result.add(entry)
            }
        }
    }

    if (currentFreeSum > 0) result.add(BlockedDiskMapEntry.Free(currentFreeSum))

    return result
}

fun List<BlockedDiskMapEntry>.toDiskMap(): List<DiskMapEntry> =
    this.flatMap { blockedDiskMapEntry ->
        when (blockedDiskMapEntry) {
            is BlockedDiskMapEntry.File -> List(blockedDiskMapEntry.size) { DiskMapEntry.File(blockedDiskMapEntry.id) }
            is BlockedDiskMapEntry.Free -> List(blockedDiskMapEntry.size) { DiskMapEntry.Free }
        }
    }


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

    fun part1(input: List<String>): Long {
        val diskMap = parseDiskMap(input).toMutableList()

        while (!diskMap.isDiskMapCompact()) {
            Collections.swap(diskMap, diskMap.firstIndexOfFreeInDiskMap(), diskMap.lastIndexOfFileInDiskMap())
        }

        return diskMap.calculateCheckSum()
    }

    fun part2(input: List<String>): Long {
        var blockedDiskMap = parseDiskMap(input).toBlockedDiskmap().toMutableList()

        while (!blockedDiskMap.isBlockedDiskMapCompact()) {
            val swappableFileIndex = blockedDiskMap.indexOfLast { entry ->
                val firstIndexOfFreeInBlockedDiskMap = blockedDiskMap.firstIndexOfFreeInBlockedDiskMap(entry.size)
                entry is BlockedDiskMapEntry.File && firstIndexOfFreeInBlockedDiskMap != -1 &&
                        firstIndexOfFreeInBlockedDiskMap < blockedDiskMap.indexOf(entry)
            }
            val swappableFile = blockedDiskMap[swappableFileIndex]
            val swappableFreeIndex = blockedDiskMap.firstIndexOfFreeInBlockedDiskMap(swappableFile.size)
            val swappableFree = blockedDiskMap[swappableFreeIndex]

            val inPlaceOfFree = if (swappableFree.size > swappableFile.size) {
                listOf(swappableFile, BlockedDiskMapEntry.Free(swappableFree.size - swappableFile.size))
            } else {
                listOf(swappableFile)
            }

            val inPlaceOfFile = BlockedDiskMapEntry.Free(swappableFile.size)

            blockedDiskMap = (blockedDiskMap.subList(0, swappableFreeIndex) + inPlaceOfFree +
                    blockedDiskMap.subList(swappableFreeIndex + 1, swappableFileIndex) + inPlaceOfFile +
                    blockedDiskMap.subList(swappableFileIndex + 1, blockedDiskMap.size)).toMutableList()

            blockedDiskMap = blockedDiskMap.mergeFreeItems()
        }

        val diskMap = blockedDiskMap.toDiskMap().toMutableList()

        return diskMap.calculateCheckSum()
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}