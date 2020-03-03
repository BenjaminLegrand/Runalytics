package fr.legrand.runalytics.data.mapper

import fr.legrand.runalytics.data.entity.SessionDBEntity
import fr.legrand.runalytics.data.exception.MappingException
import fr.legrand.runalytics.data.model.Session

class SessionDBEntityDataMapper
    (
    private val raLocationDBEntityDataMapper: RALocationDBEntityDataMapper
) {
    fun transformEntity(remotes: List<SessionDBEntity>): List<Session> {
        return remotes.mapNotNull {
            try {
                transform(it)
            } catch (e: MappingException) {
                null
            }
        }
    }

    fun transformModel(models: List<Session>): List<SessionDBEntity> {
        return models.mapNotNull {
            try {
                transform(it)
            } catch (e: MappingException) {
                null
            }
        }
    }

    fun transform(model: Session): SessionDBEntity {
        try {
            return SessionDBEntity(
                model.id,
                model.startDate,
                model.endDate,
                model.traveledDistance,
                model.altitudeDiff,
                model.kmTimeList,
                raLocationDBEntityDataMapper.transformModel(model.locations)
            )
        } catch (e: Exception) {
            throw MappingException()
        }
    }

    fun transform(db: SessionDBEntity): Session {
        try {
            return Session(
                db.id,
                db.startDate,
                db.endDate,
                db.traveledDistance,
                db.altitudeDiff,
                db.kmTimeList,
                raLocationDBEntityDataMapper.transformEntity(db.locations).toMutableList()
            )
        } catch (e: Exception) {
            throw MappingException()
        }
    }
}