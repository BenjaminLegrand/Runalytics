package fr.legrand.runalytics.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RALocationDBEntity(
    @PrimaryKey val id : Int = 0,
    val currentSpeed: Float = 0f,
    val lastDistance: Float = 0f,
    val fullDistance: Float = 0f,
    val altitude: Float = 0f,
    val fullAltitudeDiff: Float = 0f,
    val timestamp: Long = 0,
    val currentKmTime: Int = 0
)