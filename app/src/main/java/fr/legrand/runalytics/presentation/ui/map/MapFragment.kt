package fr.legrand.runalytics.presentation.ui.map

import android.os.Bundle
import android.view.View
import fr.legrand.runalytics.R
import fr.legrand.runalytics.presentation.ui.base.BaseNavFragment
import fr.legrand.runalytics.presentation.ui.map.navigator.MapFragmentNavigatorListener
import fr.legrand.runalytics.presentation.utils.observeSafe
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : BaseNavFragment<MapFragmentNavigatorListener>() {

    override val navListenerClass = MapFragmentNavigatorListener::class

    private val viewModel: MapFragmentViewModel by viewModel()

    override fun getLayoutId() = R.layout.fragment_map

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.traveledDistanceLiveData.observeSafe(this) {
            fragment_map_traveled_distance.text = it.toString()
        }

    }

}