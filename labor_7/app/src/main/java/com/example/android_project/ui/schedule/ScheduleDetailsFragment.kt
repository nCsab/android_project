package com.example.android_project.ui.schedule

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_project.R

class ScheduleDetailsFragment : Fragment() {

    private val viewModel: ScheduleDetailsViewModel by viewModels()
    // Assuming you are using SafeArgs, otherwise retrieve ID from bundle
    // private val args: ScheduleDetailsFragmentArgs by navArgs()
    private var scheduleId: Long = -1

    private lateinit var tvHabitName: TextView
    private lateinit var tvHabitDescription: TextView
    private lateinit var tvScheduleTime: TextView
    private lateinit var tvNotes: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var rvRecentActivities: RecyclerView
    private lateinit var btnDeleteSchedule: Button
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var adapter: RecentActivitiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_schedule_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Views
        tvHabitName = view.findViewById(R.id.tvHabitName)
        tvHabitDescription = view.findViewById(R.id.tvHabitDescription)
        tvScheduleTime = view.findViewById(R.id.tvScheduleTime)
        tvNotes = view.findViewById(R.id.tvNotes)
        progressBar = view.findViewById(R.id.progressBar)
        rvRecentActivities = view.findViewById(R.id.rvRecentActivities)
        btnDeleteSchedule = view.findViewById(R.id.btnDeleteSchedule)
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar)

        // Setup RecyclerView
        adapter = RecentActivitiesAdapter(emptyList())
        rvRecentActivities.layoutManager = LinearLayoutManager(context)
        rvRecentActivities.adapter = adapter

        // Get Schedule ID (Replace with SafeArgs if available)
        arguments?.let {
            scheduleId = it.getLong("scheduleId", -1)
        }

        if (scheduleId != -1L) {
            viewModel.fetchScheduleDetails(scheduleId)
        } else {
            Toast.makeText(context, "Invalid Schedule ID", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        // Observe ViewModel
        viewModel.schedule.observe(viewLifecycleOwner) { schedule ->
            schedule?.let {
                tvHabitName.text = it.habit?.name ?: "Unknown Habit"
                tvHabitDescription.text = it.habit?.description ?: ""
                tvScheduleTime.text = "Time: ${it.startTime} - ${it.endTime}" // Format this better if needed
                tvNotes.text = it.notes ?: "No notes available."
                
                // Calculate progress if needed, or use a field from response
                // For now, setting dummy progress or based on completed items
                progressBar.progress = if (it.status == "Completed") 100 else 0

                adapter.updateData(it.progress ?: emptyList())
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.deleteSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Schedule deleted successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        // Delete Button Click
        btnDeleteSchedule.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Schedule")
            .setMessage("Are you sure you want to delete this schedule?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteSchedule(scheduleId)
            }
            .setNegativeButton("No", null)
            .show()
    }
}
