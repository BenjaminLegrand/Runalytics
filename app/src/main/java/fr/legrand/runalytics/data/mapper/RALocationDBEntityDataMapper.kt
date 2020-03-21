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
                speed = model.speed,
                distance = model.distance,
                altitude = model.altitude,
                timestamp = model.timestamp
            )
        } catch (e: Exception) {
            throw MappingException()
        }
    }

    fun transform(db: RALocationDBEntity): RALocation {
        try {
            return RALocation(
                speed = db.speed,
                distance = db.distance,
                altitude = db.altitude,
                timestamp = db.timestamp
            )
        } catch (e: Exception) {
            throw MappingException()
        }
    }
}