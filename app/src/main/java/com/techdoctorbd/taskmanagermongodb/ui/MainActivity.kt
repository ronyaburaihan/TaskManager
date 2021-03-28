package com.techdoctorbd.taskmanagermongodb.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.techdoctorbd.taskmanagermongodb.databinding.ActivityMainBinding
import com.techdoctorbd.taskmanagermongodb.utils.Constants.AUTH_TOKEN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.token.text = AUTH_TOKEN

    }
}