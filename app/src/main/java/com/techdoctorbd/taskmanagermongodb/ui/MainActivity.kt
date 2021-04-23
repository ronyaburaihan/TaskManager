package com.techdoctorbd.taskmanagermongodb.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import com.techdoctorbd.taskmanagermongodb.R
import com.techdoctorbd.taskmanagermongodb.adapters.TaskListAdapter
import com.techdoctorbd.taskmanagermongodb.data.UserPreferences
import com.techdoctorbd.taskmanagermongodb.data.models.Task
import com.techdoctorbd.taskmanagermongodb.databinding.ActivityMainBinding
import com.techdoctorbd.taskmanagermongodb.ui.auth.login.LoginActivity
import com.techdoctorbd.taskmanagermongodb.ui.tasks.AddTaskActivity
import com.techdoctorbd.taskmanagermongodb.ui.tasks.TaskViewModel
import com.techdoctorbd.taskmanagermongodb.utils.CustomProgressDialog
import com.techdoctorbd.taskmanagermongodb.utils.NoScrollListView
import com.techdoctorbd.taskmanagermongodb.utils.TaskItemClickListener
import com.techdoctorbd.taskmanagermongodb.utils.runDelayed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TaskItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var userPreferences: UserPreferences
    private lateinit var progressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init
        userPreferences = UserPreferences(this)
        progressDialog = CustomProgressDialog(this)

        setSupportActionBar(binding.content.toolbar)
        setUpDrawerToggle()

        binding.content.fabAddTask.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        binding.content.tvAddNewTask.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        binding.sidebarLayout.ivCloseDrawer.setOnClickListener {
            closeDrawer()
        }

        binding.sidebarLayout.tvLogout.setOnClickListener {
            logOut()
        }

        mainViewModel.profileResponse.observe(this, {
            if (it.message.isNullOrEmpty()) {
                binding.sidebarLayout.txtDisplayName.text = it.data?.name
            } else {
                //to show error message
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                //if Unauthorized logout user
                if (it.message == "Unauthorized") {
                    logOut()
                }
            }
        })

        mainViewModel.taskListResponse.observe(this, {
            if (it.message.isNullOrEmpty()) {
                if (!it.data.isNullOrEmpty()) {
                    binding.content.layoutWelcome.visibility = View.GONE
                    binding.content.scrollView.visibility = View.VISIBLE

                    binding.content.shimmerLayout.stopShimmer()
                    binding.content.shimmerLayout.visibility = View.GONE

                } else {
                    binding.content.layoutWelcome.visibility = View.VISIBLE
                    binding.content.scrollView.visibility = View.GONE

                    binding.content.shimmerLayout.stopShimmer()
                    binding.content.shimmerLayout.visibility = View.GONE
                }
            }
        })

        mainViewModel.pendingTaskList.observe(this, {
            if (it.isNullOrEmpty())
                hideList(binding.content.pendingContainer)
            else
                setDataToList(binding.content.pendingContainer, binding.content.taskListPending, it)
        })

        mainViewModel.todayTaskList.observe(this, {
            if (it.isNullOrEmpty())
                hideList(binding.content.todayContainer)
            else
                setDataToList(binding.content.todayContainer, binding.content.taskListToday, it)
        })

        mainViewModel.tomorrowsTaskList.observe(this, {
            if (it.isNullOrEmpty())
                hideList(binding.content.tomorrowContainer)
            else
                setDataToList(
                    binding.content.tomorrowContainer,
                    binding.content.taskListTomorrow,
                    it
                )
        })

        mainViewModel.upcomingTaskList.observe(this, {
            if (it.isNullOrEmpty())
                hideList(binding.content.upcomingContainer)
            else
                setDataToList(
                    binding.content.upcomingContainer,
                    binding.content.taskListUpcoming,
                    it
                )
        })

        taskViewModel.editTaskResponse.observe(this, {
            progressDialog.dismiss()
            if (!it.message.isNullOrEmpty())
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun logOut() {
        lifecycleScope.launch { userPreferences.deleteToken() }
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun setDataToList(
        layout: LinearLayout,
        listView: NoScrollListView,
        taskList: List<Task>
    ) {
        Log.d("TaskID", taskList[0]._id!!)
        layout.visibility = View.VISIBLE
        val taskListAdapter = TaskListAdapter(taskList, this, this)
        listView.adapter = taskListAdapter
    }

    private fun hideList(layout: LinearLayout) {
        layout.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getTasks(HashMap())
    }

    override fun onClick(task: Task, isCompleted: Boolean) {
        task.completed = isCompleted
        progressDialog.show("Please wait")
        taskViewModel.updateTask(task._id!!, task)
    }

    private fun setUpDrawerToggle() {
        val toggle = object : ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.content.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                binding.main.translationX = slideOffset * drawerView.width
                (binding.drawerLayout).bringChildToFront(drawerView)
                (binding.drawerLayout).requestLayout()
            }
        }
        toggle.setToolbarNavigationClickListener {
            if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_drawer,
                theme
            )
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun closeDrawer() {
        if (binding.drawerLayout.isDrawerOpen(binding.leftDrawer)) runDelayed(50) {
            binding.drawerLayout.closeDrawer(
                binding.leftDrawer
            )
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(binding.leftDrawer)) runDelayed(50) {
            binding.drawerLayout.closeDrawer(
                binding.leftDrawer
            )
        } else
            super.onBackPressed()

    }
}