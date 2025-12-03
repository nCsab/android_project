package com.example.android_project.ui.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_project.R
import com.example.android_project.model.ProgressResponseDto

class RecentActivitiesAdapter(private var activities: List<ProgressResponseDto>) :
    RecyclerView.Adapter<RecentActivitiesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(android.R.id.text1)
        val tvDetails: TextView = view.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activities[position]
        holder.tvDate.text = activity.date
        holder.tvDetails.text = "Logged: ${activity.loggedTime} min - ${activity.notes ?: "No notes"}"
    }

    override fun getItemCount() = activities.size

    fun updateData(newActivities: List<ProgressResponseDto>) {
        activities = newActivities
        notifyDataSetChanged()
    }
}
