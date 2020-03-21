package fr.legrand.runalytics.presentation.ui.session.list.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.legrand.runalytics.R
import fr.legrand.runalytics.presentation.ui.session.list.item.SessionViewDataWrapper

class SessionListAdapter : RecyclerView.Adapter<SessionListViewHolder>() {
    private val sessionList = mutableListOf<SessionViewDataWrapper>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_session_list_item, parent, false)
        return SessionListViewHolder(view)
    }

    override fun getItemCount() = sessionList.size

    override fun onBindViewHolder(holder: SessionListViewHolder, position: Int) {
        holder.bindItem(sessionList[position])
    }

    fun setItems(items: List<SessionViewDataWrapper>) {
        sessionList.clear()
        sessionList.addAll(items)
        notifyDataSetChanged()
    }
}