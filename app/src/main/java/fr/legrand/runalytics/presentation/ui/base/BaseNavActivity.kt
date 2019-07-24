package fr.legrand.runalytics.presentation.ui.base

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import androidx.navigation.NavController
import fr.legrand.runalytics.presentation.ui.base.BaseActivity
import org.koin.androidx.scope.currentScope
import org.koin.core.parameter.parametersOf

@SuppressLint("Registered")
abstract class BaseNavActivity : BaseActivity() {

    protected val navController: NavController by currentScope.inject { parametersOf(this) }

    @IdRes
    abstract fun getNavHostId(): Int
}