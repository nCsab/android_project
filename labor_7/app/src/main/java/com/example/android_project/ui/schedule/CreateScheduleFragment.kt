package com.example.android_project.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android_project.databinding.FragmentCreateScheduleBinding
import com.example.android_project.model.HabitResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CreateScheduleFragment : Fragment() {

    private var _binding: FragmentCreateScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CreateScheduleViewModel
    private var habits: List<HabitResponse> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = CreateScheduleViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory)[CreateScheduleViewModel::class.java]

        setupObservers()
        viewModel.fetchHabits()

        binding.btnCreateSchedule.setOnClickListener {
            val startTimeStr = binding.etStartTime.text.toString()
            val durationStr = binding.etDuration.text.toString()
            val notes = binding.etNotes.text.toString()
            val selectedHabitPosition = binding.spinnerHabit.selectedItemPosition

            if (startTimeStr.isEmpty() || durationStr.isEmpty() || habits.isEmpty()) {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val habitId = habits[selectedHabitPosition].id
            val durationMinutes = durationStr.toIntOrNull() ?: 0
            
            val startTime = try {
                var parsedStr = startTimeStr
                if (!parsedStr.contains("T")) {
                    parsedStr += "T00:00:00"
                }
                LocalDateTime.parse(parsedStr)
            } catch (e: Exception) {
                Toast.makeText(context, "Invalid date format. Use YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val endTime = startTime.plusMinutes(durationMinutes.toLong())
            
            // Format according to lab spec: 2025-10-26T15:52:58.378Z
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val millis = String.format(".%03dZ", System.currentTimeMillis() % 1000)
            val startTimeIso = startTime.format(formatter) + millis
            val endTimeIso = endTime.format(formatter) + millis

            viewModel.createSchedule(habitId, startTimeIso, endTimeIso, durationMinutes, notes)
        }
    }

    private fun setupObservers() {
        viewModel.habits.observe(viewLifecycleOwner) { habitList ->
            habits = habitList
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, habits.map { it.name })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerHabit.adapter = adapter
        }

        viewModel.scheduleCreated.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(context, "Schedule created successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }.onFailure {
                Toast.makeText(context, "Failed to create schedule: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
