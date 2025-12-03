package com.example.android_project.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android_project.databinding.ItemHomeScheduleBinding
import com.example.android_project.model.ScheduleResponse
import java.time.format.DateTimeFormatter

class HomeScheduleAdapter(
    private val onItemClick: (Long) -> Unit
) : ListAdapter<ScheduleResponse, HomeScheduleAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeScheduleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: ItemHomeScheduleBinding,
        private val onItemClick: (Long) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ScheduleResponse) {
            binding.tvTitle.text = item.habit?.name ?: "Unknown Habit"
            
            val date = item.startTime?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: "No date"
            val startTime = item.startTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "--:--"
            val endTime = item.endTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "--:--"
            binding.tvTime.text = "$date  |  $startTime - $endTime"

            if (!item.notes.isNullOrEmpty()) {
                binding.tvNotes.text = item.notes
                binding.tvNotes.visibility = android.view.View.VISIBLE
            } else {
                binding.tvNotes.visibility = android.view.View.GONE
            }
            
            // Set icon based on category (simplified for now)
            // binding.ivIcon.setImageResource(...)

            binding.root.setOnClickListener {
                onItemClick(item.id)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ScheduleResponse>() {
        override fun areItemsTheSame(oldItem: ScheduleResponse, newItem: ScheduleResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ScheduleResponse, newItem: ScheduleResponse): Boolean {
            return oldItem == newItem
        }
    }
}
