package com.techdoctorbd.taskmanagermongodb.ui.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techdoctorbd.taskmanagermongodb.data.api.TaskManagerApi
import com.techdoctorbd.taskmanagermongodb.data.models.Task
import com.techdoctorbd.taskmanagermongodb.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskManagerApi: TaskManagerApi
) : ViewModel() {

    var taskResponse: MutableLiveData<NetworkResult<Task>> = MutableLiveData()

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                val response = taskManagerApi.addTask(task)
                taskResponse.value = handleTaskResponse(response)
            } catch (e: Exception) {
                taskResponse.value = NetworkResult.Error(e.localizedMessage)
            }
        }
    }

    private fun handleTaskResponse(response: Response<Task>): NetworkResult<Task> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Request timeout")
            }

            response.code() == 404 -> {
                return NetworkResult.Error("Task not found")
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