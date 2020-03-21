package fr.legrand.runalytics.data.manager.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.legrand.runalytics.data.entity.RALocationDBEntity
import fr.legrand.runalytics.data.entity.SessionDBEntity

@Database(
    version = 1,
    entities = [SessionDBEntity::class, RALocationDBEntity::class],
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomManager(): RoomManager
}