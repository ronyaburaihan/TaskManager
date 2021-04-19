package com.techdoctorbd.taskmanagermongodb.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.techdoctorbd.taskmanagermongodb.databinding.ActivityMainBinding
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

        mainViewModel.profileResponse.observe(this, {
            if (it.message.isNullOrEmpty()) {
                binding.user.text = it.data.toString()
            } else
                binding.user.text = it.message
        })
    }
}