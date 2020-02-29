package fr.legrand.runalytics.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SessionDBEntity(
    @PrimaryKey var id: Long = 0,
    var startDate: Long = 0,
    var endDate: Long = 0,
    val locations: List<RALocationDBEntity> = emptyList()
)