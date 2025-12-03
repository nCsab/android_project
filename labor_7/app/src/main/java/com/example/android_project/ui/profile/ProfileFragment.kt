package com.example.android_project.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_project.R

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var adapter: UserHabitsAdapter

    private lateinit var ivProfileImage: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var tvEmail: TextView
    private lateinit var rvUserHabits: RecyclerView
    private lateinit var btnAddHabit: Button
    private lateinit var btnLogout: Button
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Views
        ivProfileImage = view.findViewById(R.id.ivProfileImage)
        tvUsername = view.findViewById(R.id.tvUsername)
        tvEmail = view.findViewById(R.id.tvEmail)
        rvUserHabits = view.findViewById(R.id.rvUserHabits)
        btnAddHabit = view.findViewById(R.id.btnAddHabit)
        btnLogout = view.findViewById(R.id.btnLogout)
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar)

        // Setup RecyclerView
        adapter = UserHabitsAdapter(emptyList())
        rvUserHabits.layoutManager = LinearLayoutManager(context)
        rvUserHabits.adapter = adapter

        // Observe ViewModel
        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                tvUsername.text = it.username
                tvEmail.text = it.email
                // Load image if URL is present (using Glide or Picasso would be better, but keeping it simple for now)
            }
        }

        viewModel.habits.observe(viewLifecycleOwner) { habits ->
            adapter.updateData(habits)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.logoutSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                // Navigate to Login
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }
        }

        // Fetch Data
        viewModel.fetchProfile()

        // Buttons
        btnAddHabit.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_addHabitFragment)
        }

        btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.logout()
            }
            .setNegativeButton("No", null)
            .show()
    }
}
