package fr.legrand.runalytics.data.model

enum class SessionState {
    WAITING_FOR_LOCATION,
    LOW_ACCURACY,
    LOW_SPEED,
    VALID,
}