package fr.legrand.runalytics.presentation.ui.map

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import fr.legrand.runalytics.R
import fr.legrand.runalytics.presentation.ui.base.BaseNavFragment
import fr.legrand.runalytics.presentation.ui.map.navigator.MapFragmentNavigatorListener
import fr.legrand.runalytics.presentation.utils.observeSafe
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : BaseNavFragment<MapFragmentNavigatorListener>() {

    override val navListenerClass = MapFragmentNavigatorListener::class

    private val viewModel: MapFragmentViewModel by viewModel()

    private val distanceChartEntries = mutableListOf<Entry>()
    private val speedChartEntries = mutableListOf<Entry>()
    private val altitudeChartEntries = mutableListOf<Entry>()

    override fun getLayoutId() = R.layout.fragment_map

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.traveledDistanceLiveData.observeSafe(this) {
            fragment_map_traveled_distance.text = it.toString()

            distanceChartEntries.add(Entry(it.getFloatTimestamp(), it.getFullDistanceKm()))
            altitudeChartEntries.add(Entry(it.getFloatTimestamp(), it.getAltitudeDiff()))
            speedChartEntries.add(Entry(it.getFloatTimestamp(), it.getSpeed()))
            fragment_map_chart.apply {
                data = LineData(
                    LineDataSet(distanceChartEntries, "Distance").apply { color = Color.RED },
                    LineDataSet(speedChartEntries, "Speed").apply { color = Color.GREEN },
                    LineDataSet(altitudeChartEntries, "Altitude +").apply { color = Color.BLUE }
                )
                invalidate()
            }
        }

    }

}