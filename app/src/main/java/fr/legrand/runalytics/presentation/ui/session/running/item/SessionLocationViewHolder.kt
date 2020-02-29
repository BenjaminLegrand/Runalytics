package fr.legrand.runalytics.presentation.ui.session.running.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_session_location_item.view.*

class SessionLocationViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(location: RALocationViewDataWrapper) {
        with(view) {
            view_session_location_item_speed.text = location.getSpeedText(context)
        }
    }
}