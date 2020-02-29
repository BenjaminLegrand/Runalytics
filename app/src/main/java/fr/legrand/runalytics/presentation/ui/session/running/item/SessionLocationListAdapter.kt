package fr.legrand.runalytics.presentation.ui.session.running.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.legrand.runalytics.R

class SessionLocationListAdapter : RecyclerView.Adapter<SessionLocationViewHolder>() {

    private val locationList = mutableListOf<RALocationViewDataWrapper>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionLocationViewHolder {
        return SessionLocationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_session_location_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = locationList.size

    override fun onBindViewHolder(holder: SessionLocationViewHolder, position: Int) {
        holder.bind(locationList[position])
    }

    fun addLocation(location: RALocationViewDataWrapper) {
        locationList.add(location)
        notifyItemInserted(locationList.size - 1)
    }

    fun resetList() {
        locationList.clear()
        notifyDataSetChanged()
    }
}