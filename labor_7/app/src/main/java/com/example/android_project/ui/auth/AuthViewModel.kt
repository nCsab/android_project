package com.example.android_project.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android_project.model.AuthResponse
import com.example.android_project.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(application)

    private val _authResult = MutableLiveData<Result<AuthResponse>>()
    val authResult: LiveData<Result<AuthResponse>> = _authResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                handleResponse(response)
            } catch (e: Exception) {
                _authResult.postValue(Result.failure(e))
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                val response = repository.register(email, password, name)
                handleResponse(response)
            } catch (e: Exception) {
                _authResult.postValue(Result.failure(e))
            }
        }
    }

    private fun handleResponse(response: Response<AuthResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _authResult.postValue(Result.success(response.body()!!))
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"
            _authResult.postValue(Result.failure(Exception("Auth failed (${response.code()}): $errorBody")))
        }
    }
}
