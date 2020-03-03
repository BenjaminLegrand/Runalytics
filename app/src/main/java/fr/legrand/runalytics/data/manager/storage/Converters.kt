package fr.legrand.runalytics.data.manager.storage

import androidx.room.TypeConverter
import com.google.gson.Gson
import fr.legrand.runalytics.data.entity.RALocationDBEntity
import fr.legrand.runalytics.data.utils.fromJson

class RoomConverters {
    @TypeConverter
    fun locationListFromString(value: String): List<RALocationDBEntity> {
        val array = Gson().fromJson<Array<RALocationDBEntity>>(value)
        return array.toList()
    }

    @TypeConverter
    fun locationListToString(value: List<RALocationDBEntity>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun longListFromString(value: String): List<Long> {
        val array = Gson().fromJson<Array<Long>>(value)
        return array.toList()
    }

    @TypeConverter
    fun longListToString(value: List<Long>): String {
        return Gson().toJson(value)
    }
}
