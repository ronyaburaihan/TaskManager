package com.techdoctorbd.taskmanagermongodb.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.techdoctorbd.taskmanagermongodb.R
import com.techdoctorbd.taskmanagermongodb.adapters.CompletedTaskListAdapter
import com.techdoctorbd.taskmanagermongodb.data.models.Task
import com.techdoctorbd.taskmanagermongodb.databinding.ActivityCompletedTasksBinding
import com.techdoctorbd.taskmanagermongodb.ui.tasks.TaskViewModel
import com.techdoctorbd.taskmanagermongodb.utils.Constants.QUERY_COMPLETED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompletedTasksBinding
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompletedTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup toolbar
        setSupportActionBar(binding.toolbarLayout.toolbar)
        supportActionBar?.title = ""
        binding.toolbarLayout.toolbarTitle.text = resources.getString(R.string.completed_tasks)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_COMPLETED] = "true"

        taskViewModel.getCompletedTask(queries)

        taskViewModel.completedTaskList.observe(this, {
            if (it.message.isNullOrEmpty()) {

                if (it.data.isNullOrEmpty()) {
                    binding.shimmer.shimmerLayout.visibility = View.GONE
                    binding.scrollView.visibility = View.GONE
                    binding.empty.emptyLayout.visibility = View.VISIBLE
                } else
                    setupTaskList(it.data)
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                finish()
            }
        })

    }

    private fun setupTaskList(data: List<Task>?) {
        binding.shimmer.shimmerLayout.visibility = View.GONE
        binding.empty.emptyLayout.visibility = View.GONE
        binding.scrollView.visibility = View.VISIBLE

        val adapter = CompletedTaskListAdapter(data!!)
        binding.taskList.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}