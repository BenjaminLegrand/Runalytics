package fr.legrand.runalytics.data.model

data class Session(
    var id : Long = 0,
    var startDate: Long = 0,
    var endDate: Long = 0,
    var traveledDistance: Float = 0f,
    var altitudeDiff : Float = 0f,
    var kmTimeList : List<Long> = emptyList(),
    val locations: MutableList<RALocation> = mutableListOf()
)