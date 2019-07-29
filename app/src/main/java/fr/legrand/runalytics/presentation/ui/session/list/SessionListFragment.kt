package fr.legrand.runalytics.presentation.ui.session.list

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import fr.legrand.runalytics.R
import fr.legrand.runalytics.presentation.ui.base.BaseNavFragment
import fr.legrand.runalytics.presentation.ui.session.list.navigator.SessionListFragmentNavigatorListener
import fr.legrand.runalytics.presentation.ui.session.list.ui.SessionListAdapter
import fr.legrand.runalytics.presentation.utils.observeSafe
import kotlinx.android.synthetic.main.fragment_session_list.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SessionListFragment : BaseNavFragment<SessionListFragmentNavigatorListener>() {

    override val navListenerClass = SessionListFragmentNavigatorListener::class

    private val viewModel: SessionListFragmentViewModel by viewModel()
    private val sessionListAdapter: SessionListAdapter by inject()

    override fun getLayoutId() = R.layout.fragment_session_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_session_list_recycler_view.adapter = sessionListAdapter
        fragment_session_list_recycler_view.layoutManager = LinearLayoutManager(requireContext())

        viewModel.sessionList.observeSafe(this) {
            sessionListAdapter.setItems(it)
        }

        fragment_session_list_start_session.setOnClickListener {
            navigatorListener.startSession()
        }
    }

}