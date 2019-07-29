package fr.legrand.runalytics.presentation.ui.session.list.ui

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.legrand.runalytics.R
import fr.legrand.runalytics.presentation.ui.session.list.item.SessionViewDataWrapper

class SessionListViewHolder(private val context: Context, view: View) :
    RecyclerView.ViewHolder(view) {

    private val duration : TextView = view.findViewById(R.id.session_item_duration)

    fun bindItem(session: SessionViewDataWrapper) {
        duration.text = session.getDuration()
    }
}