package fr.legrand.runalytics.data.manager.storage

import fr.legrand.runalytics.data.entity.SessionDBEntity

interface StorageManager {
    fun getAllSessions(): List<SessionDBEntity>

    fun getSessionById(id: Int): SessionDBEntity

    fun saveSession(sessionDBEntity: SessionDBEntity)
}