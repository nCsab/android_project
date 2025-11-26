package com.example.android_project.ui.schedule

import android.content.Context
import androidx.lifecycle.*
import com.example.android_project.model.HabitResponse
import com.example.android_project.model.ScheduleResponse
import com.example.android_project.repository.HabitRepository
import com.example.android_project.repository.ScheduleRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CreateScheduleViewModel(
    private val scheduleRepository: ScheduleRepository,
    private val habitRepository: HabitRepository
) : ViewModel() {

    private val _habits = MutableLiveData<List<HabitResponse>>()
    val habits: LiveData<List<HabitResponse>> get() = _habits

    private val _scheduleCreated = MutableLiveData<Result<Boolean>>()
    val scheduleCreated: LiveData<Result<Boolean>> get() = _scheduleCreated

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchHabits() {
        viewModelScope.launch {
            try {
                val result = habitRepository.getHabits()
                _habits.value = result
            } catch (e: Exception) {
                _error.value = "Failed to load habits: ${e.message}"
            }
        }
    }

    fun createSchedule(habitId: Long, startTime: String, endTime: String, durationMinutes: Int, notes: String) {
        viewModelScope.launch {
            try {
                val errorMsg = scheduleRepository.createSchedule(habitId, startTime, endTime, durationMinutes, notes)
                if (errorMsg == null) {
                    _scheduleCreated.value = Result.success(true)
                } else {
                    _scheduleCreated.value = Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                _scheduleCreated.value = Result.failure(e)
            }
        }
    }
}

class CreateScheduleViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateScheduleViewModel::class.java)) {
            return CreateScheduleViewModel(
                ScheduleRepository(context),
                HabitRepository(context)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
