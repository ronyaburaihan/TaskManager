package com.techdoctorbd.taskmanagermongodb.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techdoctorbd.taskmanagermongodb.api.TaskManagerApi
import com.techdoctorbd.taskmanagermongodb.models.User
import com.techdoctorbd.taskmanagermongodb.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val taskManagerApi: TaskManagerApi
) : ViewModel() {

    var registerResponse: MutableLiveData<NetworkResult<User>> = MutableLiveData()
    var userList: MutableLiveData<List<User>> = MutableLiveData()


    fun registerUser(user: User) {
        viewModelScope.launch {
            registerResponse.value = NetworkResult.Loading()
            try {
                val response = taskManagerApi.registerUser(user)
                //registerResponse.value = handleRegisterResponse(response)
            } catch (e: Exception) {
                registerResponse.value = NetworkResult.Error(e.message)
            }
        }
    }

    private fun handleRegisterResponse(response: Response<User>): NetworkResult<User> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Request timeout")
            }

            response.code() == 400 -> {
                return NetworkResult.Error("User already exists")
            }

            response.isSuccessful -> {
                val user = response.body()
                return NetworkResult.Success(user!!)
            }

            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }
}