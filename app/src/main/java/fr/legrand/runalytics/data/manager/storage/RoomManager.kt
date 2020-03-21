package fr.legrand.runalytics.data.manager.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.legrand.runalytics.data.entity.SessionDBEntity

@Dao
interface RoomManager {
    @Query("SELECT * FROM sessiondbentity")
    fun getAllSessions(): List<SessionDBEntity>

    @Query("SELECT * FROM sessiondbentity WHERE id == :id")
    fun getSessionById(id: Int): SessionDBEntity

    @Insert
    fun saveSession(sessionDBEntity: SessionDBEntity)

}