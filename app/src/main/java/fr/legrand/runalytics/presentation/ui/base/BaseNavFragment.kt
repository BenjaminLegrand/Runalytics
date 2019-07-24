package fr.legrand.runalytics.presentation.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import org.koin.androidx.scope.currentScope
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass

abstract class BaseNavFragment<T : Any> : BaseFragment() {

    lateinit var navigatorListener: T

    abstract val navListenerClass: KClass<T>

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigatorListener =
            requireActivity().currentScope.get(navListenerClass, null) {
                parametersOf(
                    activity
                )
            }
    }
}
