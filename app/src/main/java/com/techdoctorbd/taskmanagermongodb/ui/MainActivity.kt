package com.techdoctorbd.taskmanagermongodb.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.techdoctorbd.taskmanagermongodb.adapters.TaskListAdapter
import com.techdoctorbd.taskmanagermongodb.data.UserPreferences
import com.techdoctorbd.taskmanagermongodb.data.models.Task
import com.techdoctorbd.taskmanagermongodb.databinding.ActivityMainBinding
import com.techdoctorbd.taskmanagermongodb.ui.auth.login.LoginActivity
import com.techdoctorbd.taskmanagermongodb.ui.tasks.AddTaskActivity
import com.techdoctorbd.taskmanagermongodb.utils.NoScrollListView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init
        userPreferences = UserPreferences(this)

        binding.fabAddTask.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        binding.tvAddNewTask.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        mainViewModel.profileResponse.observe(this, {
            if (it.message.isNullOrEmpty()) {
                binding.toolbarTitle.text = "Welcome ${it.data?.name}!"
            } else {
                //to show error message
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                //if Unauthorized logout user
                if (it.message == "Unauthorized") {
                    lifecycleScope.launch { userPreferences.deleteToken() }
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        })

        mainViewModel.taskListResponse.observe(this, {
            if (it.message.isNullOrEmpty()) {
                if (!it.data.isNullOrEmpty()) {
                    binding.layoutWelcome.visibility = View.GONE
                    binding.scrollView.visibility = View.VISIBLE
                } else {
                    binding.layoutWelcome.visibility = View.VISIBLE
                    binding.scrollView.visibility = View.GONE
                }
            }
        })

        mainViewModel.pendingTaskList.observe(this, {
            if (it.isNullOrEmpty())
                hideList(binding.pendingContainer)
            else
                setDataToList(binding.pendingContainer, binding.taskListPending, it)
        })

        mainViewModel.todayTaskList.observe(this, {
            if (it.isNullOrEmpty())
                hideList(binding.todayContainer)
            else
                setDataToList(binding.todayContainer, binding.taskListToday, it)
        })

        mainViewModel.tomorrowsTaskList.observe(this, {
            if (it.isNullOrEmpty())
                hideList(binding.tomorrowContainer)
            else
                setDataToList(binding.tomorrowContainer, binding.taskListTomorrow, it)
        })

        mainViewModel.upcomingTaskList.observe(this, {
            if (it.isNullOrEmpty())
                hideList(binding.upcomingContainer)
            else
                setDataToList(binding.upcomingContainer, binding.taskListUpcoming, it)
        })
    }

    private fun setDataToList(
        layout: LinearLayout,
        listView: NoScrollListView,
        taskList: List<Task>
    ) {
        layout.visibility = View.VISIBLE
        val taskListAdapter = TaskListAdapter(taskList, this)
        listView.adapter = taskListAdapter
    }

    private fun hideList(layout: LinearLayout) {
        layout.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getTasks(HashMap())
    }

}