package com.example.android_project.ui.home

import androidx.lifecycle.*
import com.example.android_project.model.ScheduleResponse
import com.example.android_project.repository.ScheduleRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ScheduleRepository) : ViewModel() {

    private val _schedules = MutableLiveData<List<ScheduleResponse>>()
    val schedules: LiveData<List<ScheduleResponse>> get() = _schedules

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getScheduleByDay() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getScheduleByDay()
                val today = java.time.LocalDate.now()
                val todaySchedules = response.filter { schedule ->
                    schedule.startTime?.toLocalDate() == today
                }
                _schedules.value = todaySchedules
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load schedules"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
