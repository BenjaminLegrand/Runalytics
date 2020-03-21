package fr.legrand.runalytics.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.legrand.runalytics.data.model.RALocation

@Entity
data class SessionDBEntity(
    @PrimaryKey var id: Long = 0,
    var startDate: Long = 0,
    var endDate: Long = 0,
    var traveledDistance: Float = 0f,
    var altitudeDiff : Float = 0f,
    var kmTimeList : List<Long> = emptyList(),
    val locations: List<RALocationDBEntity> = mutableListOf()
)