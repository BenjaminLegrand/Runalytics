package fr.legrand.runalytics.presentation.ui.main

import android.os.Bundle
import androidx.navigation.ui.setupWithNavController
import fr.legrand.runalytics.R
import fr.legrand.runalytics.presentation.ui.base.BaseNavActivity
import fr.legrand.runalytics.presentation.ui.main.navigator.MainActivityNavigatorListener
import kotlinx.android.synthetic.main.activity_main.activity_main_bottom_nav_view
import org.koin.androidx.scope.currentScope
import org.koin.core.parameter.parametersOf

class MainActivity : BaseNavActivity() {

    private val navigatorListener: MainActivityNavigatorListener by lazy {
        currentScope.get<MainActivityNavigatorListener>(
            MainActivityNavigatorListener::class,
            null
        ) {
            parametersOf(
                this
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity_main_bottom_nav_view.setupWithNavController(navController)
    }


    override fun getNavHostId(): Int = R.id.main_container

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onBackPressed() {
        navigatorListener.onMainActivityBackPressed()
    }
}