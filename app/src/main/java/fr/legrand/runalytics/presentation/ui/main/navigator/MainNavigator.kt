package fr.legrand.runalytics.presentation.ui.main.navigator

import androidx.navigation.NavController
import fr.legrand.runalytics.presentation.ui.base.BaseActivity
import fr.legrand.runalytics.presentation.ui.map.navigator.MapFragmentNavigatorListener


class MainNavigator(
    private val navController: NavController,
    private val baseActivity: BaseActivity
) : MapFragmentNavigatorListener {

}
