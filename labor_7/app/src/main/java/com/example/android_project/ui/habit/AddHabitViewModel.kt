package com.example.android_project.ui.habit

import android.content.Context
import androidx.lifecycle.*
import com.example.android_project.model.Habit
import com.example.android_project.model.HabitCategory
import com.example.android_project.repository.HabitRepository
import kotlinx.coroutines.launch

class AddHabitViewModel(private val repository: HabitRepository) : ViewModel() {

    private val _categories = MutableLiveData<List<HabitCategory>>()
    val categories: LiveData<List<HabitCategory>> get() = _categories

    private val _habitCreated = MutableLiveData<Result<Habit>>()
    val habitCreated: LiveData<Result<Habit>> get() = _habitCreated

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val result = repository.getHabitCategories()
                _categories.value = result
            } catch (e: Exception) {
            } catch (e: Exception) {
            }
        }
    }

    fun createHabit(name: String, description: String, categoryId: Long, goal: String) {
        viewModelScope.launch {
            try {
                val habit = repository.createHabit(name, description, categoryId, goal)
                if (habit != null) {
                    _habitCreated.value = Result.success(habit)
                } else {
                    _habitCreated.value = Result.failure(Exception("Failed to create habit"))
                }
            } catch (e: Exception) {
                _habitCreated.value = Result.failure(e)
            }
        }
    }
}

class AddHabitViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddHabitViewModel::class.java)) {
            return AddHabitViewModel(HabitRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
