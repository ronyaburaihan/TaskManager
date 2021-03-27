package com.techdoctorbd.taskmanagermongodb.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.techdoctorbd.taskmanagermongodb.R
import com.techdoctorbd.taskmanagermongodb.models.User
import com.techdoctorbd.taskmanagermongodb.ui.auth.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}