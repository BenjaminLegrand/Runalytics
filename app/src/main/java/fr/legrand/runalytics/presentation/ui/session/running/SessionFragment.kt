package fr.legrand.runalytics.presentation.ui.session.running

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import fr.legrand.runalytics.R
import fr.legrand.runalytics.presentation.component.error.ErrorDisplayComponent
import fr.legrand.runalytics.presentation.ui.base.BaseNavFragment
import fr.legrand.runalytics.presentation.ui.session.running.item.SessionLocationListAdapter
import fr.legrand.runalytics.presentation.ui.session.running.navigator.SessionFragmentNavigatorListener
import fr.legrand.runalytics.presentation.utils.observe
import fr.legrand.runalytics.presentation.utils.observeSafe
import fr.legrand.runalytics.presentation.utils.setVisible
import kotlinx.android.synthetic.main.fragment_session.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SessionFragment : BaseNavFragment<SessionFragmentNavigatorListener>() {

    override val navListenerClass = SessionFragmentNavigatorListener::class

    private val viewModel: SessionFragmentViewModel by viewModel()
    private val sessionLocationListAdapter: SessionLocationListAdapter by inject()
    private val errorDisplayComponent: ErrorDisplayComponent by inject()

    private val distanceChartEntries = mutableListOf<Entry>()
    private val speedChartEntries = mutableListOf<Entry>()
    private val altitudeChartEntries = mutableListOf<Entry>()

    override fun getLayoutId() = R.layout.fragment_session

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_session_data_list.layoutManager =
            LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
                reverseLayout = true
                stackFromEnd = true
            }
        fragment_session_data_list.adapter = sessionLocationListAdapter

        viewModel.traveledDistanceLiveData.observeSafe(viewLifecycleOwner) {
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

            sessionLocationListAdapter.addLocation(it)
        }

        viewModel.sessionSaved.observe(viewLifecycleOwner) {
            navigatorListener.onSessionFinished()
        }

        viewModel.errorEvent.observeSafe(viewLifecycleOwner) {
            errorDisplayComponent.displayError(requireActivity(), it)
        }

        viewModel.sessionTimer.observeSafe(viewLifecycleOwner) {
            fragment_session_duration_text.text = requireContext().getString(
                R.string.session_duration_format,
                it.first,
                it.second,
                it.third
            )
        }

        fragment_session_start_button.setOnClickListener {
            sessionLocationListAdapter.resetList()
            viewModel.startLocationComputation()
        }
        fragment_session_stop_button.setOnClickListener { viewModel.stopLocationComputation() }
        fragment_session_save_button.setOnClickListener { viewModel.saveCurrentSession() }

        fragment_session_chart_switch.setOnCheckedChangeListener { _, checked ->
            fragment_session_chart.setVisible(checked)
            fragment_session_data_list.setVisible(checked)
        }
    }

}