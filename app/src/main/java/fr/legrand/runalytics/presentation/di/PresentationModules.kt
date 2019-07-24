package fr.legrand.runalytics.presentation.di

import fr.legrand.runalytics.presentation.ui.map.MapFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val activityModules = arrayOf(
    mainActivityModule
)

private val viewModelModule = module {
    viewModel { MapFragmentViewModel(get()) }
}

private val adapterModule = module {
}

val presentationModules = activityModules + viewModelModule + adapterModule