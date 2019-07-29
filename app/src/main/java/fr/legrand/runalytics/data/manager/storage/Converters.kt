package fr.legrand.runalytics.data.manager.storage

import androidx.room.TypeConverter
import com.google.gson.Gson
import fr.legrand.runalytics.data.entity.RALocationDBEntity
import fr.legrand.runalytics.data.utils.fromJson

class RoomConverters {
    @TypeConverter
    fun stringListfromString(value: String): List<RALocationDBEntity> {
        val array = Gson().fromJson<Array<RALocationDBEntity>>(value)
        return array.toList()
    }

    @TypeConverter
    fun stringListToString(value: List<RALocationDBEntity>): String {
        return Gson().toJson(value)
    }
}
