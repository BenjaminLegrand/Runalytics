package fr.legrand.runalytics.presentation.di

import android.app.NotificationManager
import android.content.Context
import fr.legrand.runalytics.data.model.Session
import fr.legrand.runalytics.data.model.SessionState
import fr.legrand.runalytics.presentation.component.background.BackgroundComponent
import fr.legrand.runalytics.presentation.component.background.BackgroundComponentImpl
import fr.legrand.runalytics.presentation.component.error.ErrorDisplayComponent
import fr.legrand.runalytics.presentation.component.error.ErrorDisplayComponentSnackbar
import fr.legrand.runalytics.presentation.component.error.ErrorTranslater
import fr.legrand.runalytics.presentation.component.notification.NotificationComponent
import fr.legrand.runalytics.presentation.component.notification.NotificationComponentImpl
import fr.legrand.runalytics.presentation.ui.session.list.SessionListFragmentViewModel
import fr.legrand.runalytics.presentation.ui.session.list.ui.SessionListAdapter
import fr.legrand.runalytics.presentation.ui.session.running.SessionFragmentViewModel
import io.reactivex.subjects.PublishSubject
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val activityModules = arrayOf(
    mainActivityModule
)

private val viewModelModule = module {
    viewModel { SessionFragmentViewModel(get(), get()) }
    viewModel { SessionListFragmentViewModel(get()) }
}

private val adapterModule = module {
    factory { SessionListAdapter() }
}

private val componentModule = module {
    single<ErrorDisplayComponent> { ErrorDisplayComponentSnackbar(get()) }
    single<NotificationComponent> { NotificationComponentImpl(get(), get()) }
    single<BackgroundComponent> {
        BackgroundComponentImpl(
            get(),
            get(named(InjectionValues.SESSION_PUBLISH)),
            get(named(InjectionValues.SESSION_TIMER_PUBLISH))
        )
    }
    single { ErrorTranslater(get()) }

    single { get<Context>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    single(named(InjectionValues.SESSION_PUBLISH)) { PublishSubject.create<Pair<SessionState, Session>>() }
    single(named(InjectionValues.SESSION_TIMER_PUBLISH)) { PublishSubject.create<Long>() }
}

val presentationModules = activityModules + viewModelModule + adapterModule + componentModule