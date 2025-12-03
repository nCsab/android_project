package com.example.android_project.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_project.R
import com.example.android_project.model.HabitResponse

class UserHabitsAdapter(private var habits: List<HabitResponse>) :
    RecyclerView.Adapter<UserHabitsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(android.R.id.text1)
        val tvDescription: TextView = view.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habit = habits[position]
        holder.tvName.text = habit.name
        holder.tvDescription.text = habit.description ?: "No description"
    }

    override fun getItemCount() = habits.size

    fun updateData(newHabits: List<HabitResponse>) {
        habits = newHabits
        notifyDataSetChanged()
    }
}
