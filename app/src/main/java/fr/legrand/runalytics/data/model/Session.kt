package fr.legrand.runalytics.data.model

data class Session(
    val id : Int = 0,
    var startDate: Long = 0,
    var endDate: Long = 0,
    val locations: MutableList<RALocation> = mutableListOf()
)