package fr.legrand.runalytics.data.mapper

import fr.legrand.runalytics.data.entity.RALocationDBEntity
import fr.legrand.runalytics.data.exception.MappingException
import fr.legrand.runalytics.data.model.RALocation

class RALocationDBEntityDataMapper {
    fun transformEntity(remotes: List<RALocationDBEntity>): List<RALocation> {
        return remotes.mapNotNull {
            try {
                transform(it)
            } catch (e: MappingException) {
                null
            }
        }
    }

    fun transformModel(models: List<RALocation>): List<RALocationDBEntity> {
        return models.mapNotNull {
            try {
                transform(it)
            } catch (e: MappingException) {
                null
            }
        }
    }

    fun transform(model: RALocation): RALocationDBEntity {
        try {
            return RALocationDBEntity(
                currentSpeed = model.currentSpeed,
                lastDistance = model.lastDistance,
                fullDistance = model.fullDistance,
                altitude = model.altitude,
                fullAltitudeDiff = model.fullAltitudeDiff,
                timestamp = model.timestamp,
                currentKmTime = model.currentKmTime
            )
        } catch (e: Exception) {
            throw MappingException()
        }
    }

    fun transform(db: RALocationDBEntity): RALocation {
        try {
            return RALocation(
                currentSpeed = db.currentSpeed,
                lastDistance = db.lastDistance,
                fullDistance = db.fullDistance,
                altitude = db.altitude,
                fullAltitudeDiff = db.fullAltitudeDiff,
                timestamp = db.timestamp,
                currentKmTime = db.currentKmTime
            )
        } catch (e: Exception) {
            throw MappingException()
        }
    }
}