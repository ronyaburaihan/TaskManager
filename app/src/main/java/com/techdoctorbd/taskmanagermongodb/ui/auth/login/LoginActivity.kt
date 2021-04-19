package com.techdoctorbd.taskmanagermongodb.ui.auth.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.techdoctorbd.taskmanagermongodb.R
import com.techdoctorbd.taskmanagermongodb.data.UserPreferences
import com.techdoctorbd.taskmanagermongodb.data.models.User
import com.techdoctorbd.taskmanagermongodb.databinding.ActivityLoginBinding
import com.techdoctorbd.taskmanagermongodb.ui.MainActivity
import com.techdoctorbd.taskmanagermongodb.ui.auth.register.RegisterActivity
import com.techdoctorbd.taskmanagermongodb.utils.Constants.AUTH_TOKEN
import com.techdoctorbd.taskmanagermongodb.utils.CustomProgressDialog
import com.techdoctorbd.taskmanagermongodb.utils.checkConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog: CustomProgressDialog
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init
        progressDialog = CustomProgressDialog(this)
        userPreferences = UserPreferences(this)

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edEmail.text.toString().trim()
            val password = binding.edPassword.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                binding.edEmail.error = "This field can't be empty"
                binding.edEmail.requestFocus()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edEmail.error = "Enter valid email address"
                binding.edEmail.requestFocus()
            }

            if (TextUtils.isEmpty(password)) {
                binding.edPassword.error = "This field can't be empty"
                binding.edPassword.requestFocus()
            } else if (password.length < 6) {
                binding.edPassword.error = "Minimum 6 character required"
                binding.edPassword.requestFocus()
            } else if (!checkConnection(this)) {
                val dialog = Dialog(this@LoginActivity)
                dialog.setTitle("No Internet Connection !")
                dialog.setContentView(R.layout.layout_dialog)
                dialog.setCancelable(false)
                Objects.requireNonNull(dialog.window)!!.attributes.windowAnimations =
                    R.style.animation_dialog
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val tvOk: TextView = dialog.findViewById(R.id.popup_button)
                val message: TextView = dialog.findViewById(R.id.popup_textView)
                message.setText(R.string.no_internet_connection)
                tvOk.setOnClickListener { dialog.dismiss() }
                dialog.show()
            } else {
                progressDialog.show("Please Wait")
                loginViewModel.loginUser(User(email = email, password = password))
            }
        }

        loginViewModel.loginResponse.observe(this, {
            progressDialog.dismiss()
            if (it.message.isNullOrEmpty()) {
                if (it.data!!.token.isNotEmpty()) {
                    AUTH_TOKEN = it.data.token
                    lifecycleScope.launch {
                        userPreferences.saveAuthToken(AUTH_TOKEN)
                    }
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            } else
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })
    }
}