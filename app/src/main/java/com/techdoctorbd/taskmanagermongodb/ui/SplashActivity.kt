package com.techdoctorbd.taskmanagermongodb.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.techdoctorbd.taskmanagermongodb.R
import com.techdoctorbd.taskmanagermongodb.ui.auth.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}