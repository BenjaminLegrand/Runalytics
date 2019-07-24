package fr.legrand.runalytics.presentation.di

import androidx.navigation.findNavController
import fr.legrand.runalytics.presentation.ui.base.BaseNavActivity
import fr.legrand.runalytics.presentation.ui.main.MainActivity
import fr.legrand.runalytics.presentation.ui.main.navigator.MainNavigator
import fr.legrand.runalytics.presentation.ui.map.navigator.MapFragmentNavigatorListener
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainActivityModule = module {
    scope(named<MainActivity>()) {

        scoped { (activity: BaseNavActivity) ->
            activity.findNavController(activity.getNavHostId())
        }

        scoped<MapFragmentNavigatorListener> { (activity: BaseNavActivity) ->
            get<MainNavigator>(parameters = {
                parametersOf(
                    activity
                )
            })
        }

        scoped { (activity: BaseNavActivity) ->
            MainNavigator(
                get(parameters = { parametersOf(activity) }),
                activity
            )
        }
    }
}