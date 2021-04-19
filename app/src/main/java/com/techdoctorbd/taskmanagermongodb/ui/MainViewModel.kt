package com.techdoctorbd.taskmanagermongodb.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techdoctorbd.taskmanagermongodb.data.api.TaskManagerApi
import com.techdoctorbd.taskmanagermongodb.data.models.User
import com.techdoctorbd.taskmanagermongodb.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskManagerApi: TaskManagerApi
) : ViewModel() {

    var profileResponse: MutableLiveData<NetworkResult<User>> = MutableLiveData()

    fun readProfile() {
        viewModelScope.launch {
            try {
                val response = taskManagerApi.readProfile()
                profileResponse.value = handleRegisterResponse(response)
            } catch (e: Exception) {
                profileResponse.value = NetworkResult.Error(e.message)
            }
        }
    }

    private fun handleRegisterResponse(response: Response<User>): NetworkResult<User> {
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