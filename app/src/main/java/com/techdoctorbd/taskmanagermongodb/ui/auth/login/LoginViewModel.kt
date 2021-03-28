package com.techdoctorbd.taskmanagermongodb.ui.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techdoctorbd.taskmanagermongodb.api.TaskManagerApi
import com.techdoctorbd.taskmanagermongodb.models.AuthResponse
import com.techdoctorbd.taskmanagermongodb.models.User
import com.techdoctorbd.taskmanagermongodb.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val taskManagerApi: TaskManagerApi
) : ViewModel() {

    var loginResponse: MutableLiveData<NetworkResult<AuthResponse>> = MutableLiveData()

    fun loginUser(user: User) {
        viewModelScope.launch {
            try {
                val response = taskManagerApi.loginUser(user)
                loginResponse.value = handleRegisterResponse(response)
            } catch (e: Exception) {
                loginResponse.value = NetworkResult.Error(e.message)
            }
        }
    }

    private fun handleRegisterResponse(response: Response<AuthResponse>): NetworkResult<AuthResponse> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Request timeout")
            }

            response.code() == 404 -> {
                return NetworkResult.Error("User not found")
            }

            response.isSuccessful -> {
                val result = response.body()
                return NetworkResult.Success(result!!)
            }

            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }
}