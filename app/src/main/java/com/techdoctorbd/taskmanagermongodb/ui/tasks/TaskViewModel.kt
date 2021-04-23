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

    var addTaskResponse: MutableLiveData<NetworkResult<Task>> = MutableLiveData()
    var editTaskResponse: MutableLiveData<NetworkResult<Task>> = MutableLiveData()
    var deleteTaskResponse: MutableLiveData<NetworkResult<Task>> = MutableLiveData()
    var completedTaskList: MutableLiveData<NetworkResult<List<Task>>> = MutableLiveData()

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                val response = taskManagerApi.addTask(task)
                addTaskResponse.value = handleTaskResponse(response)
            } catch (e: Exception) {
                addTaskResponse.value = NetworkResult.Error(e.localizedMessage)
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                val response = taskManagerApi.deleteTask(taskId)
                deleteTaskResponse.value = handleTaskResponse(response)
            } catch (e: Exception) {
                deleteTaskResponse.value = NetworkResult.Error(e.localizedMessage)
            }
        }
    }

    fun getCompletedTask(query: HashMap<String, String>) {
        viewModelScope.launch {
            try {
                val response = taskManagerApi.getTasks(query)
                completedTaskList.value = handleTaskListResponse(response)
            } catch (exception: Exception) {
                completedTaskList.value = NetworkResult.Error(exception.localizedMessage)
            }
        }
    }

    fun updateTask(taskId: String, taskItem: Task) {
        viewModelScope.launch {
            try {
                val task = Task(
                    description = taskItem.description,
                    completed = taskItem.completed,
                    taskTime = taskItem.taskTime
                )
                val response = taskManagerApi.updateTask(taskId, task)
                editTaskResponse.value = handleTaskResponse(response)
            } catch (e: Exception) {
                editTaskResponse.value = NetworkResult.Error(e.localizedMessage)
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

    private fun handleTaskListResponse(response: Response<List<Task>>): NetworkResult<List<Task>> {

        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Request timeout")
            }

            response.code() == 404 -> {
                NetworkResult.Error("User not found")
            }

            response.isSuccessful -> {
                NetworkResult.Success(response.body()!!)
            }

            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

}