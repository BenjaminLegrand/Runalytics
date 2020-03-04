package fr.legrand.runalytics.presentation.ui.main

import fr.legrand.runalytics.R
import fr.legrand.runalytics.presentation.ui.base.BaseNavActivity
import fr.legrand.runalytics.presentation.ui.main.navigator.MainActivityNavigatorListener
import org.koin.androidx.scope.currentScope
import org.koin.core.parameter.parametersOf

class MainActivity : BaseNavActivity() {

    private val navigatorListener: MainActivityNavigatorListener by lazy{
        currentScope.get<MainActivityNavigatorListener>(MainActivityNavigatorListener::class, null) {
            parametersOf(
                this
            )
        }
    }


    override fun getNavHostId(): Int = R.id.main_container

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onBackPressed() {
        navigatorListener.onMainActivityBackPressed()
    }
}