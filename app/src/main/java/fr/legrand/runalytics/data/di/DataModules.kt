package fr.legrand.runalytics.data.di

import fr.legrand.runalytics.data.manager.location.LocationManager
import fr.legrand.runalytics.data.manager.location.LocationManagerImpl
import fr.legrand.runalytics.data.manager.storage.StorageManager
import fr.legrand.runalytics.data.manager.storage.StorageManagerImpl
import fr.legrand.runalytics.data.mapper.RALocationDBEntityDataMapper
import fr.legrand.runalytics.data.mapper.SessionDBEntityDataMapper
import fr.legrand.runalytics.data.repository.LocationRepository
import org.koin.dsl.module

val managerModule = module {
    single<LocationManager> { LocationManagerImpl(get()) }
    single<StorageManager> { StorageManagerImpl(get()) }
}
val repositoryModule = module {
    single { LocationRepository(get(), get(), get()) }
}

val mapperModule = module {
    single { SessionDBEntityDataMapper(get()) }
    single { RALocationDBEntityDataMapper() }
}


val generalModule = module {

}

val componentModule = module {
}

val dataModules =
    managerModule + repositoryModule + mapperModule + generalModule + componentModule