package com.techdoctorbd.taskmanagermongodb.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techdoctorbd.taskmanagermongodb.data.api.TaskManagerApi
import com.techdoctorbd.taskmanagermongodb.data.models.Task
import com.techdoctorbd.taskmanagermongodb.data.models.User
import com.techdoctorbd.taskmanagermongodb.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskManagerApi: TaskManagerApi
) : ViewModel() {

    private val calendar = Calendar.getInstance()
    var profileResponse: MutableLiveData<NetworkResult<User>> = MutableLiveData()
    var taskListResponse: MutableLiveData<NetworkResult<List<Task>>> = MutableLiveData()
    var pendingTaskList: MutableLiveData<List<Task>> = MutableLiveData()
    var todayTaskList: MutableLiveData<List<Task>> = MutableLiveData()
    var tomorrowsTaskList: MutableLiveData<List<Task>> = MutableLiveData()
    var upcomingTaskList: MutableLiveData<List<Task>> = MutableLiveData()
    private val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    private val todayDate: String = dateFormat.format(calendar.time)
    private var tomorrowDate: String

    init {
        //to get tomorrows date
        calendar.add(Calendar.DATE, 1)
        tomorrowDate = dateFormat.format(calendar.time)
        Log.d("DATE", "Tomorrow date : $tomorrowDate")

        viewModelScope.launch {
            try {
                val response = taskManagerApi.readProfile()
                profileResponse.value = handleRegisterResponse(response)
            } catch (e: Exception) {
                profileResponse.value = NetworkResult.Error(e.message)
            }
        }
    }

    fun getTasks(queryMap: HashMap<String, String>) {
        viewModelScope.launch {
            try {
                val response = taskManagerApi.getTasks(queryMap)
                handleTaskResponse(response)

            } catch (e: Exception) {
                taskListResponse.value = NetworkResult.Error(e.message)
            }
        }
    }

    private fun handleTaskResponse(response: Response<List<Task>>) {
        when {
            response.message().toString().contains("timeout") -> {
                taskListResponse.value = NetworkResult.Error("Request timeout")
            }

            response.code() == 404 -> {
                taskListResponse.value = NetworkResult.Error("User not found")
            }

            response.isSuccessful -> {
                val result = response.body()
                filterList(result!!)
            }

            else -> {
                taskListResponse.value = NetworkResult.Error(response.message())
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

    private fun filterList(data: List<Task>) {
        val pendingList: ArrayList<Task> = arrayListOf()
        val todayList: ArrayList<Task> = arrayListOf()
        val tomorrowList: ArrayList<Task> = arrayListOf()
        val upcomingList: ArrayList<Task> = arrayListOf()

        for (taskItem in data) {
            //to get task date
            calendar.timeInMillis = taskItem.taskTime.toLong()
            val taskDate = dateFormat.format(calendar.time)
            Log.d("DATE", "Task date : $taskDate")

            when {
                taskDate < todayDate && !taskItem.completed -> {
                    pendingList.add(taskItem)
                }
                taskDate == todayDate -> {
                    todayList.add(taskItem)
                }
                taskDate == tomorrowDate -> {
                    tomorrowList.add(taskItem)
                }
                taskDate > tomorrowDate -> {
                    upcomingList.add(taskItem)
                }
            }
        }

        if (!pendingList.isNullOrEmpty() ||
            !todayList.isNullOrEmpty() ||
            !tomorrowList.isNullOrEmpty() ||
            !upcomingList.isNullOrEmpty()
        ) {
            //to show task container
            taskListResponse.value = NetworkResult.Success(
                listOf(
                    Task(
                        description = "This is task",
                        taskTime = "This is timestamp"
                    )
                )
            )
        } else {
            //no task available
            taskListResponse.value = NetworkResult.Success(listOf())
        }

        pendingTaskList.value = pendingList
        todayTaskList.value = todayList
        tomorrowsTaskList.value = tomorrowList
        upcomingTaskList.value = upcomingList
    }
}