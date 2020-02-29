package fr.legrand.runalytics.data.model

data class Session(
    var id : Long = 0,
    var startDate: Long = 0,
    var endDate: Long = 0,
    val locations: MutableList<RALocation> = mutableListOf()
)