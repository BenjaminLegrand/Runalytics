package fr.legrand.runalytics

import android.app.Application
import fr.legrand.runalytics.data.di.dataModules
import fr.legrand.runalytics.presentation.di.presentationModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RAApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Android context
            androidContext(this@RAApplication)
            // modules
            modules((presentationModules + dataModules).toList())
        }
    }
}