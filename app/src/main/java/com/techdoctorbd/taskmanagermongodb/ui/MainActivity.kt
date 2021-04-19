package com.techdoctorbd.taskmanagermongodb.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.techdoctorbd.taskmanagermongodb.databinding.ActivityMainBinding
import com.techdoctorbd.taskmanagermongodb.ui.tasks.AddTaskActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.readProfile()

        binding.fabAddTask.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        mainViewModel.profileResponse.observe(this, {
            if (it.message.isNullOrEmpty()) {
                binding.user.text = "Welcome ${it.data?.name} !"
            } else
                binding.user.text = it.message
        })

    }
}