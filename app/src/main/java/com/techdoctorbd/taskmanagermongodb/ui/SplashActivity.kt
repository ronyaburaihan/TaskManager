package com.techdoctorbd.taskmanagermongodb.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.techdoctorbd.taskmanagermongodb.R
import com.techdoctorbd.taskmanagermongodb.data.UserPreferences
import com.techdoctorbd.taskmanagermongodb.ui.auth.login.LoginActivity
import com.techdoctorbd.taskmanagermongodb.utils.Constants.AUTH_TOKEN

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val userPreferences = UserPreferences(this)

        userPreferences.authToken.asLiveData().observe(this, {
            if (it.isNullOrEmpty()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                AUTH_TOKEN = it
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
    }
}