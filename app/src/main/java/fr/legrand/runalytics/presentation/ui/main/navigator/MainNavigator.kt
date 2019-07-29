package fr.legrand.runalytics.presentation.ui.main.navigator

import androidx.navigation.NavController
import fr.legrand.runalytics.presentation.ui.base.BaseActivity
import fr.legrand.runalytics.presentation.ui.session.list.navigator.SessionListFragmentNavigatorListener
import fr.legrand.runalytics.presentation.ui.session.running.navigator.SessionFragmentNavigatorListener


class MainNavigator(
    private val navController: NavController,
    private val baseActivity: BaseActivity
) : SessionFragmentNavigatorListener, SessionListFragmentNavigatorListener {
    override fun startSession() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSessionFinished() {
        navController.navigateUp()
    }



}
