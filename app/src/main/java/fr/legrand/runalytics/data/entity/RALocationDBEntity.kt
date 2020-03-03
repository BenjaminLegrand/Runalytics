package fr.legrand.runalytics.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RALocationDBEntity(
    @PrimaryKey val id : Int = 0,
    val speed: Float = 0f,
    val distance: Float = 0f,
    val altitude: Float = 0f,
    val timestamp: Long = 0
)