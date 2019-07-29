package fr.legrand.runalytics.presentation.di

import fr.legrand.runalytics.presentation.ui.session.running.SessionFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val activityModules = arrayOf(
    mainActivityModule
)

private val viewModelModule = module {
    viewModel { SessionFragmentViewModel(get()) }
}

private val adapterModule = module {
}

val presentationModules = activityModules + viewModelModule + adapterModule