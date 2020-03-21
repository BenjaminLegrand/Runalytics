package fr.legrand.runalytics.data.utils

import fr.legrand.runalytics.data.model.RALocation

private const val KILOMETER_VALUE = 1000

object LocationUtils {

    fun splitByKilometer(items: List<RALocation>): List<List<RALocation>> {
        val result: MutableList<List<RALocation>> = mutableListOf()

        var currentDistance = 0f
        var currentKmItems = mutableListOf<RALocation>()
        items.forEach {
            currentDistance += it.distance
            if (currentDistance < KILOMETER_VALUE) {
                currentKmItems.add(it)
            } else {
                result.add(currentKmItems)
                currentKmItems = mutableListOf()
                currentKmItems.add(it)
                currentDistance = 0f
            }
        }
        if (currentKmItems.isNotEmpty()) {
            result.add(currentKmItems)
        }

        return result
    }
}