package com.example.android_project.ui.habit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android_project.databinding.FragmentAddHabitBinding
import com.example.android_project.model.HabitCategory

class AddHabitFragment : Fragment() {

    private var _binding: FragmentAddHabitBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AddHabitViewModel
    private var categories: List<HabitCategory> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = AddHabitViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, factory)[AddHabitViewModel::class.java]

        setupObservers()
        viewModel.fetchCategories()

        binding.btnSaveHabit.setOnClickListener {
            val name = binding.etHabitName.text.toString()
            val description = binding.etHabitDescription.text.toString()
            val goal = binding.etGoal.text.toString()
            val selectedCategoryPosition = binding.spinnerCategory.selectedItemPosition

            if (name.isEmpty() || description.isEmpty() || goal.isEmpty() || categories.isEmpty()) {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val categoryId = categories[selectedCategoryPosition].id
            viewModel.createHabit(name, description, categoryId, goal)
        }
    }

    private fun setupObservers() {
        viewModel.categories.observe(viewLifecycleOwner) { categoryList ->
            categories = categoryList
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories.map { it.name })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = adapter
        }

        viewModel.habitCreated.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(context, "Habit created successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }.onFailure {
                Toast.makeText(context, "Failed to create habit: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
