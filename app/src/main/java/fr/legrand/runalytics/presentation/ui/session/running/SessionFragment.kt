package fr.legrand.runalytics.presentation.ui.session.running

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import fr.legrand.runalytics.R
import fr.legrand.runalytics.presentation.ui.base.BaseNavFragment
import fr.legrand.runalytics.presentation.ui.session.running.navigator.SessionFragmentNavigatorListener
import fr.legrand.runalytics.presentation.utils.observe
import fr.legrand.runalytics.presentation.utils.observeSafe
import kotlinx.android.synthetic.main.fragment_session.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SessionFragment : BaseNavFragment<SessionFragmentNavigatorListener>() {

    override val navListenerClass = SessionFragmentNavigatorListener::class

    private val viewModel: SessionFragmentViewModel by viewModel()

    private val distanceChartEntries = mutableListOf<Entry>()
    private val speedChartEntries = mutableListOf<Entry>()
    private val altitudeChartEntries = mutableListOf<Entry>()

    override fun getLayoutId() = R.layout.fragment_session

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.traveledDistanceLiveData.observeSafe(this) {
            distanceChartEntries.add(Entry(it.getFloatTimestamp(), it.getFullDistanceKm()))
            altitudeChartEntries.add(Entry(it.getFloatTimestamp(), it.getAltitudeDiff()))
            speedChartEntries.add(Entry(it.getFloatTimestamp(), it.getSpeed()))
            fragment_session_chart.apply {
                data = LineData(
                    LineDataSet(distanceChartEntries, "Distance").apply { color = Color.RED },
                    LineDataSet(speedChartEntries, "Speed").apply { color = Color.GREEN },
                    LineDataSet(altitudeChartEntries, "Altitude +").apply { color = Color.BLUE }
                )
                invalidate()
            }
        }

        viewModel.sessionSaved.observe(this){
            navigatorListener.onSessionFinished()
        }

        fragment_session_start_button.setOnClickListener {  viewModel.startLocationComputation() }
        fragment_session_stop_button.setOnClickListener {  viewModel.stopLocationComputation() }
        fragment_session_save_button.setOnClickListener { viewModel.saveCurrentSession() }
    }

}