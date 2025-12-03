package com.example.android_project.ui.schedule

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android_project.model.ScheduleResponse
import com.example.android_project.network.RetrofitClient
import kotlinx.coroutines.launch

class ScheduleDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val _schedule = MutableLiveData<ScheduleResponse?>()
    val schedule: LiveData<ScheduleResponse?> = _schedule

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> = _deleteSuccess

    fun fetchScheduleDetails(scheduleId: Long) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.getInstance(getApplication()).getScheduleById(scheduleId)
                if (response.isSuccessful) {
                    _schedule.value = response.body()
                } else {
                    _error.value = "Failed to load schedule details: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                Log.e("ScheduleDetailsVM", "Error fetching details", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteSchedule(scheduleId: Long) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.getInstance(getApplication()).deleteSchedule(scheduleId)
                if (response.isSuccessful) {
                    _deleteSuccess.value = true
                } else {
                    _error.value = "Failed to delete schedule: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
