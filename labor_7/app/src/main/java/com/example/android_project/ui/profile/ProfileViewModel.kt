package com.example.android_project.ui.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android_project.model.HabitResponse
import com.example.android_project.model.ProfileResponseDto
import com.example.android_project.network.RetrofitClient
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val _profile = MutableLiveData<ProfileResponseDto?>()
    val profile: LiveData<ProfileResponseDto?> = _profile

    private val _habits = MutableLiveData<List<HabitResponse>>()
    val habits: LiveData<List<HabitResponse>> = _habits

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean> = _logoutSuccess

    fun fetchProfile() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.getInstance(getApplication()).getProfile()
                if (response.isSuccessful) {
                    val profileData = response.body()
                    _profile.value = profileData
                    profileData?.id?.let { fetchUserHabits(it) }
                } else {
                    _error.value = "Failed to load profile: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("ProfileVM", "Error fetching profile", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchUserHabits(userId: Long) {
        viewModelScope.launch {
            try {
                val habitsList = RetrofitClient.getInstance(getApplication()).getHabitsByUserId(userId)
                _habits.value = habitsList
            } catch (e: Exception) {
                Log.e("ProfileVM", "Error fetching habits", e)
                // Don't show error to user if habits fail, just log it
            }
        }
    }

    fun logout() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.getInstance(getApplication()).logout()
                if (response.isSuccessful) {
                    _logoutSuccess.value = true
                } else {
                    _error.value = "Logout failed: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
