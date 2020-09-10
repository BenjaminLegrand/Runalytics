package fr.legrand.runalytics.presentation.ui.session.list.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import fr.legrand.runalytics.presentation.ui.session.list.item.SessionViewDataWrapper
import kotlinx.android.synthetic.main.view_session_list_item.view.session_item_date
import kotlinx.android.synthetic.main.view_session_list_item.view.session_item_duration

class SessionListViewHolder(private val view: View) :
    RecyclerView.ViewHolder(view) {

    fun bindItem(session: SessionViewDataWrapper) {
        with(view) {
            session_item_date.text = session.getStartDateText()
            session_item_duration.text = session.getDurationText(context)
        }
    }
}