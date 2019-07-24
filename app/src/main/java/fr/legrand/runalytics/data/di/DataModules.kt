package fr.legrand.runalytics.data.di

import fr.legrand.runalytics.data.location.LocationManager
import fr.legrand.runalytics.data.location.LocationManagerImpl
import fr.legrand.runalytics.data.repository.LocationRepository
import org.koin.dsl.module

val managerModule = module {
    single<LocationManager> { LocationManagerImpl(get()) }
}
val repositoryModule = module {
    single { LocationRepository(get()) }
}

val mapperModule = module {
}


val generalModule = module {

}

val componentModule = module {
}

val dataModules =
    managerModule + repositoryModule + mapperModule + generalModule + componentModule