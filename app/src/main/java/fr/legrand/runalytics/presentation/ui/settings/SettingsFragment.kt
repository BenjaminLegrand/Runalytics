package fr.legrand.runalytics.presentation.ui.settings

import android.os.Bundle
import android.view.View
import fr.legrand.runalytics.R
import fr.legrand.runalytics.presentation.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings.fragment_settings_dark_mode_switch

class SettingsFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_settings_dark_mode_switch.setOnCheckedChangeListener { _, checked ->
           //TODO
        }
    }
}