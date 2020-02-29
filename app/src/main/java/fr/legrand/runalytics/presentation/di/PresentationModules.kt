package fr.legrand.runalytics.presentation.di

import fr.legrand.runalytics.presentation.ui.session.list.SessionListFragmentViewModel
import fr.legrand.runalytics.presentation.ui.session.list.ui.SessionListAdapter
import fr.legrand.runalytics.presentation.ui.session.running.SessionFragmentViewModel
import fr.legrand.runalytics.presentation.ui.session.running.item.SessionLocationListAdapter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val activityModules = arrayOf(
    mainActivityModule
)

private val viewModelModule = module {
    viewModel { SessionFragmentViewModel(get()) }
    viewModel { SessionListFragmentViewModel(get()) }
}

private val adapterModule = module {
    factory { SessionListAdapter() }
    factory { SessionLocationListAdapter() }
}

val presentationModules = activityModules + viewModelModule + adapterModule