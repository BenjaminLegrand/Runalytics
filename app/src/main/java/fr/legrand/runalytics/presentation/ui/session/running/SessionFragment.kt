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
import fr.legrand.runalytics.data.model.SessionState
import fr.legrand.runalytics.presentation.component.error.ErrorDisplayComponent
import fr.legrand.runalytics.presentation.ui.base.BaseNavFragment
import fr.legrand.runalytics.presentation.ui.session.running.item.SessionLocationListAdapter
import fr.legrand.runalytics.presentation.ui.session.running.navigator.SessionFragmentNavigatorListener
import fr.legrand.runalytics.presentation.utils.*
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

        observeSession()

        viewModel.sessionSaved.observe(viewLifecycleOwner) {
            navigatorListener.onSessionFinished()
        }

        viewModel.errorEvent.observeSafe(viewLifecycleOwner) {
            errorDisplayComponent.displayError(requireActivity(), it)
        }

        fragment_session_start_button.setOnClickListener {
            sessionLocationListAdapter.resetList()
            fragment_session_duration_group.hide()
            fragment_session_current_km_duration_group.hide()
            fragment_session_distance_group.hide()
            fragment_session_last_km_duration_group.hide()
            fragment_session_data_list_group.hide()
            viewModel.startSession()
            fragment_session_start_button.isEnabled = false
            fragment_session_stop_button.isEnabled = true
            fragment_session_save_button.isEnabled = true
        }
        fragment_session_stop_button.setOnClickListener {
            viewModel.stopLocationComputation()
            fragment_session_start_button.isEnabled = true
            fragment_session_stop_button.isEnabled = false
            fragment_session_save_button.isEnabled = false
        }
        fragment_session_save_button.setOnClickListener { viewModel.saveCurrentSession() }

        fragment_session_chart_switch.setOnCheckedChangeListener { _, checked ->
            fragment_session_chart.setVisible(checked)
            fragment_session_data_list_group.setVisible(!checked)
        }
    }

    private fun observeSession() {
        viewModel.sessionState.observeSafe(viewLifecycleOwner) {

            when (it) {
                SessionState.WAITING_FOR_LOCATION -> {
                    fragment_session_location_waiting_text.show()
                    fragment_session_low_accuracy_text.hide()
                    fragment_session_low_speed_text.hide()
                }
                SessionState.LOW_ACCURACY -> {
                    fragment_session_low_accuracy_text.show()
                    fragment_session_location_waiting_text.hide()
                    fragment_session_low_speed_text.hide()
                }
                SessionState.LOW_SPEED -> {
                    fragment_session_low_speed_text.show()
                    fragment_session_location_waiting_text.hide()
                    fragment_session_low_accuracy_text.hide()
                }
                else -> {
                    fragment_session_location_waiting_text.hide()
                    fragment_session_low_accuracy_text.hide()
                    fragment_session_low_speed_text.hide()
                }
            }
        }

        viewModel.sessionUpdate.observeSafe(viewLifecycleOwner) {
            it.getLastLocation()?.let { location ->
                fragment_session_distance_group.show()
                fragment_session_data_list_group.setVisible(!fragment_session_chart_switch.isChecked)
                sessionLocationListAdapter.addLocation(location)
                speedChartEntries.add(Entry(location.getFloatTimestamp(), location.getSpeed()))
                distanceChartEntries.add(
                    Entry(
                        location.getFloatTimestamp(),
                        it.getFullDistanceKm()
                    )
                )
                altitudeChartEntries.add(Entry(location.getFloatTimestamp(), it.getAltitudeDiff()))
            }

            fragment_session_chart.apply {
                data = LineData(
                    LineDataSet(distanceChartEntries, "Distance").apply { color = Color.RED },
                    LineDataSet(speedChartEntries, "Speed").apply { color = Color.GREEN },
                    LineDataSet(altitudeChartEntries, "Altitude +").apply { color = Color.BLUE }
                )
                invalidate()
            }
        }


        viewModel.sessionTimer.observeSafe(viewLifecycleOwner) {
            fragment_session_duration_group.show()
            fragment_session_duration_text.text = requireContext().getString(
                R.string.running_session_duration_format,
                it.first,
                it.second,
                it.third
            )
        }


        viewModel.traveledDistance.observeSafe(viewLifecycleOwner) {
            fragment_session_distance_group.show()
            fragment_session_distance_text.text =
                requireContext().getString(R.string.running_session_distance_format, it)
        }

        viewModel.currentKmTime.observeSafe(viewLifecycleOwner) {
            if (it > 0) {
                fragment_session_current_km_duration_text.text =
                    requireContext().getString(
                        R.string.running_session_current_km_time_format,
                        TimeUtils.extractTimeText(requireContext(), it)
                    )
                fragment_session_current_km_duration_group.show()
            } else {
                fragment_session_current_km_duration_group.hide()
            }
        }

        viewModel.lastKmTime.observeSafe(viewLifecycleOwner) {
            if (it > 0) {
                fragment_session_last_km_duration_text.text =
                    requireContext().getString(
                        R.string.running_session_last_km_time_format,
                        TimeUtils.extractTimeText(requireContext(), it)
                    )
                fragment_session_last_km_duration_group.show()
            } else {
                fragment_session_last_km_duration_group.hide()
            }

        }
    }

}