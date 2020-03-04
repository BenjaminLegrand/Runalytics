package fr.legrand.runalytics.presentation.ui.main.navigator

import androidx.navigation.NavController
import fr.legrand.runalytics.presentation.ui.base.BaseActivity
import fr.legrand.runalytics.presentation.ui.session.list.SessionListFragmentDirections
import fr.legrand.runalytics.presentation.ui.session.list.navigator.SessionListFragmentNavigatorListener
import fr.legrand.runalytics.presentation.ui.session.running.navigator.SessionFragmentNavigatorListener


class MainNavigator(
    private val navController: NavController,
    private val baseActivity: BaseActivity
) : SessionFragmentNavigatorListener, SessionListFragmentNavigatorListener,
    MainActivityNavigatorListener {

    override fun startSession() {
        navController.navigate(SessionListFragmentDirections.actionSessionListFragmentToSessionFragment())
    }

    override fun onSessionFinished() {
        if (!navController.navigateUp()) {
            baseActivity.finish()
        }
    }

    override fun onMainActivityBackPressed() {
        if (!navController.navigateUp()) {
            baseActivity.finish()
        }
    }
}
