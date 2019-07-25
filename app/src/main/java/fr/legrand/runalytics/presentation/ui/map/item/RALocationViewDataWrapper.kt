package fr.legrand.runalytics.presentation.ui.map.item

import fr.legrand.runalytics.data.model.RALocation

private const val MS_TO_S = 1000
private const val M_TO_DAM = 10

data class RALocationViewDataWrapper(private val location: RALocation) {

    fun getText() = location.toString()

    fun getFullDistanceKm() = location.fullDistance / M_TO_DAM

    fun getFloatTimestamp() = (location.timestamp / MS_TO_S).toFloat()
    fun getAltitudeDiff() = location.fullAltitudeDiff
    fun getSpeed() = location.currentSpeed
}