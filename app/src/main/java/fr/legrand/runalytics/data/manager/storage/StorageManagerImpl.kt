package fr.legrand.runalytics.data.manager.storage

import android.content.Context
import androidx.room.Room
import fr.legrand.runalytics.data.entity.SessionDBEntity

class StorageManagerImpl constructor(context: Context) : StorageManager {

    private val roomManager: RoomManager =
        Room.databaseBuilder(context, AppDatabase::class.java, "database").build().roomManager()

    override fun getAllSessions() = roomManager.getAllSessions()


    override fun getSessionById(id: Int): SessionDBEntity = roomManager.getSessionById(id)

    override fun saveSession(sessionDBEntity: SessionDBEntity) = roomManager.saveSession(sessionDBEntity)

}