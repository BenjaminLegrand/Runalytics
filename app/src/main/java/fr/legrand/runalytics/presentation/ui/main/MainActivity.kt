package fr.legrand.runalytics.presentation.ui.main

import fr.legrand.runalytics.R
import fr.legrand.runalytics.presentation.ui.base.BaseNavActivity

class MainActivity : BaseNavActivity() {

    override fun getNavHostId(): Int = R.id.main_container

    override fun getLayoutId(): Int = R.layout.activity_main

}